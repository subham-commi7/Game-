package com.example.engine

import android.content.Context
import android.content.SharedPreferences
import com.example.data.ChemistryUniverseData
import com.example.model.*
import java.text.SimpleDateFormat
import java.util.*

class StorageManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("neurovault_save_data", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LANG = "key_language"
        private const val KEY_SOUND = "key_sound"
        private const val KEY_HAPTICS = "key_haptics"
        private const val KEY_REDUCED_MOTION = "key_reduced_motion"
        private const val KEY_EFFECTS = "key_effects"
        private const val KEY_CHALLENGE = "key_challenge"
        private const val KEY_SCORE = "key_total_score"
        private const val KEY_CHEM_SCORE = "key_chem_score"
        private const val KEY_MEM_SCORE = "key_mem_score"
        private const val KEY_STREAK = "key_streak"
        private const val KEY_LAST_ACTIVE = "key_last_active"
        private const val KEY_UNLOCKED_WORLDS = "key_unlocked_worlds"
        private const val KEY_BOOKMARKS = "key_bookmarks"
        private const val KEY_SYNTHESIZED_FORMULAS = "key_synthesized_formulas"
        private const val KEY_LEARNED_CONCEPTS = "key_learned_concepts"
        private const val KEY_DAILY_DATE = "key_daily_date"
        private const val KEY_DAILY_COMPLETED = "key_daily_completed"
        private const val KEY_DAILY_SCORE = "key_daily_score"
        private const val KEY_UNLOCKED_LEVELS = "key_unlocked_levels"
        private const val KEY_COMPLETED_LEVELS = "key_completed_levels"

        const val DAILY_CHALLENGE_REWARD: Int = 250
        const val UNIQUE_SYNTHESIS_REWARD: Int = 50
    }

    fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }

    fun loadSettings(): AppSettings {
        return AppSettings(
            language = prefs.getString(KEY_LANG, "bn") ?: "bn",
            soundEnabled = prefs.getBoolean(KEY_SOUND, true),
            hapticsEnabled = prefs.getBoolean(KEY_HAPTICS, true),
            reducedMotion = prefs.getBoolean(KEY_REDUCED_MOTION, false),
            visualEffectsHigh = prefs.getBoolean(KEY_EFFECTS, true),
            challengeLevel = prefs.getString(KEY_CHALLENGE, "Balanced") ?: "Balanced"
        )
    }

    fun saveSettings(settings: AppSettings) {
        prefs.edit()
            .putString(KEY_LANG, settings.language)
            .putBoolean(KEY_SOUND, settings.soundEnabled)
            .putBoolean(KEY_HAPTICS, settings.hapticsEnabled)
            .putBoolean(KEY_REDUCED_MOTION, settings.reducedMotion)
            .putBoolean(KEY_EFFECTS, settings.visualEffectsHigh)
            .putString(KEY_CHALLENGE, settings.challengeLevel)
            .apply()
    }

    fun loadDailyChallengeState(dateOverride: String? = null): DailyChallengeState {
        val today = dateOverride ?: getTodayDateString()
        val savedDate = prefs.getString(KEY_DAILY_DATE, "") ?: ""
        return if (savedDate == today) {
            val completed = prefs.getBoolean(KEY_DAILY_COMPLETED, false)
            val score = prefs.getInt(KEY_DAILY_SCORE, 0)
            DailyChallengeState(dateString = today, completed = completed, scoreAwarded = score)
        } else {
            // New day: reset completion state
            DailyChallengeState(dateString = today, completed = false, scoreAwarded = 0)
        }
    }

    /**
     * Completes today's daily challenge.
     * Prevents duplicate rewards on the same day.
     * Returns the actual score awarded (DAILY_CHALLENGE_REWARD on first completion, 0 if already completed).
     */
    fun completeDailyChallenge(dateOverride: String? = null): Int {
        val today = dateOverride ?: getTodayDateString()
        val savedDate = prefs.getString(KEY_DAILY_DATE, "") ?: ""
        val alreadyCompleted = savedDate == today && prefs.getBoolean(KEY_DAILY_COMPLETED, false)
        if (alreadyCompleted) {
            return 0
        }

        val editor = prefs.edit()
        editor.putString(KEY_DAILY_DATE, today)
        editor.putBoolean(KEY_DAILY_COMPLETED, true)
        editor.putInt(KEY_DAILY_SCORE, DAILY_CHALLENGE_REWARD)

        // Update streak if consecutive day
        val lastActive = prefs.getLong(KEY_LAST_ACTIVE, 0L)
        val streak = prefs.getInt(KEY_STREAK, 1)
        val oneDayMillis = 24 * 60 * 60 * 1000L
        val now = System.currentTimeMillis()
        val diff = now - lastActive
        val updatedStreak = if (diff in (oneDayMillis * 0.8).toLong()..(oneDayMillis * 2)) streak + 1 else if (diff > oneDayMillis * 2) 1 else streak

        editor.putInt(KEY_STREAK, updatedStreak)
        editor.putLong(KEY_LAST_ACTIVE, now)

        val curScore = prefs.getInt(KEY_SCORE, 0)
        val curMemScore = prefs.getInt(KEY_MEM_SCORE, 0)
        editor.putInt(KEY_SCORE, curScore + DAILY_CHALLENGE_REWARD)
        editor.putInt(KEY_MEM_SCORE, curMemScore + DAILY_CHALLENGE_REWARD)
        editor.apply()

        return DAILY_CHALLENGE_REWARD
    }

    /**
     * Synthesizes a unique formula in Formula Forge.
     * Validates formula with ChemistryEngine and canonicalizes ASCII/Unicode representation.
     * Awards points ONLY for first-time synthesis of each distinct valid compound formula.
     * Returns points awarded (UNIQUE_SYNTHESIS_REWARD if new, 0 if duplicate or invalid).
     */
    fun recordFormulaSynthesis(formula: String): Int {
        val canonical = ChemistryEngine.normalizeFormulaText(formula).trim()
        if (canonical.isEmpty()) return 0

        // Strict chemical formula validation
        if (!ChemistryEngine.isValidFormula(canonical)) {
            return 0
        }

        val currentStr = prefs.getString(KEY_SYNTHESIZED_FORMULAS, "") ?: ""
        val set = currentStr.split(",").filter { it.isNotEmpty() }.toMutableSet()
        if (set.contains(canonical)) {
            return 0 // Already synthesized; prevent unlimited score farming
        }

        set.add(canonical)
        val editor = prefs.edit()
        editor.putString(KEY_SYNTHESIZED_FORMULAS, set.joinToString(","))
        val curTotal = prefs.getInt(KEY_SCORE, 0)
        val curChem = prefs.getInt(KEY_CHEM_SCORE, 0)
        editor.putInt(KEY_SCORE, curTotal + UNIQUE_SYNTHESIS_REWARD)
        editor.putInt(KEY_CHEM_SCORE, curChem + UNIQUE_SYNTHESIS_REWARD)
        editor.apply()

        return UNIQUE_SYNTHESIS_REWARD
    }

    fun loadUserProgress(): UserProgress {
        val settings = loadSettings()
        val totalScore = prefs.getInt(KEY_SCORE, 0)
        val chemScore = prefs.getInt(KEY_CHEM_SCORE, 0)
        val memScore = prefs.getInt(KEY_MEM_SCORE, 0)
        val streak = prefs.getInt(KEY_STREAK, 1)
        val lastActive = prefs.getLong(KEY_LAST_ACTIVE, System.currentTimeMillis())
        val unlockedString = prefs.getString(KEY_UNLOCKED_WORLDS, "1") ?: "1"
        val unlockedSet = unlockedString.split(",")
            .mapNotNull { it.trim().toIntOrNull() }
            .toSet().ifEmpty { setOf(1) }

        val bookmarksString = prefs.getString(KEY_BOOKMARKS, "") ?: ""
        val bookmarksSet = bookmarksString.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toSet()

        val synthString = prefs.getString(KEY_SYNTHESIZED_FORMULAS, "") ?: ""
        val synthSet = synthString.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toSet()

        val learnedString = prefs.getString(KEY_LEARNED_CONCEPTS, "") ?: ""
        val learnedSet = learnedString.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toSet()

        // Load world progress
        val worldMap = mutableMapOf<Int, WorldProgress>()
        for (w in 1..10) {
            val isUnlocked = unlockedSet.contains(w)
            val stars = prefs.getInt("world_${w}_stars", 0)
            val score = prefs.getInt("world_${w}_score", 0)
            val lessonsString = prefs.getString("world_${w}_lessons", "") ?: ""
            val lessons = lessonsString.split(",").filter { it.isNotEmpty() }.toSet()
            val questions = prefs.getInt("world_${w}_q_count", 0)
            val correct = prefs.getInt("world_${w}_correct_count", 0)
            worldMap[w] = WorldProgress(
                worldId = w,
                isUnlocked = isUnlocked,
                stars = stars,
                score = score,
                lessonsCompleted = lessons,
                questionsAnswered = questions,
                correctAnswers = correct
            )
        }

        // Load topic mastery
        val topicKeys = listOf("atoms", "periodic", "bonding", "formula", "reactions", "moles", "ph", "solutions", "organic", "pharma")
        val masteryMap = mutableMapOf<String, TopicMastery>()
        for (k in topicKeys) {
            val correct = prefs.getInt("mastery_${k}_correct", 0)
            val mistake = prefs.getInt("mastery_${k}_mistake", 0)
            val lastTime = prefs.getLong("mastery_${k}_time", 0L)
            masteryMap[k] = TopicMastery(k, correct, mistake, lastTime)
        }

        // Load review card states
        val reviewStates = mutableMapOf<String, SpacedReviewState>()
        for (card in ChemistryUniverseData.REVIEW_ITEMS) {
            if (learnedSet.contains(card.id) || learnedSet.contains(card.conceptKey)) {
                val nextEpoch = prefs.getLong("rev_${card.id}_next", 0L)
                val interval = prefs.getInt("rev_${card.id}_interval", 1)
                val rep = prefs.getInt("rev_${card.id}_rep", 0)
                val lastRev = prefs.getLong("rev_${card.id}_last", 0L)
                val timesRev = prefs.getInt("rev_${card.id}_times", 0)
                val timesCor = prefs.getInt("rev_${card.id}_correct", 0)
                reviewStates[card.id] = SpacedReviewState(
                    conceptId = card.id,
                    nextReviewEpoch = nextEpoch,
                    intervalDays = interval,
                    repetitionCount = rep,
                    lastReviewedEpoch = lastRev,
                    timesReviewed = timesRev,
                    timesCorrect = timesCor
                )
            }
        }

        // Load level progression
        val unlockedLevelsStr = prefs.getString(KEY_UNLOCKED_LEVELS, "1") ?: "1"
        val unlockedLevelsSet = unlockedLevelsStr.split(",").filter { it.isNotEmpty() }.mapNotNull { it.toIntOrNull() }.toSet().ifEmpty { setOf(1) }
        val completedLevelsStr = prefs.getString(KEY_COMPLETED_LEVELS, "") ?: ""
        val completedLevelsSet = completedLevelsStr.split(",").filter { it.isNotEmpty() }.mapNotNull { it.toIntOrNull() }.toSet()

        val levelStarsMap = mutableMapOf<Int, Int>()
        val levelScoresMap = mutableMapOf<Int, Int>()
        for (lvl in 1..50) {
            val stars = prefs.getInt("level_${lvl}_stars", 0)
            if (stars > 0) levelStarsMap[lvl] = stars
            val sc = prefs.getInt("level_${lvl}_score", 0)
            if (sc > 0) levelScoresMap[lvl] = sc
        }

        return UserProgress(
            settings = settings,
            totalScore = totalScore,
            chemistryScore = chemScore,
            memoryScore = memScore,
            streakDays = streak,
            lastActiveEpoch = lastActive,
            unlockedWorlds = unlockedSet,
            worldProgressMap = worldMap,
            topicMasteryMap = masteryMap,
            bookmarkedReviewIds = bookmarksSet,
            dailyChallenge = loadDailyChallengeState(),
            activeMemoryThreadsCount = learnedSet.size,
            formulaForgeSynthesizedCount = synthSet.size,
            synthesizedFormulas = synthSet,
            learnedConceptIds = learnedSet,
            reviewCardStates = reviewStates,
            unlockedLevels = unlockedLevelsSet,
            completedLevels = completedLevelsSet,
            levelStars = levelStarsMap,
            levelHighScores = levelScoresMap
        )
    }

    /**
     * Records completion of an individual campaign level (1..50).
     * Enforces that first-time completion awards XP bonus and unlocks next level/world.
     * Replaying records personal best stars and score, but awards 0 duplicate completion points.
     */
    fun recordLevelCompletion(
        levelNumber: Int,
        scoreEarned: Int,
        stars: Int,
        conceptKey: String = ""
    ): Int {
        val editor = prefs.edit()
        val completedLevelsStr = prefs.getString(KEY_COMPLETED_LEVELS, "") ?: ""
        val completedSet = completedLevelsStr.split(",").filter { it.isNotEmpty() }.mapNotNull { it.toIntOrNull() }.toMutableSet()
        val isFirstTime = !completedSet.contains(levelNumber)

        var pointsAwarded = 0
        if (isFirstTime) {
            completedSet.add(levelNumber)
            editor.putString(KEY_COMPLETED_LEVELS, completedSet.joinToString(","))
            pointsAwarded = scoreEarned

            val totalScore = prefs.getInt(KEY_SCORE, 0)
            val chemScore = prefs.getInt(KEY_CHEM_SCORE, 0)
            editor.putInt(KEY_SCORE, totalScore + pointsAwarded)
            editor.putInt(KEY_CHEM_SCORE, chemScore + pointsAwarded)
        }

        // Always update best stars and score
        val prevStars = prefs.getInt("level_${levelNumber}_stars", 0)
        if (stars > prevStars) {
            editor.putInt("level_${levelNumber}_stars", stars)
        }
        val prevScore = prefs.getInt("level_${levelNumber}_score", 0)
        if (scoreEarned > prevScore) {
            editor.putInt("level_${levelNumber}_score", scoreEarned)
        }

        // Unlock next level (up to 50)
        val unlockedLevelsStr = prefs.getString(KEY_UNLOCKED_LEVELS, "1") ?: "1"
        val unlockedLevelsSet = unlockedLevelsStr.split(",").filter { it.isNotEmpty() }.mapNotNull { it.toIntOrNull() }.toMutableSet()
        if (levelNumber < 50) {
            unlockedLevelsSet.add(levelNumber + 1)
            editor.putString(KEY_UNLOCKED_LEVELS, unlockedLevelsSet.joinToString(","))
        }

        // If levelNumber is the 5th level of world W, unlock world W+1
        val worldId = ((levelNumber - 1) / 5) + 1
        if (levelNumber % 5 == 0 && worldId < 10) {
            val currentUnlockedWorlds = prefs.getString(KEY_UNLOCKED_WORLDS, "1") ?: "1"
            val unlockedWorldsSet = currentUnlockedWorlds.split(",").filter { it.isNotEmpty() }.mapNotNull { it.toIntOrNull() }.toMutableSet()
            unlockedWorldsSet.add(worldId + 1)
            editor.putString(KEY_UNLOCKED_WORLDS, unlockedWorldsSet.joinToString(","))
        }

        // Register conceptKey in learnedConceptIds for Spaced Review
        if (conceptKey.isNotEmpty()) {
            val curLearned = prefs.getString(KEY_LEARNED_CONCEPTS, "") ?: ""
            val learnedSet = curLearned.split(",").filter { it.isNotEmpty() }.toMutableSet()
            learnedSet.add(conceptKey)
            editor.putString(KEY_LEARNED_CONCEPTS, learnedSet.joinToString(","))
        }

        editor.apply()
        return pointsAwarded
    }

    fun unlockWorld(worldId: Int) {
        val current = prefs.getString(KEY_UNLOCKED_WORLDS, "1") ?: "1"
        val set = current.split(",").mapNotNull { it.trim().toIntOrNull() }.toMutableSet()
        set.add(worldId)
        prefs.edit().putString(KEY_UNLOCKED_WORLDS, set.joinToString(",")).apply()
    }

    fun addPoints(points: Int, isMemory: Boolean = false) {
        if (points <= 0) return
        val cur = prefs.getInt(KEY_SCORE, 0)
        val editor = prefs.edit()
        editor.putInt(KEY_SCORE, cur + points)
        if (isMemory) {
            val mem = prefs.getInt(KEY_MEM_SCORE, 0)
            editor.putInt(KEY_MEM_SCORE, mem + points)
        } else {
            val chem = prefs.getInt(KEY_CHEM_SCORE, 0)
            editor.putInt(KEY_CHEM_SCORE, chem + points)
        }
        editor.apply()
    }

    fun recordLessonCompletion(worldId: Int, lessonId: String, conceptIds: List<String> = emptyList()) {
        val curLessons = prefs.getString("world_${worldId}_lessons", "") ?: ""
        val set = curLessons.split(",").filter { it.isNotEmpty() }.toMutableSet()
        set.add(lessonId)

        val curLearned = prefs.getString(KEY_LEARNED_CONCEPTS, "") ?: ""
        val learnedSet = curLearned.split(",").filter { it.isNotEmpty() }.toMutableSet()
        learnedSet.addAll(conceptIds)

        // Also add world key to learned concepts
        val world = ChemistryUniverseData.WORLDS.find { it.id == worldId }
        if (world != null) {
            learnedSet.add(world.key)
        }

        prefs.edit()
            .putString("world_${worldId}_lessons", set.joinToString(","))
            .putString(KEY_LEARNED_CONCEPTS, learnedSet.joinToString(","))
            .apply()
    }

    fun recordQuizResult(worldId: Int, topicKey: String, correct: Boolean, scoreEarned: Int) {
        val editor = prefs.edit()
        val curScore = prefs.getInt("world_${worldId}_score", 0)
        editor.putInt("world_${worldId}_score", curScore + scoreEarned)

        val totalScore = prefs.getInt(KEY_SCORE, 0)
        editor.putInt(KEY_SCORE, totalScore + scoreEarned)

        val chemScore = prefs.getInt(KEY_CHEM_SCORE, 0)
        editor.putInt(KEY_CHEM_SCORE, chemScore + scoreEarned)

        val qCount = prefs.getInt("world_${worldId}_q_count", 0)
        val correctCount = prefs.getInt("world_${worldId}_correct_count", 0)
        editor.putInt("world_${worldId}_q_count", qCount + 1)
        if (correct) {
            editor.putInt("world_${worldId}_correct_count", correctCount + 1)
        }

        val curCorrect = prefs.getInt("mastery_${topicKey}_correct", 0)
        val curMistake = prefs.getInt("mastery_${topicKey}_mistake", 0)
        if (correct) {
            editor.putInt("mastery_${topicKey}_correct", curCorrect + 1)
        } else {
            editor.putInt("mastery_${topicKey}_mistake", curMistake + 1)
        }
        editor.putLong("mastery_${topicKey}_time", System.currentTimeMillis())

        val stars = if (curCorrect + (if (correct) 1 else 0) >= 4) 3 else if (curCorrect >= 2) 2 else 1
        val prevStars = prefs.getInt("world_${worldId}_stars", 0)
        if (stars > prevStars) {
            editor.putInt("world_${worldId}_stars", stars)
        }

        // Register topic in learned concepts
        val curLearned = prefs.getString(KEY_LEARNED_CONCEPTS, "") ?: ""
        val learnedSet = curLearned.split(",").filter { it.isNotEmpty() }.toMutableSet()
        learnedSet.add(topicKey)
        editor.putString(KEY_LEARNED_CONCEPTS, learnedSet.joinToString(","))

        // Check structured unlocking milestone: World W unlocks W+1 if at least 2 questions answered correctly
        val updatedCorrect = correctCount + (if (correct) 1 else 0)
        if (updatedCorrect >= 2 && worldId < 10) {
            val currentUnlocked = prefs.getString(KEY_UNLOCKED_WORLDS, "1") ?: "1"
            val unlockedSet = currentUnlocked.split(",").mapNotNull { it.trim().toIntOrNull() }.toMutableSet()
            unlockedSet.add(worldId + 1)
            editor.putString(KEY_UNLOCKED_WORLDS, unlockedSet.joinToString(","))
        }

        editor.apply()
    }

    /**
     * Records a spaced repetition recall outcome for a card.
     */
    fun recordReviewResult(cardId: String, wasCorrect: Boolean) {
        val now = System.currentTimeMillis()
        val curRep = prefs.getInt("rev_${cardId}_rep", 0)
        val curInterval = prefs.getInt("rev_${cardId}_interval", 1)
        val timesRev = prefs.getInt("rev_${cardId}_times", 0)
        val timesCor = prefs.getInt("rev_${cardId}_correct", 0)

        val nextCard = AdaptiveEngine.calculateNextReview(
            card = ReviewCard(
                id = cardId,
                conceptKey = cardId,
                worldId = 1,
                titleEn = "",
                titleBn = "",
                formula = "",
                summaryEn = "",
                summaryBn = "",
                nextReviewEpoch = 0L,
                intervalDays = curInterval,
                repetitionCount = curRep
            ),
            wasCorrect = wasCorrect,
            nowEpoch = now
        )

        prefs.edit()
            .putLong("rev_${cardId}_next", nextCard.nextReviewEpoch)
            .putInt("rev_${cardId}_interval", nextCard.intervalDays)
            .putInt("rev_${cardId}_rep", nextCard.repetitionCount)
            .putLong("rev_${cardId}_last", now)
            .putInt("rev_${cardId}_times", timesRev + 1)
            .putInt("rev_${cardId}_correct", timesCor + (if (wasCorrect) 1 else 0))
            .apply()
    }

    fun toggleBookmark(reviewId: String): Boolean {
        val current = prefs.getString(KEY_BOOKMARKS, "") ?: ""
        val set = current.split(",").filter { it.isNotEmpty() }.toMutableSet()
        val isNowBookmarked: Boolean
        if (set.contains(reviewId)) {
            set.remove(reviewId)
            isNowBookmarked = false
        } else {
            set.add(reviewId)
            isNowBookmarked = true
        }
        prefs.edit().putString(KEY_BOOKMARKS, set.joinToString(",")).apply()
        return isNowBookmarked
    }

    fun resetProgress() {
        prefs.edit().clear().apply()
    }
}
