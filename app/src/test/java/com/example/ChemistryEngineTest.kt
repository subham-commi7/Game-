package com.example

import com.example.engine.ChemistryEngine
import org.junit.Assert.*
import org.junit.Test

class ChemistryEngineTest {

    // =========================================================
    // TASK 2.1: VALID FORMULA TESTS
    // =========================================================

    @Test
    fun testValidFormulas() {
        // 1. Sodium chloride: NaCl
        val nacl = ChemistryEngine.parseAtomCounts("NaCl")
        assertEquals(mapOf("Na" to 1, "Cl" to 1), nacl)

        // 2. Magnesium chloride: MgCl2 and MgCl₂
        val mgcl2 = ChemistryEngine.parseAtomCounts("MgCl2")
        assertEquals(mapOf("Mg" to 1, "Cl" to 2), mgcl2)
        val mgcl2Unicode = ChemistryEngine.parseAtomCounts("MgCl₂")
        assertEquals(mapOf("Mg" to 1, "Cl" to 2), mgcl2Unicode)

        // 3. Calcium carbonate: CaCO3 and CaCO₃
        val caco3 = ChemistryEngine.parseAtomCounts("CaCO3")
        assertEquals(mapOf("Ca" to 1, "C" to 1, "O" to 3), caco3)
        val caco3Unicode = ChemistryEngine.parseAtomCounts("CaCO₃")
        assertEquals(mapOf("Ca" to 1, "C" to 1, "O" to 3), caco3Unicode)

        // 4. Aluminium sulfate: Al2(SO4)3 and Al₂(SO₄)₃
        val al2so43 = ChemistryEngine.parseAtomCounts("Al2(SO4)3")
        assertEquals(mapOf("Al" to 2, "S" to 3, "O" to 12), al2so43)
        val al2so43Unicode = ChemistryEngine.parseAtomCounts("Al₂(SO₄)₃")
        assertEquals(mapOf("Al" to 2, "S" to 3, "O" to 12), al2so43Unicode)

        // 5. Ammonium chloride: NH4Cl and NH₄Cl
        val nh4cl = ChemistryEngine.parseAtomCounts("NH4Cl")
        assertEquals(mapOf("N" to 1, "H" to 4, "Cl" to 1), nh4cl)
        val nh4clUnicode = ChemistryEngine.parseAtomCounts("NH₄Cl")
        assertEquals(mapOf("N" to 1, "H" to 4, "Cl" to 1), nh4clUnicode)

        // 6. Sodium sulfate: Na2SO4 and Na₂SO₄
        val na2so4 = ChemistryEngine.parseAtomCounts("Na2SO4")
        assertEquals(mapOf("Na" to 2, "S" to 1, "O" to 4), na2so4)
        val na2so4Unicode = ChemistryEngine.parseAtomCounts("Na₂SO₄")
        assertEquals(mapOf("Na" to 2, "S" to 1, "O" to 4), na2so4Unicode)

        // 7. Calcium hydroxide: Ca(OH)2 and Ca(OH)₂
        val caoh2 = ChemistryEngine.parseAtomCounts("Ca(OH)2")
        assertEquals(mapOf("Ca" to 1, "O" to 2, "H" to 2), caoh2)
        val caoh2Unicode = ChemistryEngine.parseAtomCounts("Ca(OH)₂")
        assertEquals(mapOf("Ca" to 1, "O" to 2, "H" to 2), caoh2Unicode)

        // Water: H2O and H₂O
        val h2o = ChemistryEngine.parseAtomCounts("H2O")
        assertEquals(mapOf("H" to 2, "O" to 1), h2o)
        val h2oUnicode = ChemistryEngine.parseAtomCounts("H₂O")
        assertEquals(mapOf("H" to 2, "O" to 1), h2oUnicode)
    }

    @Test
    fun testFormatIonicFormulaGeneration() {
        assertEquals("NaCl", ChemistryEngine.formatIonicFormula("Na", +1, false, "Cl", -1, false))
        assertEquals("MgCl₂", ChemistryEngine.formatIonicFormula("Mg", +2, false, "Cl", -1, false))
        assertEquals("CaCO₃", ChemistryEngine.formatIonicFormula("Ca", +2, false, "CO₃", -2, true))
        assertEquals("Al₂(SO₄)₃", ChemistryEngine.formatIonicFormula("Al", +3, false, "SO₄", -2, true))
        assertEquals("NH₄Cl", ChemistryEngine.formatIonicFormula("NH₄", +1, true, "Cl", -1, false))
        assertEquals("Na₂SO₄", ChemistryEngine.formatIonicFormula("Na", +1, false, "SO₄", -2, true))
    }

    // =========================================================
    // TASK 2.2: INVALID FORMULA TESTS
    // =========================================================

    @Test(expected = IllegalArgumentException::class)
    fun testEmptyFormulaThrows() {
        ChemistryEngine.parseAtomCounts("")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testWhitespaceFormulaThrows() {
        ChemistryEngine.parseAtomCounts("   ")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUnmatchedOpeningParenthesisThrows() {
        ChemistryEngine.parseAtomCounts("Ca(OH2")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUnmatchedClosingParenthesisThrows() {
        ChemistryEngine.parseAtomCounts("H2O)")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testReversedParenthesesThrows() {
        ChemistryEngine.parseAtomCounts("Ca)OH(2")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUnrecognizedCharactersThrows() {
        ChemistryEngine.parseAtomCounts("H2O?")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testZeroAtomCountThrows() {
        ChemistryEngine.parseAtomCounts("H0")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testZeroSubscriptInsideFormulaThrows() {
        ChemistryEngine.parseAtomCounts("Na0Cl")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testNegativeSubscriptThrows() {
        ChemistryEngine.parseAtomCounts("H-2O")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUnknownElementSymbolThrows() {
        ChemistryEngine.parseAtomCounts("Xx2O")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEmptyGroupThrows() {
        ChemistryEngine.parseAtomCounts("Ca()2")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testLeadingDigitThrows() {
        ChemistryEngine.parseAtomCounts("2H2O")
    }

    // =========================================================
    // TASK 2.3: REACTION BALANCING TESTS
    // =========================================================

    @Test
    fun testBalancedWaterFormation() {
        val result = ChemistryEngine.verifyReactionBalanced(
            reactants = listOf("H2", "O2"),
            reactantCoefficients = listOf(2, 1),
            products = listOf("H2O"),
            productCoefficients = listOf(2)
        )
        assertTrue("2H2 + O2 -> 2H2O must be balanced", result)
    }

    @Test
    fun testBalancedAcidBaseNeutralization() {
        val result = ChemistryEngine.verifyReactionBalanced(
            reactants = listOf("HCl", "NaOH"),
            reactantCoefficients = listOf(1, 1),
            products = listOf("NaCl", "H2O"),
            productCoefficients = listOf(1, 1)
        )
        assertTrue("HCl + NaOH -> NaCl + H2O must be balanced", result)
    }

    @Test
    fun testUnbalancedReactionRejected() {
        val result = ChemistryEngine.verifyReactionBalanced(
            reactants = listOf("H2", "O2"),
            reactantCoefficients = listOf(1, 1),
            products = listOf("H2O"),
            productCoefficients = listOf(1)
        )
        assertFalse("H2 + O2 -> H2O is unbalanced and must return false", result)
    }

    @Test
    fun testEmptyReactantsRejected() {
        val result = ChemistryEngine.verifyReactionBalanced(
            reactants = emptyList(),
            reactantCoefficients = emptyList(),
            products = listOf("H2O"),
            productCoefficients = listOf(1)
        )
        assertFalse("Empty reactants must return false", result)
    }

    @Test
    fun testEmptyProductsRejected() {
        val result = ChemistryEngine.verifyReactionBalanced(
            reactants = listOf("H2"),
            reactantCoefficients = listOf(1),
            products = emptyList(),
            productCoefficients = emptyList()
        )
        assertFalse("Empty products must return false", result)
    }

    @Test
    fun testZeroCoefficientRejected() {
        val result = ChemistryEngine.verifyReactionBalanced(
            reactants = listOf("H2", "O2"),
            reactantCoefficients = listOf(0, 1),
            products = listOf("H2O"),
            productCoefficients = listOf(2)
        )
        assertFalse("Zero coefficient must return false", result)
    }

    @Test
    fun testNegativeCoefficientRejected() {
        val result = ChemistryEngine.verifyReactionBalanced(
            reactants = listOf("H2", "O2"),
            reactantCoefficients = listOf(-2, 1),
            products = listOf("H2O"),
            productCoefficients = listOf(2)
        )
        assertFalse("Negative coefficient must return false", result)
    }

    @Test
    fun testInvalidFormulaInReactionRejected() {
        val result = ChemistryEngine.verifyReactionBalanced(
            reactants = listOf("H2O?", "NaCl"),
            reactantCoefficients = listOf(1, 1),
            products = listOf("H2O"),
            productCoefficients = listOf(1)
        )
        assertFalse("Invalid formula string must return false", result)
    }

    @Test
    fun testMismatchedCoefficientSizeRejected() {
        val result = ChemistryEngine.verifyReactionBalanced(
            reactants = listOf("H2", "O2"),
            reactantCoefficients = listOf(2), // Mismatched size!
            products = listOf("H2O"),
            productCoefficients = listOf(2)
        )
        assertFalse("Mismatched coefficient size must return false", result)
    }

    @Test
    fun testReactionWithChargeConservation() {
        // Zn(0) + 2H+(+1) -> Zn2+(+2) + H2(0)
        // Charges: Left: 0 + 2*(+1) = +2; Right: +2 + 0 = +2
        val balanced = ChemistryEngine.verifyReactionBalanced(
            reactants = listOf("Zn", "H"),
            reactantCoefficients = listOf(1, 2),
            products = listOf("Zn", "H2"),
            productCoefficients = listOf(1, 1),
            reactantCharges = listOf(0, +1),
            productCharges = listOf(+2, 0)
        )
        assertTrue("Reaction with conserved charges must pass", balanced)

        // Charge mismatch: Left: +1, Right: +2
        val chargeMismatch = ChemistryEngine.verifyReactionBalanced(
            reactants = listOf("Zn", "H"),
            reactantCoefficients = listOf(1, 1),
            products = listOf("Zn", "H2"),
            productCoefficients = listOf(1, 1),
            reactantCharges = listOf(0, +1),
            productCharges = listOf(+2, 0)
        )
        assertFalse("Reaction with mismatched charges must return false", chargeMismatch)
    }

    // =========================================================
    // TASK 2.4: NUMERICAL CALCULATIONS & TOLERANCE TESTS
    // =========================================================

    @Test
    fun testMolarityCalculations() {
        // Assumptions: Dilute solution, volume in Litres. Numeric tolerance: 0.0001
        val molarity = ChemistryEngine.calculateMolarity(moles = 0.50, volumeLitres = 2.0)
        assertEquals(0.25, molarity, 0.0001)

        val moles = ChemistryEngine.calculateMolesFromMass(massGrams = 58.44, molarMassGramsPerMol = 58.44)
        val mFromMass = ChemistryEngine.calculateMolarity(moles = moles, volumeLitres = 1.0)
        assertEquals(1.0, mFromMass, 0.0001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testMolarityZeroVolumeThrows() {
        ChemistryEngine.calculateMolarity(moles = 1.0, volumeLitres = 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testMolarityNegativeVolumeThrows() {
        ChemistryEngine.calculateMolarity(moles = 1.0, volumeLitres = -1.5)
    }

    @Test
    fun testDilutionCalculations() {
        // C1*V1 = C2*V2. Tolerance: 0.0001
        // Stock C1 = 2.0 M, V1 = 25 mL, V2 = 100 mL -> C2 = 0.50 M
        val c2 = ChemistryEngine.calculateDilutionFinalConcentration(c1 = 2.0, v1 = 25.0, v2 = 100.0)
        assertEquals(0.50, c2, 0.0001)

        val v1 = ChemistryEngine.calculateDilutionRequiredVolume(c1 = 2.0, c2 = 0.50, v2 = 100.0)
        assertEquals(25.0, v1, 0.0001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testDilutionZeroFinalVolumeThrows() {
        ChemistryEngine.calculateDilutionFinalConcentration(c1 = 1.0, v1 = 10.0, v2 = 0.0)
    }

    @Test
    fun testMoleCalculations() {
        // Molar mass of water = 18.015 g/mol. Mass = 36.03 g -> n = 2.0 mol. Tolerance: 0.001
        val n = ChemistryEngine.calculateMolesFromMass(36.03, 18.015)
        assertEquals(2.0, n, 0.001)

        val m = ChemistryEngine.calculateMassFromMoles(2.0, 18.015)
        assertEquals(36.03, m, 0.001)

        val particles = ChemistryEngine.calculateParticlesFromMoles(1.0)
        assertEquals(6.02214076e23, particles, 1.0e15)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testMoleZeroMolarMassThrows() {
        ChemistryEngine.calculateMolesFromMass(10.0, 0.0)
    }

    @Test
    fun testPHCalculations() {
        // Assumption: Dilute aqueous solution at 25 °C. Tolerance: 0.001
        // [H+] = 0.01 M -> pH = 2.0
        val phAcid = ChemistryEngine.calculatePH(0.01)
        assertEquals(2.0, phAcid, 0.001)

        // Neutral water: [H+] = 1.0e-7 M -> pH = 7.0
        val phNeutral = ChemistryEngine.calculatePH(1.0e-7)
        assertEquals(7.0, phNeutral, 0.001)

        // pH + pOH = 14.0 at 25 °C
        val phFromPOH = ChemistryEngine.calculatePHFromPOH(4.0)
        assertEquals(10.0, phFromPOH, 0.001)

        val pOH = ChemistryEngine.calculatePOH(1.0e-4)
        assertEquals(4.0, pOH, 0.001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testPHZeroHydrogenConcentrationThrows() {
        ChemistryEngine.calculatePH(0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testPHNegativeHydrogenConcentrationThrows() {
        ChemistryEngine.calculatePH(-0.01)
    }

    @Test
    fun testSubatomicParticleAndChargeCalculations() {
        // Neutral atom: Protons == Electrons
        assertTrue(ChemistryEngine.validateNeutralAtom(protons = 11, neutrons = 12, electrons = 11))
        assertFalse(ChemistryEngine.validateNeutralAtom(protons = 11, neutrons = 12, electrons = 10))

        assertEquals(11, ChemistryEngine.calculateAtomicNumber(protons = 11))
        assertEquals(23, ChemistryEngine.calculateMassNumber(protons = 11, neutrons = 12))

        // Cation: p=11, e=10 -> Charge = +1
        assertEquals(+1, ChemistryEngine.calculateIonCharge(protons = 11, electrons = 10))
        // Anion: p=17, e=18 -> Charge = -1
        assertEquals(-1, ChemistryEngine.calculateIonCharge(protons = 17, electrons = 18))
        // Neutral: p=6, e=6 -> Charge = 0
        assertEquals(0, ChemistryEngine.calculateIonCharge(protons = 6, electrons = 6))
    }
}
