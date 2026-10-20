package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.engine.StorageManager
import com.example.model.AppSettings
import com.example.model.UserProgress
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private lateinit var storageManager: StorageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        storageManager = StorageManager(applicationContext)

        setContent {
            var progress by remember { mutableStateOf(storageManager.loadUserProgress()) }
            var currentLang by remember { mutableStateOf(progress.settings.language) }

            // Navigation screen stack
            val screenStack = remember { mutableStateListOf("launch") }
            val currentScreen = screenStack.lastOrNull() ?: "home"

            var activeWorldId by remember { mutableIntStateOf(1) }

            fun navigateTo(screen: String) {
                if (screenStack.lastOrNull() != screen) {
                    screenStack.add(screen)
                }
            }

            fun navigateBack() {
                if (screenStack.size > 1) {
                    screenStack.removeAt(screenStack.lastIndex)
                }
            }

            fun toggleLanguage() {
                val next = if (currentLang == "bn") "en" else "bn"
                currentLang = next
                val updated = progress.copy(settings = progress.settings.copy(language = next))
                progress = updated
                storageManager.saveSettings(updated.settings)
            }

            // Handle system back gesture
            BackHandler(enabled = screenStack.size > 1) {
                navigateBack()
            }

            MyApplicationTheme(darkTheme = true) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (currentScreen) {
                        "launch" -> {
                            LaunchScreen(
                                currentLang = currentLang,
                                onToggleLang = ::toggleLanguage,
                                onStart = {
                                    screenStack.clear()
                                    screenStack.add("home")
                                }
                            )
                        }

                        "home" -> {
                            HomeScreen(
                                progress = progress,
                                currentLang = currentLang,
                                onToggleLang = ::toggleLanguage,
                                onNavigate = { destination ->
                                    navigateTo(destination)
                                }
                            )
                        }

                        "expedition", "chemistry_universe" -> {
                            ExpeditionScreen(
                                progress = progress,
                                currentLang = currentLang,
                                onToggleLang = ::toggleLanguage,
                                onSelectLesson = { wId ->
                                    activeWorldId = wId
                                    navigateTo("lesson")
                                },
                                onSelectGame = { wId ->
                                    activeWorldId = wId
                                    navigateTo("chemistry_game")
                                },
                                onBack = ::navigateBack
                            )
                        }

                        "lesson" -> {
                            LessonScreen(
                                worldId = activeWorldId,
                                currentLang = currentLang,
                                score = progress.totalScore,
                                onToggleLang = ::toggleLanguage,
                                onLaunchGame = {
                                    navigateTo("chemistry_game")
                                },
                                onBack = ::navigateBack
                            )
                        }

                        "chemistry_game" -> {
                            ChemistryGameScreen(
                                worldId = activeWorldId,
                                currentLang = currentLang,
                                score = progress.totalScore,
                                onToggleLang = ::toggleLanguage,
                                onRecordQuizResult = { wId, topicKey, correct, pts ->
                                    storageManager.recordQuizResult(wId, topicKey, correct, pts)
                                    // Unlock next world if this world won
                                    if (correct && wId < 10) {
                                        storageManager.unlockWorld(wId + 1)
                                    }
                                    progress = storageManager.loadUserProgress()
                                },
                                onBack = ::navigateBack
                            )
                        }

                        "memory_universe" -> {
                            MemoryGameScreen(
                                currentLang = currentLang,
                                score = progress.totalScore,
                                onToggleLang = ::toggleLanguage,
                                onAddScore = { pts ->
                                    storageManager.addPoints(pts)
                                    progress = storageManager.loadUserProgress()
                                },
                                onBack = ::navigateBack
                            )
                        }

                        "daily_challenge" -> {
                            MemoryGameScreen(
                                currentLang = currentLang,
                                score = progress.totalScore,
                                onToggleLang = ::toggleLanguage,
                                onAddScore = { pts ->
                                    storageManager.addPoints(pts + 100) // Daily challenge bonus
                                    progress = storageManager.loadUserProgress()
                                },
                                onBack = ::navigateBack
                            )
                        }

                        "formula_forge" -> {
                            FormulaForgeScreen(
                                currentLang = currentLang,
                                score = progress.totalScore,
                                onToggleLang = ::toggleLanguage,
                                onSynthesizeCompound = { formula ->
                                    storageManager.addPoints(50)
                                    progress = storageManager.loadUserProgress()
                                },
                                onBack = ::navigateBack
                            )
                        }

                        "practice_lab" -> {
                            PracticeLabScreen(
                                currentLang = currentLang,
                                score = progress.totalScore,
                                onToggleLang = ::toggleLanguage,
                                onBack = ::navigateBack
                            )
                        }

                        "notebook" -> {
                            NotebookScreen(
                                currentLang = currentLang,
                                score = progress.totalScore,
                                bookmarkedIds = progress.bookmarkedReviewIds,
                                onToggleBookmark = { id ->
                                    storageManager.toggleBookmark(id)
                                    progress = storageManager.loadUserProgress()
                                },
                                onToggleLang = ::toggleLanguage,
                                onBack = ::navigateBack
                            )
                        }

                        "progress" -> {
                            DashboardScreen(
                                progress = progress,
                                currentLang = currentLang,
                                onToggleLang = ::toggleLanguage,
                                onLaunchDailyChallenge = {
                                    navigateTo("daily_challenge")
                                },
                                onBack = ::navigateBack
                            )
                        }

                        "settings" -> {
                            SettingsScreen(
                                settings = progress.settings,
                                score = progress.totalScore,
                                onUpdateSettings = { newSettings ->
                                    storageManager.saveSettings(newSettings)
                                    currentLang = newSettings.language
                                    progress = progress.copy(settings = newSettings)
                                },
                                onResetProgress = {
                                    storageManager.resetProgress()
                                    progress = storageManager.loadUserProgress()
                                    currentLang = progress.settings.language
                                },
                                onBack = ::navigateBack
                            )
                        }

                        else -> {
                            HomeScreen(
                                progress = progress,
                                currentLang = currentLang,
                                onToggleLang = ::toggleLanguage,
                                onNavigate = { destination ->
                                    navigateTo(destination)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
