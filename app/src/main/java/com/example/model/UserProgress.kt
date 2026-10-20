package com.example.model

data class AppSettings(
    val language: String = "bn", // Default Bengali
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val reducedMotion: Boolean = false,
    val visualEffectsHigh: Boolean = true,
    val challengeLevel: String = "Balanced" // "Relaxed", "Balanced", "Challenging"
)

data class WorldProgress(
    val worldId: Int,
    val isUnlocked: Boolean,
    val stars: Int = 0,
    val score: Int = 0,
    val lessonsCompleted: Set<String> = emptySet(),
    val questionsAnswered: Int = 0,
    val correctAnswers: Int = 0
)

data class TopicMastery(
    val topicKey: String,
    val correctCount: Int = 0,
    val mistakeCount: Int = 0,
    val lastTrainedEpoch: Long = 0L
) {
    val totalAttempts: Int get() = correctCount + mistakeCount
    val accuracyPercentage: Int get() = if (totalAttempts > 0) ((correctCount.toDouble() / totalAttempts) * 100).toInt() else 0
}

data class DailyChallengeState(
    val dateString: String = "", // Format: "YYYY-MM-DD"
    val completed: Boolean = false,
    val scoreAwarded: Int = 0
)

data class SpacedReviewState(
    val conceptId: String,
    val nextReviewEpoch: Long,
    val intervalDays: Int,
    val repetitionCount: Int,
    val lastReviewedEpoch: Long = 0L,
    val timesReviewed: Int = 0,
    val timesCorrect: Int = 0
)

data class UserProgress(
    val settings: AppSettings = AppSettings(),
    val totalScore: Int = 0,
    val chemistryScore: Int = 0,
    val memoryScore: Int = 0,
    val streakDays: Int = 1,
    val lastActiveEpoch: Long = System.currentTimeMillis(),
    val unlockedWorlds: Set<Int> = setOf(1), // World 1 unlocked by default
    val worldProgressMap: Map<Int, WorldProgress> = emptyMap(),
    val topicMasteryMap: Map<String, TopicMastery> = emptyMap(),
    val bookmarkedReviewIds: Set<String> = emptySet(),
    val dailyChallenge: DailyChallengeState = DailyChallengeState(),
    val activeMemoryThreadsCount: Int = 0,
    val formulaForgeSynthesizedCount: Int = 0,
    val synthesizedFormulas: Set<String> = emptySet(),
    val learnedConceptIds: Set<String> = emptySet(),
    val reviewCardStates: Map<String, SpacedReviewState> = emptyMap(),
    val unlockedLevels: Set<Int> = setOf(1), // Level 1 unlocked by default
    val completedLevels: Set<Int> = emptySet(),
    val levelStars: Map<Int, Int> = emptyMap(), // Level Number -> Stars (1..3)
    val levelHighScores: Map<Int, Int> = emptyMap() // Level Number -> Best Score
)
