package com.example

import com.example.engine.AdaptiveEngine
import com.example.model.ReviewCard
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.TimeUnit

class SpacedReviewTest {

    @Test
    fun testInitialReviewCorrectAdvancesInterval() {
        val now = 1000000000L
        val initialCard = ReviewCard(
            id = "test_z",
            conceptKey = "atomic_number",
            worldId = 1,
            titleEn = "Atomic Number (Z)",
            titleBn = "পারমাণবিক সংখ্যা (Z)",
            formula = "Z = p⁺",
            summaryEn = "Number of protons",
            summaryBn = "প্রোটন সংখ্যা",
            nextReviewEpoch = 0L,
            intervalDays = 1,
            repetitionCount = 0
        )

        // First successful recall: should advance repetition to 1, interval to 1 day
        val cardAfter1st = AdaptiveEngine.calculateNextReview(initialCard, wasCorrect = true, nowEpoch = now)
        assertEquals(1, cardAfter1st.repetitionCount)
        assertEquals(1, cardAfter1st.intervalDays)
        assertEquals(now + TimeUnit.DAYS.toMillis(1), cardAfter1st.nextReviewEpoch)

        // Second successful recall: repetition to 2, interval to 3 days
        val cardAfter2nd = AdaptiveEngine.calculateNextReview(cardAfter1st, wasCorrect = true, nowEpoch = cardAfter1st.nextReviewEpoch)
        assertEquals(2, cardAfter2nd.repetitionCount)
        assertEquals(3, cardAfter2nd.intervalDays)
        assertEquals(cardAfter1st.nextReviewEpoch + TimeUnit.DAYS.toMillis(3), cardAfter2nd.nextReviewEpoch)

        // Third successful recall: repetition to 3, interval to 7 days
        val cardAfter3rd = AdaptiveEngine.calculateNextReview(cardAfter2nd, wasCorrect = true, nowEpoch = cardAfter2nd.nextReviewEpoch)
        assertEquals(3, cardAfter3rd.repetitionCount)
        assertEquals(7, cardAfter3rd.intervalDays)

        // Fourth successful recall: repetition to 4, interval to 14 days
        val cardAfter4th = AdaptiveEngine.calculateNextReview(cardAfter3rd, wasCorrect = true, nowEpoch = cardAfter3rd.nextReviewEpoch)
        assertEquals(4, cardAfter4th.repetitionCount)
        assertEquals(14, cardAfter4th.intervalDays)

        // Fifth successful recall: repetition to 5, interval to 30 days
        val cardAfter5th = AdaptiveEngine.calculateNextReview(cardAfter4th, wasCorrect = true, nowEpoch = cardAfter4th.nextReviewEpoch)
        assertEquals(5, cardAfter5th.repetitionCount)
        assertEquals(30, cardAfter5th.intervalDays)
    }

    @Test
    fun testIncorrectRecallResetsInterval() {
        val now = 2000000000L
        val advancedCard = ReviewCard(
            id = "test_mole",
            conceptKey = "mole_formula",
            worldId = 6,
            titleEn = "Mole",
            titleBn = "মোল",
            formula = "n = m / M",
            summaryEn = "Moles formula",
            summaryBn = "মোলের সূত্র",
            nextReviewEpoch = now,
            intervalDays = 14,
            repetitionCount = 4
        )

        // Mistake made: interval resets to 1, repetition resets to 0, review scheduled soon
        val resetCard = AdaptiveEngine.calculateNextReview(advancedCard, wasCorrect = false, nowEpoch = now)
        assertEquals(0, resetCard.repetitionCount)
        assertEquals(1, resetCard.intervalDays)
        assertEquals(now + TimeUnit.HOURS.toMillis(4), resetCard.nextReviewEpoch)
    }

    @Test
    fun testGetDueCardsFiltering() {
        val now = 5000000L
        val dueCard = ReviewCard("c1", "k1", 1, "", "", "", "", "", nextReviewEpoch = now - 1000, intervalDays = 1, repetitionCount = 1)
        val futureCard = ReviewCard("c2", "k2", 1, "", "", "", "", "", nextReviewEpoch = now + 50000, intervalDays = 3, repetitionCount = 2)

        val dueList = AdaptiveEngine.getDueCards(listOf(dueCard, futureCard), nowEpoch = now)
        assertEquals(1, dueList.size)
        assertEquals("c1", dueList[0].id)
    }
}
