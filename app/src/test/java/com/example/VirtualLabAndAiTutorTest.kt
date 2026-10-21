package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.engine.ChemistryEngine
import com.example.engine.FirebaseAiService
import com.example.engine.StorageManager
import com.example.engine.TutorContext
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class VirtualLabAndAiTutorTest {

    private lateinit var context: Context
    private lateinit var storage: StorageManager
    private lateinit var aiService: FirebaseAiService

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext<Context>()
        storage = StorageManager(context)
        storage.resetProgress()
        aiService = FirebaseAiService(context)
    }

    // =========================================================================
    // PRIORITY 1: CAMPAIGN LEVEL PROGRESS, UNLOCKING & RESTORATION TESTS
    // =========================================================================

    @Test
    fun testLevelCompletionPersistenceAndEarliestUnfinishedCalculation() {
        // Initial state: Level 1 is unlocked, earliest unfinished must be Level 1
        assertEquals(1, storage.getEarliestUnfinishedLevel())

        // Complete Level 1 with 3 stars and 100 score
        val pts1 = storage.recordLevelCompletion(levelNumber = 1, scoreEarned = 100, stars = 3, conceptKey = "nuclide_symbols")
        assertEquals(100, pts1)

        // Now Level 1 is completed, Level 2 is unlocked, earliest unfinished is Level 2
        val progress1 = storage.loadUserProgress()
        assertTrue(progress1.completedLevels.contains(1))
        assertTrue(progress1.unlockedLevels.contains(2))
        assertEquals(3, progress1.levelStars[1])
        assertEquals(100, progress1.levelHighScores[1])
        assertEquals(2, storage.getEarliestUnfinishedLevel())

        // Replay Level 1: Must award 0 XP to prevent farming
        val replayPts = storage.recordLevelCompletion(levelNumber = 1, scoreEarned = 100, stars = 3, conceptKey = "nuclide_symbols")
        assertEquals(0, replayPts)

        // Complete Level 2 and Level 3
        storage.recordLevelCompletion(levelNumber = 2, scoreEarned = 100, stars = 2)
        storage.recordLevelCompletion(levelNumber = 3, scoreEarned = 100, stars = 3)
        assertEquals(4, storage.getEarliestUnfinishedLevel())

        // Simulate App Restart by creating fresh StorageManager instance
        val freshStorage = StorageManager(context)
        val restoredProgress = freshStorage.loadUserProgress()
        assertTrue(restoredProgress.completedLevels.containsAll(listOf(1, 2, 3)))
        assertTrue(restoredProgress.unlockedLevels.containsAll(listOf(1, 2, 3, 4)))
        assertEquals(4, freshStorage.getEarliestUnfinishedLevel())
        assertEquals(300, restoredProgress.totalScore)
    }

    @Test
    fun testWorldCompletionUnlocksNextWorld() {
        // Complete Levels 1 through 5 (World 1)
        for (lvl in 1..4) {
            storage.recordLevelCompletion(levelNumber = lvl, scoreEarned = 100, stars = 3)
        }
        val bossReward = storage.recordLevelCompletion(levelNumber = 5, scoreEarned = 200, stars = 3)
        assertEquals(200, bossReward)

        val progress = storage.loadUserProgress()
        assertTrue("World 2 should be unlocked after completing Level 5", progress.unlockedWorlds.contains(2))
        assertTrue("Level 6 should be unlocked", progress.unlockedLevels.contains(6))
        assertEquals(6, storage.getEarliestUnfinishedLevel())
    }

    // =========================================================================
    // PRIORITY 2: AI CHEMISTRY TUTOR PEDAGOGICAL COVERAGE & MULTILINGUAL TESTS
    // =========================================================================

    @Test
    fun testAiTutorFreeFormSubatomicQuestionEnglish() {
        val tc = TutorContext(
            language = "en",
            levelNumber = 1,
            levelTitle = "Nuclide Notation",
            objective = "Determine atomic number, mass number, and net charge",
            keyRule = "A = Z + N and Net Charge = p - e"
        )

        val response = aiService.generateOfflineVerifiedResponse(
            tc = tc,
            rawQuery = "How do I calculate the number of neutrons and net charge?"
        )

        assertNotNull(response)
        assertTrue(response.contains("Atomic Structure"))
        assertTrue(response.contains("Direct Answer"))
        assertTrue(response.contains("N = A - Z"))
        assertTrue(response.contains("Common Misconception"))
        assertTrue(response.contains("Practice Question"))
    }

    @Test
    fun testAiTutorFreeFormSubatomicQuestionBengali() {
        val tc = TutorContext(
            language = "bn",
            levelNumber = 1,
            levelTitle = "নিউক্লাইড নোটেশন",
            objective = "প্রোটন, নিউট্রন ও ইলেকট্রন সংখ্যা নির্ণয়",
            keyRule = "A = Z + N"
        )

        val response = aiService.generateOfflineVerifiedResponse(
            tc = tc,
            rawQuery = "পরমাণুতে নিউট্রন এবং চার্জ কীভাবে বের করব?"
        )

        assertNotNull(response)
        assertTrue(response.contains("পরমাণুর গঠন"))
        assertTrue(response.contains("সরাসরি উত্তর"))
        assertTrue(response.contains("A = Z + N"))
        assertTrue(response.contains("সাধারণ ভুল ধারণা"))
    }

    @Test
    fun testAiTutorPeriodicTrendsQuestion() {
        val tc = TutorContext(language = "en", levelNumber = 6, levelTitle = "Periodic Trends")
        val response = aiService.generateOfflineVerifiedResponse(
            tc = tc,
            rawQuery = "Why does electronegativity increase across a period?"
        )

        assertTrue(response.contains("Periodic"))
        assertTrue(response.contains("Fluorine"))
        assertTrue(response.contains("Direct Answer"))
    }

    @Test
    fun testAiTutorChemicalBondingQuestion() {
        val tc = TutorContext(language = "en", levelNumber = 11, levelTitle = "Ionic Bonding")
        val response = aiService.generateOfflineVerifiedResponse(
            tc = tc,
            rawQuery = "What is the difference between ionic and covalent bonding?"
        )

        assertTrue(response.contains("Bonding"))
        assertTrue(response.contains("transfer"))
        assertTrue(response.contains("Octet Rule"))
    }

    @Test
    fun testAiTutorEquationBalancingQuestion() {
        val tc = TutorContext(language = "en", levelNumber = 21, levelTitle = "Reaction Reactor")
        val response = aiService.generateOfflineVerifiedResponse(
            tc = tc,
            rawQuery = "How to balance chemical equations correctly?"
        )

        assertTrue(response.contains("Balancing Chemical Equations"))
        assertTrue(response.contains("Coefficients"))
        assertTrue(response.contains("Conservation of Mass"))
    }

    @Test
    fun testAiTutorAcidsAndPhQuestion() {
        val tc = TutorContext(language = "bn", levelNumber = 31, levelTitle = "pH Dimension")
        val response = aiService.generateOfflineVerifiedResponse(
            tc = tc,
            rawQuery = "pH স্কেল এবং এসিড ক্ষারের সম্পর্ক ব্যাখ্যা করুন"
        )

        assertTrue(response.contains("অম্ল, ক্ষার ও পিএইচ স্কেল"))
        assertTrue(response.contains("pH = -log₁₀[H⁺]"))
    }

    @Test
    fun testAiTutorMolarityAndDilutionQuestion() {
        val tc = TutorContext(language = "en", levelNumber = 36, levelTitle = "Solution Laboratory")
        val response = aiService.generateOfflineVerifiedResponse(
            tc = tc,
            rawQuery = "How to calculate molarity and prepare a dilution?"
        )

        assertTrue(response.contains("Molarity"))
        assertTrue(response.contains("M1 V1 = M2 V2"))
    }

    @Test
    fun testAiTutorOrganicFunctionalGroupsQuestion() {
        val tc = TutorContext(language = "en", levelNumber = 41, levelTitle = "Functional Groups")
        val response = aiService.generateOfflineVerifiedResponse(
            tc = tc,
            rawQuery = "What are the common organic functional groups?"
        )

        assertTrue(response.contains("Functional Groups"))
        assertTrue(response.contains("Hydroxyl"))
        assertTrue(response.contains("Carboxyl"))
    }

    // =========================================================================
    // PRIORITY 3: VIRTUAL LAB REACTIONS & CHEMICAL CATALOG INTEGRITY
    // =========================================================================

    @Test
    fun testVirtualLabChemicalCatalogHasValidData() {
        assertTrue("Catalog must have at least 10 chemicals", ChemistryEngine.LAB_CHEMICALS.size >= 10)
        for (chem in ChemistryEngine.LAB_CHEMICALS) {
            assertTrue("Formula should not be empty", chem.formula.isNotBlank())
            assertTrue("Molar mass must be positive for ${chem.formula}", chem.molarMass > 0.0)
            assertTrue("Safety note must be provided for ${chem.formula}", chem.safetyNote.isNotBlank())
        }
    }

    @Test
    fun testVirtualLabCuratedReactionsIntegrity() {
        assertTrue("Must have at least 8 curated reactions", ChemistryEngine.CURATED_REACTIONS.size >= 8)
        for (rx in ChemistryEngine.CURATED_REACTIONS) {
            assertTrue("Equation must contain ->", rx.equation.contains("->") || rx.equation.contains("⇌"))
            assertTrue("Stoich coeffs must be positive", rx.stoichReactant1 > 0 && rx.stoichReactant2 > 0)

            // Test stoichiometry computation for each reaction
            val outcome = ChemistryEngine.calculateReactionStoichiometry(rx, 10.0, 10.0)
            assertTrue(outcome.theoreticalYieldMoles > 0.0)
            assertTrue(outcome.theoreticalYieldGrams > 0.0)
            assertTrue(outcome.atomConservationProof.isNotBlank())
        }
    }
}
