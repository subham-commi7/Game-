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
}
