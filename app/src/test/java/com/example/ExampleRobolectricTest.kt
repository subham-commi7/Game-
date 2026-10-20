package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.engine.StorageManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var context: Context
    private lateinit var storage: StorageManager

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext<Context>()
        storage = StorageManager(context)
        storage.resetProgress()
    }

    @Test
    fun testAppStringFromContext() {
        val appName = context.getString(R.string.app_name)
        assertEquals("NeuroVault", appName)
    }

    @Test
    fun testStorageManagerDefaults() {
        val progress = storage.loadUserProgress()
        assertNotNull(progress)
        assertEquals("bn", progress.settings.language)
        assertEquals(0, progress.totalScore)
        assertEquals(1, progress.unlockedWorlds.size)
        assertTrue(progress.unlockedWorlds.contains(1))
    }

    @Test
    fun testDailyChallengeDuplicateRewardPrevention() {
        // First completion awards 250 points
        val firstReward = storage.completeDailyChallenge()
        assertEquals(250, firstReward)

        val progressAfterFirst = storage.loadUserProgress()
        assertEquals(250, progressAfterFirst.totalScore)
        assertTrue(progressAfterFirst.dailyChallenge.completed)

        // Attempting second completion on same date must return 0 points
        val secondReward = storage.completeDailyChallenge()
        assertEquals(0, secondReward)

        val progressAfterSecond = storage.loadUserProgress()
        assertEquals(250, progressAfterSecond.totalScore) // Score did not increase!
    }

    @Test
    fun testDailyChallengeMultiDayLifecycle() {
        val day1 = "2026-10-20"
        val day2 = "2026-10-21"

        // Day 1: First completion awards 250 points
        val day1Reward = storage.completeDailyChallenge(dateOverride = day1)
        assertEquals(250, day1Reward)

        // Day 1: Repeated completion returns 0
        val day1Repeat = storage.completeDailyChallenge(dateOverride = day1)
        assertEquals(0, day1Repeat)

        // Restored state verification
        val day1State = storage.loadDailyChallengeState(dateOverride = day1)
        assertTrue(day1State.completed)
        assertEquals(250, day1State.scoreAwarded)
        assertEquals(day1, day1State.dateString)

        // Day 2 arrival: state resets completion for new date
        val day2InitialState = storage.loadDailyChallengeState(dateOverride = day2)
        assertFalse(day2InitialState.completed)
        assertEquals(0, day2InitialState.scoreAwarded)
        assertEquals(day2, day2InitialState.dateString)

        // Day 2: First completion awards 250 points and increments total score
        val day2Reward = storage.completeDailyChallenge(dateOverride = day2)
        assertEquals(250, day2Reward)

        val progressDay2 = storage.loadUserProgress()
        assertEquals(500, progressDay2.totalScore)

        // Day 2: Repeated completion returns 0
        val day2Repeat = storage.completeDailyChallenge(dateOverride = day2)
        assertEquals(0, day2Repeat)
    }

    @Test
    fun testFormulaForgeUniqueSynthesisReward() {
        // First synthesis of NaCl awards 50 points
        val reward1 = storage.recordFormulaSynthesis("NaCl")
        assertEquals(50, reward1)

        val p1 = storage.loadUserProgress()
        assertEquals(50, p1.totalScore)
        assertTrue(p1.synthesizedFormulas.contains("NaCl"))

        // Duplicate synthesis of NaCl awards 0 points
        val reward2 = storage.recordFormulaSynthesis("NaCl")
        assertEquals(0, reward2)
        val p2 = storage.loadUserProgress()
        assertEquals(50, p2.totalScore)

        // Synthesis of new compound MgCl2 awards 50 points
        val reward3 = storage.recordFormulaSynthesis("MgCl2")
        assertEquals(50, reward3)
        val p3 = storage.loadUserProgress()
        assertEquals(100, p3.totalScore)
        assertEquals(2, p3.synthesizedFormulas.size)
    }

    @Test
    fun testFormulaForgeValidationAndCanonicalDuplicates() {
        // Unicode vs ASCII equivalence: "MgCl₂" vs "MgCl2"
        val rUnicode = storage.recordFormulaSynthesis("MgCl₂")
        assertEquals(50, rUnicode)

        // Same compound in standard ASCII digits must be rejected as duplicate
        val rAsciiDuplicate = storage.recordFormulaSynthesis("MgCl2")
        assertEquals(0, rAsciiDuplicate)

        // Invalid chemical formula with illegal character '?'
        val rInvalidChar = storage.recordFormulaSynthesis("H2O?")
        assertEquals(0, rInvalidChar)

        // Invalid formula with unknown chemical element symbol 'Xx'
        val rUnknownElement = storage.recordFormulaSynthesis("Xx2O")
        assertEquals(0, rUnknownElement)

        // Invalid formula with unbalanced parenthesis
        val rUnbalanced = storage.recordFormulaSynthesis("Ca(OH2")
        assertEquals(0, rUnbalanced)

        // Invalid formula with zero atom count
        val rZeroCount = storage.recordFormulaSynthesis("Na0Cl")
        assertEquals(0, rZeroCount)

        // Empty and whitespace formulas
        assertEquals(0, storage.recordFormulaSynthesis(""))
        assertEquals(0, storage.recordFormulaSynthesis("   "))

        // Total score only increased for the 1 valid unique synthesis (MgCl2 = 50 pts)
        val p = storage.loadUserProgress()
        assertEquals(50, p.totalScore)
        assertEquals(1, p.synthesizedFormulas.size)
    }

    @Test
    fun testSpacedReviewStatePersistence() {
        storage.recordLessonCompletion(worldId = 1, lessonId = "w1_lesson", conceptIds = listOf("rev_z", "rev_a"))
        val pBefore = storage.loadUserProgress()
        assertTrue(pBefore.learnedConceptIds.contains("rev_z"))

        // Record recall success for rev_z
        storage.recordReviewResult(cardId = "rev_z", wasCorrect = true)
        val pAfter = storage.loadUserProgress()
        val reviewState = pAfter.reviewCardStates["rev_z"]
        assertNotNull(reviewState)
        assertEquals(1, reviewState!!.repetitionCount)
        assertEquals(1, reviewState.timesReviewed)
        assertEquals(1, reviewState.timesCorrect)
    }

    @Test
    fun testSpacedReviewLifecycleAndIndependentBookmarks() {
        // Concept not yet encountered: not in learnedConceptIds
        val initialProgress = storage.loadUserProgress()
        assertFalse(initialProgress.learnedConceptIds.contains("rev_mole"))
        assertNull(initialProgress.reviewCardStates["rev_mole"])

        // Complete lesson for World 6 -> unlocks rev_mole
        storage.recordLessonCompletion(worldId = 6, lessonId = "w6_lesson", conceptIds = listOf("rev_mole", "mole_formula"))
        val progressAfterLesson = storage.loadUserProgress()
        assertTrue(progressAfterLesson.learnedConceptIds.contains("rev_mole"))

        // First successful recall
        storage.recordReviewResult("rev_mole", wasCorrect = true)
        val pAfter1st = storage.loadUserProgress()
        val s1 = pAfter1st.reviewCardStates["rev_mole"]
        assertNotNull(s1)
        assertEquals(1, s1!!.repetitionCount)
        assertEquals(1, s1.intervalDays)
        assertTrue(s1.nextReviewEpoch > System.currentTimeMillis())

        // Failed recall resets repetition
        storage.recordReviewResult("rev_mole", wasCorrect = false)
        val pAfterFail = storage.loadUserProgress()
        val sFail = pAfterFail.reviewCardStates["rev_mole"]
        assertNotNull(sFail)
        assertEquals(0, sFail!!.repetitionCount)
        assertEquals(1, sFail.intervalDays)
        assertEquals(2, sFail.timesReviewed)
        assertEquals(1, sFail.timesCorrect)

        // Bookmarks are strictly independent from review scheduling
        assertFalse(pAfterFail.bookmarkedReviewIds.contains("rev_mole"))
        val isNowBookmarked = storage.toggleBookmark("rev_mole")
        assertTrue(isNowBookmarked)

        val pWithBookmark = storage.loadUserProgress()
        assertTrue(pWithBookmark.bookmarkedReviewIds.contains("rev_mole"))
        // Review state was NOT altered by bookmarking
        val sStillSame = pWithBookmark.reviewCardStates["rev_mole"]
        assertEquals(sFail.repetitionCount, sStillSame!!.repetitionCount)
        assertEquals(sFail.intervalDays, sStillSame.intervalDays)

        // Toggle bookmark off
        val isBookmarkedOff = storage.toggleBookmark("rev_mole")
        assertFalse(isBookmarkedOff)
        assertFalse(storage.loadUserProgress().bookmarkedReviewIds.contains("rev_mole"))
    }

    @Test
    fun testSettingsPersistence() {
        val initialSettings = storage.loadSettings()
        assertEquals("bn", initialSettings.language)
        assertTrue(initialSettings.soundEnabled)
        assertTrue(initialSettings.hapticsEnabled)
        assertFalse(initialSettings.reducedMotion)
        assertTrue(initialSettings.visualEffectsHigh)
        assertEquals("Balanced", initialSettings.challengeLevel)

        val newSettings = initialSettings.copy(
            language = "en",
            soundEnabled = false,
            hapticsEnabled = false,
            reducedMotion = true,
            visualEffectsHigh = false,
            challengeLevel = "Challenging"
        )
        storage.saveSettings(newSettings)

        val reloaded = storage.loadSettings()
        assertEquals("en", reloaded.language)
        assertFalse(reloaded.soundEnabled)
        assertFalse(reloaded.hapticsEnabled)
        assertTrue(reloaded.reducedMotion)
        assertFalse(reloaded.visualEffectsHigh)
        assertEquals("Challenging", reloaded.challengeLevel)
    }
}
