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

    /**
     * Standard IUPAC standard atomic weights (g/mol).
     */
    val ELEMENT_ATOMIC_MASSES: Map<String, Double> = mapOf(
        "H" to 1.008, "He" to 4.003, "Li" to 6.941, "Be" to 9.012, "B" to 10.81,
        "C" to 12.011, "N" to 14.007, "O" to 15.999, "F" to 18.998, "Ne" to 20.180,
        "Na" to 22.990, "Mg" to 24.305, "Al" to 26.982, "Si" to 28.085, "P" to 30.974,
        "S" to 32.06, "Cl" to 35.45, "Ar" to 39.948, "K" to 39.098, "Ca" to 40.078,
        "Sc" to 44.956, "Ti" to 47.867, "V" to 50.942, "Cr" to 51.996, "Mn" to 54.938,
        "Fe" to 55.845, "Co" to 58.933, "Ni" to 58.693, "Cu" to 63.546, "Zn" to 65.38,
        "Ga" to 69.723, "Ge" to 72.63, "As" to 74.922, "Se" to 78.971, "Br" to 79.904,
        "Kr" to 83.798, "Rb" to 85.468, "Sr" to 87.62, "Ag" to 107.87, "I" to 126.90,
        "Ba" to 137.33, "Pt" to 195.08, "Au" to 196.97, "Hg" to 200.59, "Pb" to 207.2
    )

    /**
     * Calculates the exact molar mass of any valid chemical formula.
     * Uses strict atom parser.
     */
    fun calculateMolarMass(formula: String): Double {
        val atoms = parseAtomCounts(formula)
        var totalMass = 0.0
        for ((elem, count) in atoms) {
            val elementMass = ELEMENT_ATOMIC_MASSES[elem]
                ?: throw IllegalArgumentException("Element '$elem' in formula '$formula' does not have calibrated atomic mass data")
            totalMass += elementMass * count
        }
        return totalMass
    }

    /**
     * Curated catalog of educational laboratory chemicals with verified properties.
     */
    val LAB_CHEMICALS: List<LabChemical> = listOf(
        LabChemical("NaCl", "Sodium Chloride", "সোডিয়াম ক্লোরাইড", 58.44, "Salt", 0xFF00E5FF),
        LabChemical("NaOH", "Sodium Hydroxide", "সোডিয়াম হাইড্রোক্সাইড", 40.00, "Strong Base", 0xFF8B5CF6),
        LabChemical("HCl", "Hydrochloric Acid", "হাইড্রোক্লোরিক অ্যাসিড", 36.46, "Strong Acid", 0xFFEF4444),
        LabChemical("H2SO4", "Sulfuric Acid", "সালফিউরিক অ্যাসিড", 98.08, "Strong Acid", 0xFFDC2626),
        LabChemical("C6H12O6", "D-Glucose", "ডি-গ্লুকোজ", 180.16, "Sugar / Carbohydrate", 0xFFF59E0B),
        LabChemical("CuSO4", "Copper(II) Sulfate", "কপার(II) সালফেট", 159.61, "Transition Salt", 0xFF3B82F6),
        LabChemical("KMnO4", "Potassium Permanganate", "পটাশিয়াম পারম্যাঙ্গানেট", 158.03, "Oxidizing Salt", 0xFF9333EA),
        LabChemical("CaCO3", "Calcium Carbonate", "ক্যালসিয়াম কার্বনেট", 100.09, "Insoluble Salt", 0xFFE2E8F0),
        LabChemical("NaHCO3", "Sodium Bicarbonate", "সোডিয়াম বাইকার্বনেট", 84.01, "Buffer Salt", 0xFF10B981),
        LabChemical("C8H9NO2", "Paracetamol", "প্যারাসিটামল", 151.16, "Pharmaceutical Drug", 0xFFEC4899)
    )

    /**
     * Curated database of verified educational chemical reactions.
     */
    val CURATED_REACTIONS: List<CuratedReaction> = listOf(
        CuratedReaction(
            id = "rxn_acid_base_nacl",
            nameEn = "Hydrochloric Acid & Sodium Hydroxide Neutralization",
            nameBn = "হাইড্রোক্লোরিক অ্যাসিড ও সোডিয়াম হাইড্রোক্সাইড প্রশমন",
            categoryEn = "Acid-Base Neutralization",
            categoryBn = "অম্ল-ক্ষারক প্রশমন বিক্রিয়া",
            reactants = listOf("HCl", "NaOH"),
            reactantCoefficients = listOf(1, 1),
            products = listOf("NaCl", "H2O"),
            productCoefficients = listOf(1, 1),
            conditionsEn = "Aqueous Solution (Exothermic, heat released)",
            conditionsBn = "জলীয় দ্রবণ (তাপোৎপাদী বিক্রিয়া)",
            explanationEn = "Strong acid HCl reacts with strong base NaOH in 1:1 molar stoichiometry to form table salt NaCl and water, reaching neutral pH 7.",
            explanationBn = "তীব্র অ্যাসিড HCl এবং তীব্র ক্ষারক NaOH ১:১ মোলার অনুপাতে বিক্রিয়া করে খাবার লবণ NaCl ও পানি তৈরি করে।"
        ),
        CuratedReaction(
            id = "rxn_acid_base_sulfuric",
            nameEn = "Sulfuric Acid & Sodium Hydroxide Neutralization",
            nameBn = "সালফিউরিক অ্যাসিড ও সোডিয়াম হাইড্রোক্সাইড প্রশমন",
            categoryEn = "Diprotic Acid Neutralization",
            categoryBn = "দ্বি-ক্ষারকীয় অম্ল প্রশমন",
            reactants = listOf("H2SO4", "NaOH"),
            reactantCoefficients = listOf(1, 2),
            products = listOf("Na2SO4", "H2O"),
            productCoefficients = listOf(1, 2),
            conditionsEn = "Aqueous Solution (Stoichiometry 1:2)",
            conditionsBn = "জলীয় দ্রবণ (১:২ স্টোইকিওমেট্রি)",
            explanationEn = "Diprotic H₂SO₄ yields two protons, requiring exactly 2 moles of NaOH per mole of acid to form Sodium Sulfate Na₂SO₄.",
            explanationBn = "H₂SO₄ দ্বি-ক্ষারকীয় হওয়ায় ১ মোল অ্যাসিডকে সম্পূর্ণ প্রশমিত করতে ২ মোল NaOH প্রয়োজন হয়।"
        ),
        CuratedReaction(
            id = "rxn_gas_carbonate",
            nameEn = "Calcium Carbonate & Hydrochloric Acid (Gas Evolution)",
            nameBn = "ক্যালসিয়াম কার্বনেট ও অ্যাসিড (গ্যাস উৎপাদন)",
            categoryEn = "Gas Evolution",
            categoryBn = "গ্যাস নির্গমন বিক্রিয়া",
            reactants = listOf("CaCO3", "HCl"),
            reactantCoefficients = listOf(1, 2),
            products = listOf("CaCl2", "CO2", "H2O"),
            productCoefficients = listOf(1, 1, 1),
            conditionsEn = "Effervescence observed (CO₂ gas bubbles evolve)",
            conditionsBn = "তীব্র বুদবুদ সৃষ্টি (CO₂ গ্যাস নির্গত হয়)",
            explanationEn = "Carbonate salt reacts with acid to form calcium chloride, water, and vigorous carbon dioxide gas effervescence.",
            explanationBn = "কার্বনেট লবণের সাথে অ্যাসিড বিক্রিয়া করে ক্যালসিয়াম ক্লোরাইড, পানি এবং দ্রুত CO₂ গ্যাস উৎপন্ন করে।"
        ),
        CuratedReaction(
            id = "rxn_gas_bicarb",
            nameEn = "Bicarbonate Neutralization (Antacid Action)",
            nameBn = "সোডিয়াম বাইকার্বনেট ও পাকস্থলীর অ্যাসিড প্রশমন",
            categoryEn = "Antacid Buffer Reaction",
            categoryBn = "অ্যান্টাসিড বাফার বিক্রিয়া",
            reactants = listOf("NaHCO3", "HCl"),
            reactantCoefficients = listOf(1, 1),
            products = listOf("NaCl", "CO2", "H2O"),
            productCoefficients = listOf(1, 1, 1),
            conditionsEn = "Rapid dissolution and effervescence",
            conditionsBn = "দ্রুত দ্রবীভবন ও গ্যাস নির্গমন",
            explanationEn = "Primary active ingredient in antacids (Eno, Alka-Seltzer): neutralizes stomach acid HCl rapidly producing carbon dioxide.",
            explanationBn = "অ্যান্টাসিডের প্রধান উপাদান পাকস্থলীর অতিরিক্ত HCl-কে প্রশমিত করে লবণ, পানি ও CO₂ উৎপন্ন করে।"
        ),
        CuratedReaction(
            id = "rxn_precip_agcl",
            nameEn = "Silver Nitrate & Sodium Chloride (Precipitation)",
            nameBn = "সিলভার নাইট্রেট ও সোডিয়াম ক্লোরাইড অধঃক্ষেপণ",
            categoryEn = "Double Displacement / Precipitation",
            categoryBn = "অধঃক্ষেপণ বিক্রিয়া (Precipitation)",
            reactants = listOf("AgNO3", "NaCl"),
            reactantCoefficients = listOf(1, 1),
            products = listOf("AgCl", "NaNO3"),
            productCoefficients = listOf(1, 1),
            conditionsEn = "Formation of curdy white AgCl precipitate (insoluble)",
            conditionsBn = "ঘন সাদা AgCl অধঃক্ষেপ তৈরি (অদ্রবণীয়)",
            explanationEn = "Classic analytical test for chloride ions (BP106T): Ag⁺ and Cl⁻ combine to form insoluble white silver chloride precipitate.",
            explanationBn = "ক্লোরাইড আয়ন শনাক্তকরণের মূল পরীক্ষা: Ag⁺ ও Cl⁻ যুক্ত হয়ে অদ্রবণীয় সাদা সিলভার ক্লোরাইড অধঃক্ষেপ ফেলে।"
        ),
        CuratedReaction(
            id = "rxn_synthesis_water",
            nameEn = "Hydrogen and Oxygen Combustion (Water Synthesis)",
            nameBn = "হাইড্রোজেন ও অক্সিজেনের দহনে পানি সংশ্লেষণ",
            categoryEn = "Synthesis / Redox",
            categoryBn = "সংশ্লেষণ / রেডক্স বিক্রিয়া",
            reactants = listOf("H2", "O2"),
            reactantCoefficients = listOf(2, 1),
            products = listOf("H2O"),
            productCoefficients = listOf(2),
            conditionsEn = "Ignition spark / High temperature",
            conditionsBn = "ইগনিশন স্পার্ক / উচ্চ তাপমাত্রা",
            explanationEn = "2 molecules of hydrogen gas combine with 1 molecule of oxygen to yield 2 molecules of water vapor with high energy release.",
            explanationBn = "২ মোল হাইড্রোজেন ১ মোল অক্সিজেনের সাথে যুক্ত হয়ে ২ মোল পানি তৈরি করে।"
        ),
        CuratedReaction(
            id = "rxn_haber_ammonia",
            nameEn = "Haber Process (Ammonia Synthesis)",
            nameBn = "হেবার পদ্ধতিতে অ্যামোনিয়া সংশ্লেষণ",
            categoryEn = "Industrial Gas Synthesis",
            categoryBn = "শিল্প সংশ্লেষণ বিক্রিয়া",
            reactants = listOf("N2", "H2"),
            reactantCoefficients = listOf(1, 3),
            products = listOf("NH3"),
            productCoefficients = listOf(2),
            conditionsEn = "450°C, 200 atm, Iron (Fe) catalyst",
            conditionsBn = "৪৫০°সে, ২০০ বায়ুমণ্ডলীয় চাপ, লৌহ অনুঘটক",
            explanationEn = "Nitrogen from air is fixed with hydrogen across iron catalysts at 200 atm to produce ammonia NH₃.",
            explanationBn = "বায়ুমণ্ডলের নাইট্রোজেন ও হাইড্রোজেন উচ্চ চাপে যুক্ত হয়ে সার ও ঔষধের কাঁচামাল অ্যামোনিয়া তৈরি করে।"
        ),
        CuratedReaction(
            id = "rxn_combustion_methane",
            nameEn = "Methane Complete Combustion",
            nameBn = "মিথেনের পূর্ণ দহন বিক্রিয়া",
            categoryEn = "Combustion / Hydrocarbon Oxidation",
            categoryBn = "হাইড্রোকার্বন দহন বিক্রিয়া",
            reactants = listOf("CH4", "O2"),
            reactantCoefficients = listOf(1, 2),
            products = listOf("CO2", "H2O"),
            productCoefficients = listOf(1, 2),
            conditionsEn = "Excess Oxygen, ignition source",
            conditionsBn = "পর্যাপ্ত অক্সিজেন, প্রজ্বলন শিখা",
            explanationEn = "Complete combustion of natural gas CH₄ consumes 2 moles of O₂ per mole of methane, producing carbon dioxide and steam.",
            explanationBn = "১ মোল মিথেন সম্পূর্ণ পুড়ে ২ মোল অক্সিজেনের সহায়তায় কার্বন ডাই অক্সাইড ও জলীয় বাষ্প উৎপন্ন করে।"
        ),
        CuratedReaction(
            id = "rxn_metal_acid_zinc",
            nameEn = "Zinc & Hydrochloric Acid (Hydrogen Production)",
            nameBn = "জিঙ্ক ও হাইড্রোক্লোরিক অ্যাসিড (হাইড্রোজেন গ্যাস)",
            categoryEn = "Single Displacement",
            categoryBn = "প্রতিস্থাপন বিক্রিয়া",
            reactants = listOf("Zn", "HCl"),
            reactantCoefficients = listOf(1, 2),
            products = listOf("ZnCl2", "H2"),
            productCoefficients = listOf(1, 1),
            conditionsEn = "Room temperature, rapid bubbling",
            conditionsBn = "কক্ষ তাপমাত্রা, তীব্র বুদবুদ সৃষ্টি",
            explanationEn = "Electropositive zinc displaces hydrogen from acid to form zinc chloride solution and hydrogen gas bubbles.",
            explanationBn = "জিঙ্ক ধাতু অ্যাসিড থেকে হাইড্রোজেন প্রতিস্থাপন করে এবং দ্রবণীয় জিংক ক্লোরাইড গঠন করে।"
        )
    )

    /**
     * Deterministic Stoichiometric & Limiting Reagent Engine.
     * Calculates:
     * - Initial moles from input masses or direct moles
     * - Exact limiting reagent based on stoichiometric ratio (moles / coefficient)
     * - Moles consumed of each reactant
     * - Leftover unreacted reactant quantities (moles and mass)
     * - Theoretical yield for all products in moles and grams
     * - Atom conservation ledger before and after reaction
     */
    fun calculateReactionStoichiometry(
        reaction: CuratedReaction,
        reactantQuantities: Map<String, Double>,
        isInputInMoles: Boolean
    ): ReactionOutcome {
        val reactantMolesProvided = mutableMapOf<String, Double>()
        val reactantMolarMasses = mutableMapOf<String, Double>()

        for (r in reaction.reactants) {
            val q = reactantQuantities[r] ?: 0.0
            val mm = calculateMolarMass(r)
            reactantMolarMasses[r] = mm
            reactantMolesProvided[r] = if (isInputInMoles) maxOf(0.0, q) else calculateMolesFromMass(maxOf(0.0, q), mm)
        }

        // Determine stoichiometric ratios: moles / coefficient
        var minRatio = Double.MAX_VALUE
        var limitingIndex = 0

        for (i in reaction.reactants.indices) {
            val r = reaction.reactants[i]
            val coeff = reaction.reactantCoefficients[i]
            val moles = reactantMolesProvided[r] ?: 0.0
            val ratio = if (coeff > 0) moles / coeff else 0.0
            if (ratio < minRatio) {
                minRatio = ratio
                limitingIndex = i
            }
        }

        val limitingRatio = maxOf(0.0, minRatio)
        val limitingFormula = reaction.reactants[limitingIndex]

        val limitingChemical = LAB_CHEMICALS.find { it.formula == limitingFormula }
        val limitingNameEn = limitingChemical?.nameEn ?: limitingFormula
        val limitingNameBn = limitingChemical?.nameBn ?: limitingFormula

        // Calculate consumed and leftover
        val reactantMolesConsumed = mutableMapOf<String, Double>()
        val reactantMolesLeftover = mutableMapOf<String, Double>()
        val reactantMassLeftoverGrams = mutableMapOf<String, Double>()

        for (i in reaction.reactants.indices) {
            val r = reaction.reactants[i]
            val coeff = reaction.reactantCoefficients[i]
            val mm = reactantMolarMasses[r] ?: calculateMolarMass(r)
            val provided = reactantMolesProvided[r] ?: 0.0
            val consumed = coeff * limitingRatio
            val leftoverMoles = maxOf(0.0, provided - consumed)
            val leftoverGrams = leftoverMoles * mm

            reactantMolesConsumed[r] = consumed
            reactantMolesLeftover[r] = leftoverMoles
            reactantMassLeftoverGrams[r] = leftoverGrams
        }

        // Calculate theoretical product yields
        val productYields = mutableListOf<ReactionProductYield>()
        for (j in reaction.products.indices) {
            val p = reaction.products[j]
            val coeff = reaction.productCoefficients[j]
            val mm = calculateMolarMass(p)
            val productMoles = coeff * limitingRatio
            val productGrams = productMoles * mm
            productYields.add(ReactionProductYield(p, productMoles, productGrams))
        }

        // Atom conservation calculation
        val reactantAtomCounts = mutableMapOf<String, Int>()
        for (i in reaction.reactants.indices) {
            val r = reaction.reactants[i]
            val coeff = reaction.reactantCoefficients[i]
            val atoms = parseAtomCounts(r)
            for ((elem, count) in atoms) {
                reactantAtomCounts[elem] = (reactantAtomCounts[elem] ?: 0) + (count * coeff)
            }
        }

        val productAtomCounts = mutableMapOf<String, Int>()
        for (j in reaction.products.indices) {
            val p = reaction.products[j]
            val coeff = reaction.productCoefficients[j]
            val atoms = parseAtomCounts(p)
            for ((elem, count) in atoms) {
                productAtomCounts[elem] = (productAtomCounts[elem] ?: 0) + (count * coeff)
            }
        }

        val isBalanced = reactantAtomCounts == productAtomCounts

        // Generate Step-by-Step Mathematical Explanation Ledger
        val stepsEn = mutableListOf<String>()
        val stepsBn = mutableListOf<String>()

        stepsEn.add("Step 1: Convert input quantities to moles: " + reaction.reactants.joinToString(", ") { r ->
            "$r = ${"%.3f".format(reactantMolesProvided[r])} mol"
        })
        stepsBn.add("ধাপ ১: সরবরাহকৃত পরিমাণকে মোলে রূপান্তর: " + reaction.reactants.joinToString(", ") { r ->
            "$r = ${"%.3f".format(reactantMolesProvided[r])} মোল"
        })

        stepsEn.add("Step 2: Compare molar ratio to stoichiometric coefficients: " + reaction.reactants.mapIndexed { idx, r ->
            "$r ratio = ${"%.3f".format(reactantMolesProvided[r])} ÷ ${reaction.reactantCoefficients[idx]} = ${"%.3f".format((reactantMolesProvided[r] ?: 0.0) / reaction.reactantCoefficients[idx])}"
        }.joinToString(", "))
        stepsBn.add("ধাপ ২: স্টোইকিওমেট্রিক অনুপাত তুলনা: " + reaction.reactants.mapIndexed { idx, r ->
            "$r অনুপাত = ${"%.3f".format(reactantMolesProvided[r])} ÷ ${reaction.reactantCoefficients[idx]} = ${"%.3f".format((reactantMolesProvided[r] ?: 0.0) / reaction.reactantCoefficients[idx])}"
        }.joinToString(", "))

        stepsEn.add("Step 3: Limiting Reagent is $limitingFormula ($limitingNameEn), which strictly dictates maximum theoretical yield.")
        stepsBn.add("ধাপ ৩: লিমিটিং বিক্রিয়ক হলো $limitingFormula ($limitingNameBn), যা সর্বোচ্চ তাত্ত্বিক উৎপাদ নির্ধারণ করে।")

        for (py in productYields) {
            stepsEn.add("Step 4: Theoretical yield of ${py.formula} = ${"%.3f".format(py.moles)} mol (${"%.2f".format(py.massGrams)} g).")
            stepsBn.add("ধাপ ৪: ${py.formula}-এর তাত্ত্বিক উৎপাদ = ${"%.3f".format(py.moles)} মোল (${"%.2f".format(py.massGrams)} গ্রাম)।")
        }

        return ReactionOutcome(
            reaction = reaction,
            limitingReagentFormula = limitingFormula,
            limitingReagentNameEn = limitingNameEn,
            limitingReagentNameBn = limitingNameBn,
            reactantMolesProvided = reactantMolesProvided,
            reactantMolesConsumed = reactantMolesConsumed,
            reactantMolesLeftover = reactantMolesLeftover,
            reactantMassLeftoverGrams = reactantMassLeftoverGrams,
            productYields = productYields,
            isStoichiometricallyBalanced = isBalanced,
            reactantAtomCounts = reactantAtomCounts,
            productAtomCounts = productAtomCounts,
            calculationStepsEn = stepsEn,
            calculationStepsBn = stepsBn
        )
    }

    /**
     * Convenience overload for two-reactant reaction stoichiometry simulations.
     */
    fun calculateReactionStoichiometry(
        reaction: CuratedReaction,
        reactant1Grams: Double,
        reactant2Grams: Double
    ): ReactionOutcome {
        val quantities = mapOf(
            reaction.reactants.getOrElse(0) { "" } to reactant1Grams,
            reaction.reactants.getOrElse(1) { "" } to reactant2Grams
        )
        return calculateReactionStoichiometry(reaction, quantities, isInputInMoles = false)
    }
}

data class LabChemical(
    val formula: String,
    val nameEn: String,
    val nameBn: String,
    val molarMass: Double,
    val category: String,
    val defaultColorHex: Long = 0xFF00E5FF,
    val safetyNote: String = "Standard laboratory PPE (safety goggles, lab coat, nitrile gloves) required."
)

data class CuratedReaction(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val categoryEn: String,
    val categoryBn: String,
    val reactants: List<String>,
    val reactantCoefficients: List<Int>,
    val products: List<String>,
    val productCoefficients: List<Int>,
    val conditionsEn: String = "Standard Aqueous Solution (25°C, 1 atm)",
    val conditionsBn: String = "প্রমাণ জলীয় দ্রবণ (২৫°সে, ১ বায়ুমণ্ডলীয় চাপ)",
    val explanationEn: String = "",
    val explanationBn: String = ""
) {
    val category: String get() = categoryEn
    val stoichReactant1: Int get() = reactantCoefficients.getOrElse(0) { 1 }
    val stoichReactant2: Int get() = reactantCoefficients.getOrElse(1) { 1 }
    val reactant1Formula: String get() = reactants.getOrElse(0) { "" }
    val reactant2Formula: String get() = reactants.getOrElse(1) { "" }

    val equation: String
        get() {
            val lhs = reactants.mapIndexed { idx, r ->
                val c = reactantCoefficients.getOrElse(idx) { 1 }
                if (c > 1) "$c $r" else r
            }.joinToString(" + ")
            val rhs = products.mapIndexed { idx, p ->
                val c = productCoefficients.getOrElse(idx) { 1 }
                if (c > 1) "$c $p" else p
            }.joinToString(" + ")
            return "$lhs → $rhs"
        }
}

data class ReactionProductYield(
    val formula: String,
    val moles: Double,
    val massGrams: Double
)

data class ReactionOutcome(
    val reaction: CuratedReaction,
    val limitingReagentFormula: String,
    val limitingReagentNameEn: String,
    val limitingReagentNameBn: String,
    val reactantMolesProvided: Map<String, Double>,
    val reactantMolesConsumed: Map<String, Double>,
    val reactantMolesLeftover: Map<String, Double>,
    val reactantMassLeftoverGrams: Map<String, Double>,
    val productYields: List<ReactionProductYield>,
    val isStoichiometricallyBalanced: Boolean,
    val reactantAtomCounts: Map<String, Int>,
    val productAtomCounts: Map<String, Int>,
    val calculationStepsEn: List<String>,
    val calculationStepsBn: List<String>
) {
    val limitingReagent: String
        get() = "$limitingReagentFormula ($limitingReagentNameEn)"

    val primaryProduct: String
        get() = productYields.firstOrNull()?.formula ?: ""

    val theoreticalYieldGrams: Double
        get() = productYields.firstOrNull()?.massGrams ?: 0.0

    val theoreticalYieldMoles: Double
        get() = productYields.firstOrNull()?.moles ?: 0.0

    val excessReagent: String
        get() = reactantMassLeftoverGrams.filter { it.value > 0.001 }.keys.joinToString(", ").ifEmpty { "None (Equimolar)" }

    val excessLeftoverGrams: Double
        get() = reactantMassLeftoverGrams.values.maxOrNull() ?: 0.0

    val atomConservationProof: String
        get() = if (isStoichiometricallyBalanced) "Atoms Conserved (Reactants = Products)" else "Unbalanced"

    val visualObservation: String
        get() = reaction.conditionsEn

    val pharmaceuticalNote: String
        get() = reaction.explanationEn.ifBlank { "Important benchmark reaction for pharmaceutical synthesis and analysis." }
}

