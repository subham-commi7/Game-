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
import com.example.engine.TutorContext
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
            var activeLevelNumber by remember { mutableIntStateOf(1) }
            var activeTutorContext by remember { mutableStateOf<TutorContext?>(null) }

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

            // Always synchronize latest user progress when returning to any screen
            LaunchedEffect(currentScreen) {
                progress = storageManager.loadUserProgress()
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
                                onSelectLevel = { lvlNum ->
                                    activeLevelNumber = lvlNum
                                    navigateTo("level_play")
                                },
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

                        "level_play" -> {
                            LevelPlayScreen(
                                levelNumber = activeLevelNumber,
                                currentLang = currentLang,
                                score = progress.totalScore,
                                isLevelAlreadyCompleted = progress.completedLevels.contains(activeLevelNumber),
                                savedStars = progress.levelStars[activeLevelNumber] ?: 0,
                                reducedMotion = progress.settings.reducedMotion,
                                visualEffectsHigh = progress.settings.visualEffectsHigh,
                                onToggleLang = ::toggleLanguage,
                                onCompleteLevel = { lvlNum, earnedScore, stars, conceptKey ->
                                    val awarded = storageManager.recordLevelCompletion(lvlNum, earnedScore, stars, conceptKey)
                                    progress = storageManager.loadUserProgress()
                                    awarded
                                },
                                onOpenAiTutor = { level, userLastAnswer, isCorrect ->
                                    val expAns = if (currentLang == "bn") {
                                        level.question.optionsBn.getOrNull(level.question.correctIndex) ?: ""
                                    } else {
                                        level.question.optionsEn.getOrNull(level.question.correctIndex) ?: ""
                                    }
                                    activeTutorContext = TutorContext(
                                        language = currentLang,
                                        worldId = level.worldId,
                                        levelNumber = level.levelNumber,
                                        levelTitle = if (currentLang == "bn") level.titleBn else level.titleEn,
                                        objective = if (currentLang == "bn") level.objectiveBn else level.objectiveEn,
                                        conceptExplanation = if (currentLang == "bn") level.conceptExplanationBn else level.conceptExplanationEn,
                                        keyRule = if (currentLang == "bn") level.keyRuleBn else level.keyRuleEn,
                                        pharmacyRelevance = if (currentLang == "bn") level.pharmacyRelevanceBn else level.pharmacyRelevanceEn,
                                        lastSubmittedAnswer = userLastAnswer,
                                        isCorrect = isCorrect,
                                        expectedAnswer = expAns
                                    )
                                    navigateTo("ai_tutor")
                                },
                                onBack = ::navigateBack
                            )
                        }

                        "ai_tutor" -> {
                            val defaultGeneralTutorContext = remember(currentLang) {
                                TutorContext(
                                    language = currentLang,
                                    worldId = 1,
                                    levelNumber = 1,
                                    levelTitle = if (currentLang == "bn") "সাধারণ রসায়ন সহায়ক" else "General Chemistry Mentor",
                                    objective = if (currentLang == "bn") "বি.ফার্ম মৌলিক রসায়ন ও সার্বিক বিজ্ঞানের ধারণা ব্যাখ্যা" else "Foundational B.Pharm Chemistry Concepts & Inquiry",
                                    conceptExplanation = if (currentLang == "bn") "পরমাণুর গঠন, পর্যায় সারণি, রাসায়নিক বন্ধন, সমীকরণ সমতাকরণ, মোলারিটি, এবং ফার্মা ফর্মুলেশনের যে কোনো প্রশ্ন জিজ্ঞাসা করুন।" else "Ask any question regarding atomic structure, periodic properties, chemical bonding, stoichiometry, pH, or pharmaceutical solutions.",
                                    keyRule = if (currentLang == "bn") "রসায়ন মুখস্থের বিষয় নয়; এটি জীবন ও ঔষধ বিজ্ঞানের আণবিক যুক্তি।" else "Chemistry is not memorization; it is the universal molecular logic of life and pharmacy.",
                                    pharmacyRelevance = if (currentLang == "bn") "ফার্মাসিউটিক্যাল ইনঅর্গানিক কেমিস্ট্রি (BP106T) ও অর্গানিক কেমিস্ট্রি (BP202T)" else "Pharmaceutical Inorganic Chemistry (BP106T) & Organic Chemistry (BP202T)"
                                )
                            }
                            AiTutorScreen(
                                tutorContext = activeTutorContext ?: defaultGeneralTutorContext,
                                currentLang = currentLang,
                                score = progress.totalScore,
                                onToggleLang = ::toggleLanguage,
                                onBack = {
                                    activeTutorContext = null
                                    navigateBack()
                                }
                            )
                        }

                        "lesson" -> {
                            val isLessonAlreadyLearned = progress.worldProgressMap[activeWorldId]?.lessonsCompleted?.contains("w${activeWorldId}_lesson") == true
                            LessonScreen(
                                worldId = activeWorldId,
                                currentLang = currentLang,
                                score = progress.totalScore,
                                isAlreadyLearned = isLessonAlreadyLearned,
                                onToggleLang = ::toggleLanguage,
                                onCompleteLesson = {
                                    val concepts = ChemistryUniverseData.REVIEW_ITEMS
                                        .filter { it.worldId == activeWorldId }
                                        .flatMap { listOf(it.id, it.conceptKey) }
                                    storageManager.recordLessonCompletion(activeWorldId, "w${activeWorldId}_lesson", concepts)
                                    if (!isLessonAlreadyLearned) {
                                        storageManager.addPoints(50, isMemory = false)
                                    }
                                    progress = storageManager.loadUserProgress()
                                },
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
