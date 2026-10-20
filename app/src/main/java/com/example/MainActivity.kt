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
import com.example.data.ChemistryUniverseData
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
                            LaunchedEffect(activeWorldId) {
                                val concepts = ChemistryUniverseData.REVIEW_ITEMS
                                    .filter { it.worldId == activeWorldId }
                                    .flatMap { listOf(it.id, it.conceptKey) }
                                storageManager.recordLessonCompletion(activeWorldId, "w${activeWorldId}_lesson", concepts)
                                progress = storageManager.loadUserProgress()
                            }
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
                                settings = progress.settings,
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
                                    storageManager.addPoints(pts, isMemory = true)
                                    progress = storageManager.loadUserProgress()
                                },
                                onBack = ::navigateBack
                            )
                        }

                        "daily_challenge" -> {
                            DailyChallengeScreen(
                                currentLang = currentLang,
                                score = progress.totalScore,
                                dailyState = progress.dailyChallenge,
                                onToggleLang = ::toggleLanguage,
                                onCompleteDailyChallenge = {
                                    val awarded = storageManager.completeDailyChallenge()
                                    progress = storageManager.loadUserProgress()
                                    awarded
                                },
                                onBack = ::navigateBack
                            )
                        }

                        "formula_forge" -> {
                            FormulaForgeScreen(
                                currentLang = currentLang,
                                score = progress.totalScore,
                                synthesizedFormulas = progress.synthesizedFormulas,
                                onToggleLang = ::toggleLanguage,
                                onSynthesizeCompound = { formula ->
                                    val awarded = storageManager.recordFormulaSynthesis(formula)
                                    progress = storageManager.loadUserProgress()
                                    awarded
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
                                progress = progress,
                                onRecordReviewResult = { cardId, wasCorrect ->
                                    storageManager.recordReviewResult(cardId, wasCorrect)
                                    progress = storageManager.loadUserProgress()
                                },
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
