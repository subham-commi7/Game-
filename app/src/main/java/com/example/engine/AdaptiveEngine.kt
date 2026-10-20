package com.example.engine

import com.example.model.ReviewCard
import com.example.model.TopicMastery
import java.util.concurrent.TimeUnit

object AdaptiveEngine {

    val INTERVAL_DAYS_SEQUENCE = listOf(1, 3, 7, 14, 30)

    /**
     * Calculates the next review date based on user recall result.
     * If remembered correctly: interval advances.
     * If forgotten/mistake: interval resets to 1 day.
     */
    fun calculateNextReview(
        card: ReviewCard,
        wasCorrect: Boolean,
        nowEpoch: Long = System.currentTimeMillis()
    ): ReviewCard {
        return if (wasCorrect) {
            val nextRep = card.repetitionCount + 1
            val nextIndex = (nextRep - 1).coerceAtMost(INTERVAL_DAYS_SEQUENCE.lastIndex)
            val nextIntervalDays = INTERVAL_DAYS_SEQUENCE[nextIndex]
            val nextEpoch = nowEpoch + TimeUnit.DAYS.toMillis(nextIntervalDays.toLong())
            card.copy(
                nextReviewEpoch = nextEpoch,
                intervalDays = nextIntervalDays,
                repetitionCount = nextRep
            )
        } else {
            val nextEpoch = nowEpoch + TimeUnit.HOURS.toMillis(4) // Review again soon
            card.copy(
                nextReviewEpoch = nextEpoch,
                intervalDays = 1,
                repetitionCount = 0
            )
        }
    }

    /**
     * Filters cards that are due for spaced review.
     */
    fun getDueCards(cards: List<ReviewCard>, nowEpoch: Long = System.currentTimeMillis()): List<ReviewCard> {
        return cards.filter { it.nextReviewEpoch <= nowEpoch }
    }

    /**
     * Adjusts difficulty parameters based on recent performance.
     */
    fun evaluateDifficulty(mastery: TopicMastery, preferredLevel: String): String {
        if (mastery.totalAttempts < 3) return preferredLevel
        val accuracy = mastery.accuracyPercentage
        return when {
            accuracy >= 85 && preferredLevel != "Relaxed" -> "Challenging"
            accuracy <= 50 -> "Relaxed"
            else -> "Balanced"
        }
    }
}
