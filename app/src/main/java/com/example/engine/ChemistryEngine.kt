package com.example.engine

import kotlin.math.abs
import kotlin.math.log10

object ChemistryEngine {

    const val AVOGADRO_CONSTANT: Double = 6.02214076e23

    /**
     * Complete registry of all 118 IUPAC recognized chemical element symbols.
     */
    val VALID_ELEMENT_SYMBOLS: Set<String> = setOf(
        "H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne",
        "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar", "K", "Ca",
        "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn",
        "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr", "Y", "Zr",
        "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn",
        "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd",
        "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb",
        "Lu", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg",
        "Tl", "Pb", "Bi", "Po", "At", "Rn", "Fr", "Ra", "Ac", "Th",
        "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm",
        "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds",
        "Rg", "Cn", "Nh", "Fl", "Mc", "Lv", "Ts", "Og"
    )

    /**
     * Calculates greatest common divisor (GCD).
     */
    fun gcd(a: Int, b: Int): Int {
        var x = abs(a)
        var y = abs(b)
        while (y != 0) {
            val temp = y
            y = x % y
            x = temp
        }
        return if (x == 0) 1 else x
    }

    /**
     * Calculates the simplest whole-number ratio of cation to anion to form an electrically neutral compound.
     * Cation charge must be positive, anion charge must be negative.
     * Returns Pair(cationCount, anionCount)
     */
    fun calculateNeutralRatio(cationCharge: Int, anionCharge: Int): Pair<Int, Int> {
        require(cationCharge > 0) { "Cation charge must be positive, got $cationCharge" }
        require(anionCharge < 0) { "Anion charge must be negative, got $anionCharge" }

        val absAnion = abs(anionCharge)
        val divisor = gcd(cationCharge, absAnion)
        val cationCount = absAnion / divisor
        val anionCount = cationCharge / divisor
        return Pair(cationCount, anionCount)
    }

    /**
     * Formats chemical formula for ionic compound, correctly placing subscripts and
     * parentheses around polyatomic ions when their count > 1.
     */
    fun formatIonicFormula(
        cationSymbol: String,
        cationCharge: Int,
        isCationPolyatomic: Boolean,
        anionSymbol: String,
        anionCharge: Int,
        isAnionPolyatomic: Boolean
    ): String {
        val (catCount, anCount) = calculateNeutralRatio(cationCharge, anionCharge)

        val catPart = when {
            catCount == 1 -> cationSymbol
            isCationPolyatomic -> "($cationSymbol)${toSubscript(catCount)}"
            else -> "$cationSymbol${toSubscript(catCount)}"
        }

        val anPart = when {
            anCount == 1 -> anionSymbol
            isAnionPolyatomic -> "($anionSymbol)${toSubscript(anCount)}"
            else -> "$anionSymbol${toSubscript(anCount)}"
        }

        return "$catPart$anPart"
    }

    /**
     * Converts a positive integer to unicode subscript digits.
     */
    fun toSubscript(number: Int): String {
        if (number <= 1) return ""
        val s = number.toString()
        val sb = StringBuilder()
        for (ch in s) {
            val sub = when (ch) {
                '0' -> '₀'
                '1' -> '₁'
                '2' -> '₂'
                '3' -> '₃'
                '4' -> '₄'
                '5' -> '₅'
                '6' -> '₆'
                '7' -> '₇'
                '8' -> '₈'
                '9' -> '₉'
                else -> ch
            }
            sb.append(sub)
        }
        return sb.toString()
    }

    /**
     * Normalizes Unicode subscripts (₀..₉) to standard ASCII digits ('0'..'9').
     */
    fun normalizeFormulaText(input: String): String {
        return input.trim()
            .replace("₀", "0").replace("₁", "1").replace("₂", "2")
            .replace("₃", "3").replace("₄", "4").replace("₅", "5")
            .replace("₆", "6").replace("₇", "7").replace("₈", "8").replace("₉", "9")
    }

    /**
     * Safe multiplication avoiding integer overflow.
     */
    fun safeMultiply(a: Int, b: Int): Int {
        val res = a.toLong() * b.toLong()
        if (res > Int.MAX_VALUE || res < Int.MIN_VALUE) {
            throw ArithmeticException("Integer overflow calculating atom count ($a * $b)")
        }
        return res.toInt()
    }

    /**
     * Safe addition avoiding integer overflow.
     */
    fun safeAdd(a: Int, b: Int): Int {
        val res = a.toLong() + b.toLong()
        if (res > Int.MAX_VALUE || res < Int.MIN_VALUE) {
            throw ArithmeticException("Integer overflow calculating atom count ($a + $b)")
        }
        return res.toInt()
    }

    /**
     * Strict chemical-formula parser.
     *
     * Validates:
     * - Only recognized IUPAC element symbols.
     * - Correctly matched parentheses.
     * - No unrecognized or illegal characters (e.g., '?', '#', whitespace, punctuation).
     * - Positive atom counts (rejects zero '0' or negative counts).
     * - Non-empty formulas and non-empty groups.
     * - Safe arithmetic without integer overflow.
     *
     * Throws IllegalArgumentException or ArithmeticException with detailed diagnosis on failure.
     */
    fun parseAtomCounts(formula: String): Map<String, Int> {
        val clean = normalizeFormulaText(formula)
        if (clean.isEmpty()) {
            throw IllegalArgumentException("Chemical formula cannot be empty or whitespace")
        }

        // Fast character validation: only A-Z, a-z, 0-9, '(', ')'
        for (i in clean.indices) {
            val c = clean[i]
            val isValidChar = (c in 'A'..'Z') || (c in 'a'..'z') || (c in '0'..'9') || c == '(' || c == ')'
            if (!isValidChar) {
                throw IllegalArgumentException("Formula contains invalid character '$c' at position $i")
            }
        }

        // Parentheses balance check
        var pDepth = 0
        for (i in clean.indices) {
            val c = clean[i]
            if (c == '(') pDepth++
            else if (c == ')') {
                pDepth--
                if (pDepth < 0) {
                    throw IllegalArgumentException("Unmatched closing parenthesis ')' at position $i")
                }
            }
        }
        if (pDepth != 0) {
            throw IllegalArgumentException("Unmatched opening parenthesis '(' in formula '$clean'")
        }

        var index = 0
        val len = clean.length

        fun parseCount(): Int {
            if (index >= len || !clean[index].isDigit()) {
                return 1
            }
            if (clean[index] == '0') {
                throw IllegalArgumentException("Atom count cannot be zero or have leading zero at position $index")
            }
            val numStart = index
            while (index < len && clean[index].isDigit()) {
                index++
            }
            val numStr = clean.substring(numStart, index)
            val count = numStr.toIntOrNull() ?: throw ArithmeticException("Atom count overflow in '$numStr'")
            if (count <= 0) {
                throw IllegalArgumentException("Atom count must be strictly positive")
            }
            return count
        }

        fun parseGroup(depth: Int): Map<String, Int> {
            val groupMap = mutableMapOf<String, Int>()
            var itemsInGroup = 0

            while (index < len) {
                val c = clean[index]
                if (c == '(') {
                    index++ // consume '('
                    val inner = parseGroup(depth + 1)
                    if (index >= len || clean[index] != ')') {
                        throw IllegalArgumentException("Expected ')' matching '('")
                    }
                    index++ // consume ')'
                    val mult = parseCount()
                    for ((elem, count) in inner) {
                        val total = safeMultiply(count, mult)
                        groupMap[elem] = safeAdd(groupMap[elem] ?: 0, total)
                    }
                    itemsInGroup++
                } else if (c == ')') {
                    if (depth == 0) {
                        throw IllegalArgumentException("Unexpected ')' at top level")
                    }
                    break
                } else if (c.isUpperCase()) {
                    val elemStart = index
                    index++
                    while (index < len && clean[index].isLowerCase()) {
                        index++
                    }
                    val sym = clean.substring(elemStart, index)
                    if (!VALID_ELEMENT_SYMBOLS.contains(sym)) {
                        throw IllegalArgumentException("Unknown or invalid element symbol '$sym' in formula")
                    }
                    val count = parseCount()
                    groupMap[sym] = safeAdd(groupMap[sym] ?: 0, count)
                    itemsInGroup++
                } else if (c.isDigit()) {
                    throw IllegalArgumentException("Unexpected digit '$c' at position $index; formulas must start with an element symbol or '('")
                } else {
                    throw IllegalArgumentException("Unexpected character '$c' at position $index")
                }
            }

            if (itemsInGroup == 0) {
                throw IllegalArgumentException("Empty chemical group '()' found in formula")
            }

            return groupMap
        }

        val result = parseGroup(0)
        if (index < len) {
            throw IllegalArgumentException("Extra unparsed characters in formula '$clean' at position $index")
        }
        if (result.isEmpty()) {
            throw IllegalArgumentException("Formula contains no valid chemical elements")
        }

        return result
    }

    /**
     * Returns true if the formula is strictly valid without throwing.
     */
    fun isValidFormula(formula: String): Boolean {
        return try {
            parseAtomCounts(formula)
            true
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Verifies that a chemical reaction equation has balanced atom counts on both sides.
     * Optionally validates charge conservation if reactantCharges and productCharges are supplied.
     *
     * Rejects:
     * - Empty reactant or product lists
     * - Empty formula strings
     * - Invalid chemical formulas
     * - Mismatched coefficient list sizes
     * - Non-positive coefficients (zero or negative)
     * - Arithmetic overflow
     */
    fun verifyReactionBalanced(
        reactants: List<String>,
        reactantCoefficients: List<Int>,
        products: List<String>,
        productCoefficients: List<Int>,
        reactantCharges: List<Int>? = null,
        productCharges: List<Int>? = null
    ): Boolean {
        // 1. Non-empty lists
        if (reactants.isEmpty() || products.isEmpty()) return false

        // 2. Size alignment
        if (reactants.size != reactantCoefficients.size) return false
        if (products.size != productCoefficients.size) return false

        // 3. Positive coefficients
        for (c in reactantCoefficients) {
            if (c <= 0) return false
        }
        for (c in productCoefficients) {
            if (c <= 0) return false
        }

        // 4. Strict Atom count verification
        val leftCounts = mutableMapOf<String, Int>()
        for (i in reactants.indices) {
            val f = reactants[i]
            val coeff = reactantCoefficients[i]
            val atoms = try {
                parseAtomCounts(f)
            } catch (_: Exception) {
                return false
            }
            for ((elem, count) in atoms) {
                try {
                    val added = safeMultiply(count, coeff)
                    leftCounts[elem] = safeAdd(leftCounts[elem] ?: 0, added)
                } catch (_: ArithmeticException) {
                    return false
                }
            }
        }

        val rightCounts = mutableMapOf<String, Int>()
        for (i in products.indices) {
            val f = products[i]
            val coeff = productCoefficients[i]
            val atoms = try {
                parseAtomCounts(f)
            } catch (_: Exception) {
                return false
            }
            for ((elem, count) in atoms) {
                try {
                    val added = safeMultiply(count, coeff)
                    rightCounts[elem] = safeAdd(rightCounts[elem] ?: 0, added)
                } catch (_: ArithmeticException) {
                    return false
                }
            }
        }

        if (leftCounts != rightCounts) return false

        // 5. Charge conservation when ionic charges are supplied
        if (reactantCharges != null || productCharges != null) {
            if (reactantCharges == null || productCharges == null) return false
            if (reactantCharges.size != reactants.size) return false
            if (productCharges.size != products.size) return false

            var leftCharge: Long = 0L
            for (i in reactants.indices) {
                leftCharge += reactantCoefficients[i].toLong() * reactantCharges[i].toLong()
            }

            var rightCharge: Long = 0L
            for (i in products.indices) {
                rightCharge += productCoefficients[i].toLong() * productCharges[i].toLong()
            }

            if (leftCharge != rightCharge) return false
        }

        return true
    }

    /**
     * Validates neutral atom properties:
     * In a neutral atom: protons == electrons
     */
    fun validateNeutralAtom(protons: Int, neutrons: Int, electrons: Int): Boolean {
        if (protons <= 0 || neutrons < 0 || electrons < 0) return false
        return protons == electrons
    }

    fun calculateAtomicNumber(protons: Int): Int {
        require(protons > 0) { "Atomic number must have positive proton count" }
        return protons
    }

    fun calculateMassNumber(protons: Int, neutrons: Int): Int {
        require(protons > 0) { "Protons must be positive" }
        require(neutrons >= 0) { "Neutrons cannot be negative" }
        return protons + neutrons
    }

    fun calculateIonCharge(protons: Int, electrons: Int): Int {
        require(protons >= 0 && electrons >= 0) { "Subatomic particles cannot be negative" }
        return protons - electrons
    }

    /**
     * Moles n = mass (m) / molar mass (M)
     */
    fun calculateMolesFromMass(massGrams: Double, molarMassGramsPerMol: Double): Double {
        require(molarMassGramsPerMol > 0.0) { "Molar mass must be greater than zero" }
        require(massGrams >= 0.0) { "Mass must be non-negative" }
        return massGrams / molarMassGramsPerMol
    }

    /**
     * Mass m = moles (n) * molar mass (M)
     */
    fun calculateMassFromMoles(moles: Double, molarMassGramsPerMol: Double): Double {
        require(molarMassGramsPerMol > 0.0) { "Molar mass must be greater than zero" }
        require(moles >= 0.0) { "Moles must be non-negative" }
        return moles * molarMassGramsPerMol
    }

    /**
     * Particle count N = moles (n) * Avogadro
     */
    fun calculateParticlesFromMoles(moles: Double): Double {
        require(moles >= 0.0) { "Moles must be non-negative" }
        return moles * AVOGADRO_CONSTANT
    }

    /**
     * Molarity M = moles / volume in Litres
     */
    fun calculateMolarity(moles: Double, volumeLitres: Double): Double {
        require(volumeLitres > 0.0) { "Solution volume must be greater than zero" }
        require(moles >= 0.0) { "Moles must be non-negative" }
        return moles / volumeLitres
    }

    /**
     * Dilution C1 * V1 = C2 * V2
     * Solves for target concentration C2.
     */
    fun calculateDilutionFinalConcentration(c1: Double, v1: Double, v2: Double): Double {
        require(v2 > 0.0) { "Final volume V2 must be greater than zero" }
        require(c1 >= 0.0 && v1 > 0.0) { "Initial concentration and volume must be positive" }
        return (c1 * v1) / v2
    }

    /**
     * Solves for required initial stock volume V1 in dilution.
     */
    fun calculateDilutionRequiredVolume(c1: Double, c2: Double, v2: Double): Double {
        require(c1 > 0.0) { "Initial stock concentration C1 must be greater than zero" }
        require(c2 >= 0.0 && v2 > 0.0) { "Target concentration and final volume must be positive" }
        return (c2 * v2) / c1
    }

    /**
     * pH = -log10[H+] in dilute aqueous solution.
     * Validates [H+] > 0.0 and <= 14.0.
     */
    fun calculatePH(hydrogenConcentration: Double): Double {
        require(hydrogenConcentration > 0.0 && !hydrogenConcentration.isNaN() && !hydrogenConcentration.isInfinite()) {
            "Hydrogen ion concentration must be positive and finite, got $hydrogenConcentration"
        }
        return -log10(hydrogenConcentration)
    }

    /**
     * pOH = -log10[OH-]
     */
    fun calculatePOH(hydroxideConcentration: Double): Double {
        require(hydroxideConcentration > 0.0 && !hydroxideConcentration.isNaN() && !hydroxideConcentration.isInfinite()) {
            "Hydroxide ion concentration must be positive and finite, got $hydroxideConcentration"
        }
        return -log10(hydroxideConcentration)
    }

    /**
     * At 25°C in dilute aqueous solution: pH + pOH ≈ 14.0 (assuming Kw = 1.0e-14).
     */
    fun calculatePHFromPOH(pOH: Double): Double {
        return 14.0 - pOH
    }

    /**
     * Verifies if two numbers are equal within a specified relative or absolute tolerance.
     */
    fun isClose(actual: Double, expected: Double, tolerance: Double = 0.01): Boolean {
        val diff = abs(actual - expected)
        if (diff <= tolerance) return true
        if (expected != 0.0 && (diff / abs(expected)) <= tolerance) return true
        return false
    }
}
