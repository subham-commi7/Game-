package com.example.model

data class ElementData(
    val atomicNumber: Int,
    val symbol: String,
    val nameEn: String,
    val nameBn: String,
    val mass: Double,
    val group: Int,
    val period: Int,
    val category: String, // Alkali Metal, Alkaline Earth, Transition Metal, Nonmetal, Halogen, Noble Gas, etc.
    val categoryBn: String,
    val commonValency: Int,
    val valenceElectrons: Int,
    val electronConfig: String,
    val standardState: String = "Gas"
)

data class IonData(
    val formula: String,
    val symbolClean: String,
    val nameEn: String,
    val nameBn: String,
    val charge: Int, // e.g. +1, +2, +3, -1, -2, -3
    val isPolyatomic: Boolean = false
)

data class CompoundData(
    val formula: String,
    val nameEn: String,
    val nameBn: String,
    val cationFormula: String,
    val anionFormula: String,
    val cationCount: Int,
    val anionCount: Int,
    val molarMass: Double,
    val descriptionEn: String,
    val descriptionBn: String,
    val pharmaceuticalRelevanceEn: String = "",
    val pharmaceuticalRelevanceBn: String = ""
)

data class ReactionEquation(
    val id: String,
    val unbalancedEquation: String,
    val balancedEquation: String,
    val reactants: List<String>,
    val products: List<String>,
    val reactantCoefficients: List<Int>,
    val productCoefficients: List<Int>,
    val reactionType: String,
    val reactionTypeBn: String,
    val explanationEn: String,
    val explanationBn: String
)

data class ChemistryWorld(
    val id: Int,
    val key: String,
    val titleEn: String,
    val titleBn: String,
    val subtitleEn: String,
    val subtitleBn: String,
    val gameTitleEn: String,
    val gameTitleBn: String,
    val iconName: String,
    val learningObjectivesEn: List<String>,
    val learningObjectivesBn: List<String>
)

data class WorkedExample(
    val titleEn: String,
    val titleBn: String,
    val problemEn: String,
    val problemBn: String,
    val stepsEn: List<String>,
    val stepsBn: List<String>,
    val finalAnswer: String
)

data class ChemistryLesson(
    val id: String,
    val worldId: Int,
    val titleEn: String,
    val titleBn: String,
    val bilingualHeader: String,
    val conceptOverviewEn: String,
    val conceptOverviewBn: String,
    val formulaDisplay: String = "",
    val visualType: String, // "ATOM_SHELLS", "PERIODIC_CARD", "LEWIS_STRUCTURE", "FORMULA_GRID", "REACTION_BEAKER", "MOLE_SCALE", "PH_STRIP", "SOLUTION_FLASK", "ORGANIC_CHAIN", "PHARMA_VIAL"
    val workedExample: WorkedExample,
    val keyRuleEn: String,
    val keyRuleBn: String
)

data class ChemistryQuestion(
    val id: String,
    val worldId: Int,
    val promptEn: String,
    val promptBn: String,
    val optionsEn: List<String>,
    val optionsBn: List<String>,
    val correctIndex: Int,
    val hintEn: String,
    val hintBn: String,
    val explanationEn: String,
    val explanationBn: String,
    val formulaKey: String = "",
    val points: Int = 100
)

data class SyllabusTopic(
    val code: String,
    val subjectEn: String,
    val subjectBn: String,
    val topicEn: String,
    val topicBn: String,
    val semester: String,
    val alignmentStatus: String, // "VERIFIED", "PROVISIONAL", "ELECTIVE"
    val descriptionEn: String,
    val descriptionBn: String
)

data class ReviewCard(
    val id: String,
    val conceptKey: String,
    val worldId: Int,
    val titleEn: String,
    val titleBn: String,
    val formula: String,
    val summaryEn: String,
    val summaryBn: String,
    val nextReviewEpoch: Long,
    val intervalDays: Int,
    val repetitionCount: Int,
    val isBookmarked: Boolean = false
)

data class CampaignLevel(
    val levelNumber: Int, // 1 to 50
    val worldId: Int, // 1 to 10
    val titleEn: String,
    val titleBn: String,
    val subtitleEn: String,
    val subtitleBn: String,
    val visualType: String,
    val isBoss: Boolean = false,
    val xpReward: Int = 100,
    val objectiveEn: String,
    val objectiveBn: String,
    val conceptExplanationEn: String,
    val conceptExplanationBn: String,
    val workedExample: WorkedExample,
    val question: ChemistryQuestion,
    val keyRuleEn: String,
    val keyRuleBn: String,
    val conceptKey: String,
    val pharmacyRelevanceEn: String = "",
    val pharmacyRelevanceBn: String = ""
)

