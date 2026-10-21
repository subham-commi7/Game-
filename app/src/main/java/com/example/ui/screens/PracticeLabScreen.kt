package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Strings
import com.example.engine.*
import com.example.ui.components.BeakerCanvas
import com.example.ui.components.NeonButton
import com.example.ui.components.SciFiCard
import com.example.ui.components.SciFiTopBar
import com.example.ui.theme.*
import kotlinx.coroutines.launch

enum class LabArea {
    SOLUTION_PREP,
    REACTION_SIMULATOR,
    EQUATION_INVESTIGATOR,
    AI_LAB_ASSISTANT
}

@Composable
fun PracticeLabScreen(
    currentLang: String,
    score: Int,
    onToggleLang: () -> Unit,
    onBack: () -> Unit
) {
    var activeArea by remember { mutableStateOf(LabArea.SOLUTION_PREP) }

    // Lab A State
    var selectedChemicalIndex by remember { mutableIntStateOf(0) }
    var soluteMassGrams by remember { mutableFloatStateOf(58.44f) }
    var volumeLitres by remember { mutableFloatStateOf(1.0f) }
    var customFormulaInput by remember { mutableStateOf("") }
    var isCustomFormulaMode by remember { mutableStateOf(false) }

    // Lab B State
    var selectedReactionIndex by remember { mutableIntStateOf(0) }
    var reactant1Grams by remember { mutableFloatStateOf(36.5f) }
    var reactant2Grams by remember { mutableFloatStateOf(40.0f) }

    // Lab C State
    var selectedEquatIndex by remember { mutableIntStateOf(0) }
    var coeffA by remember { mutableIntStateOf(1) }
    var coeffB by remember { mutableIntStateOf(1) }
    var coeffC by remember { mutableIntStateOf(1) }
    var coeffD by remember { mutableIntStateOf(1) }
    var showWorkedSolution by remember { mutableStateOf(false) }

    // Lab D State
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val aiService = remember { FirebaseAiService(context) }
    var aiQueryText by remember { mutableStateOf("") }
    var aiResponseText by remember { mutableStateOf<String?>(null) }
    var isAiLoading by remember { mutableStateOf(false) }

    val activeChemical = ChemistryEngine.LAB_CHEMICALS.getOrElse(selectedChemicalIndex) { ChemistryEngine.LAB_CHEMICALS[0] }
    val effectiveMolarMass = remember(isCustomFormulaMode, customFormulaInput, activeChemical) {
        if (isCustomFormulaMode && customFormulaInput.isNotBlank()) {
            val calc = ChemistryEngine.calculateMolarMass(customFormulaInput.trim())
            if (calc > 0) calc else activeChemical.molarMass
        } else {
            activeChemical.molarMass
        }
    }

    val molesSolute = remember(soluteMassGrams, effectiveMolarMass) {
        ChemistryEngine.calculateMolesFromMass(soluteMassGrams.toDouble(), effectiveMolarMass)
    }

    val molarity = remember(molesSolute, volumeLitres) {
        ChemistryEngine.calculateMolarity(molesSolute, volumeLitres.toDouble())
    }

    val massConcentration = remember(soluteMassGrams, volumeLitres) {
        if (volumeLitres > 0) soluteMassGrams / volumeLitres else 0.0f
    }

    val percentWv = remember(soluteMassGrams, volumeLitres) {
        if (volumeLitres > 0) (soluteMassGrams / (volumeLitres * 1000f)) * 100f else 0.0f
    }

    val activeReaction = ChemistryEngine.CURATED_REACTIONS.getOrElse(selectedReactionIndex) { ChemistryEngine.CURATED_REACTIONS[0] }
    val reactionOutcome = remember(activeReaction, reactant1Grams, reactant2Grams) {
        ChemistryEngine.calculateReactionStoichiometry(
            activeReaction,
            reactant1Grams.toDouble(),
            reactant2Grams.toDouble()
        )
    }

    Scaffold(
        topBar = {
            SciFiTopBar(
                title = Strings.practiceLabTitle(currentLang),
                score = score,
                currentLang = currentLang,
                onToggleLang = onToggleLang,
                onBack = onBack,
                testTag = "practice_lab_top_bar"
            )
        },
        containerColor = SpaceBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Lab Navigation Tabs
            ScrollableTabRow(
                selectedTabIndex = activeArea.ordinal,
                containerColor = SurfaceNavy,
                contentColor = CyanNeon,
                edgePadding = 12.dp
            ) {
                Tab(
                    selected = activeArea == LabArea.SOLUTION_PREP,
                    onClick = { activeArea = LabArea.SOLUTION_PREP },
                    text = {
                        Text(
                            text = if (currentLang == "bn") "১. দ্রবণ প্রস্তুত" else "1. Solution Prep",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                )
                Tab(
                    selected = activeArea == LabArea.REACTION_SIMULATOR,
                    onClick = { activeArea = LabArea.REACTION_SIMULATOR },
                    text = {
                        Text(
                            text = if (currentLang == "bn") "২. বিক্রিয়া সিমুলেটর" else "2. Reactions",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                )
                Tab(
                    selected = activeArea == LabArea.EQUATION_INVESTIGATOR,
                    onClick = { activeArea = LabArea.EQUATION_INVESTIGATOR },
                    text = {
                        Text(
                            text = if (currentLang == "bn") "৩. সমীকরণ সমতাকরণ" else "3. Balancer",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                )
                Tab(
                    selected = activeArea == LabArea.AI_LAB_ASSISTANT,
                    onClick = { activeArea = LabArea.AI_LAB_ASSISTANT },
                    text = {
                        Text(
                            text = if (currentLang == "bn") "৪. AI সহকারী" else "4. AI Lab Assistant",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (activeArea) {
                    // ==========================================
                    // LAB A: SOLUTION PREPARATION
                    // ==========================================
                    LabArea.SOLUTION_PREP -> {
                        item {
                            Text(
                                text = if (currentLang == "bn")
                                    "🧪 দ্রবণ প্রস্তুতি গবেষণাগার: রাসায়নিক পদার্থ নির্বাচন ও মোলারিটি নির্ধারণ"
                                else
                                    "🧪 Solution Preparation Laboratory: Chemical Catalog & Live Molarity",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanNeon
                            )
                        }

                        // Chemical Catalog Chips
                        item {
                            Text(
                                text = if (currentLang == "bn") "পদার্থ নির্বাচন করুন (Catalog):" else "Select Chemical from Catalog:",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(ChemistryEngine.LAB_CHEMICALS.indices.toList()) { index ->
                                    val chem = ChemistryEngine.LAB_CHEMICALS[index]
                                    val isSelected = !isCustomFormulaMode && selectedChemicalIndex == index
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) SurfaceCardHover else SurfaceNavy)
                                            .border(1.dp, if (isSelected) CyanNeon else BorderSubtle, RoundedCornerShape(8.dp))
                                            .clickable {
                                                isCustomFormulaMode = false
                                                selectedChemicalIndex = index
                                                soluteMassGrams = chem.molarMass.toFloat()
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Column {
                                            Text(
                                                text = chem.formula,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) CyanNeon else TextPrimary
                                            )
                                            Text(
                                                text = "${chem.molarMass} g/mol",
                                                fontSize = 9.sp,
                                                color = TextSecondary
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Custom Formula Option
                        item {
                            SciFiCard(borderColor = BorderSubtle) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = if (currentLang == "bn") "কাস্টম রাসায়নিক সংকেত লিখুন:" else "Or enter custom chemical formula:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextSecondary
                                    )
                                    Switch(
                                        checked = isCustomFormulaMode,
                                        onCheckedChange = { isCustomFormulaMode = it },
                                        colors = SwitchDefaults.colors(checkedThumbColor = CyanNeon, checkedTrackColor = SurfaceNavy)
                                    )
                                }
                                if (isCustomFormulaMode) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    TextField(
                                        value = customFormulaInput,
                                        onValueChange = { customFormulaInput = it },
                                        placeholder = { Text("e.g. K2Cr2O7 or Fe2(SO4)3", fontSize = 11.sp, color = TextTertiary) },
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = SpaceBlack,
                                            unfocusedContainerColor = SpaceBlack,
                                            focusedIndicatorColor = CyanNeon,
                                            unfocusedIndicatorColor = BorderSubtle,
                                            focusedTextColor = TextPrimary,
                                            unfocusedTextColor = TextPrimary
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        // Live Beaker Display
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(SurfaceNavy)
                                    .border(1.dp, BorderCyan, RoundedCornerShape(16.dp))
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                val beakerColor = when (activeChemical.formula) {
                                    "CuSO4" -> CyanNeon
                                    "KMnO4" -> VioletNeon
                                    "FeSO4" -> EmeraldNeon
                                    "HCl", "H2SO4", "HNO3" -> RoseNeon
                                    else -> when {
                                        molarity > 2.0 -> RoseNeon
                                        molarity > 0.8 -> VioletNeon
                                        molarity > 0.3 -> CyanNeon
                                        else -> EmeraldLight
                                    }
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    BeakerCanvas(
                                        liquidLevel = (volumeLitres / 3.0f).coerceIn(0.15f, 0.95f),
                                        liquidColor = beakerColor,
                                        label = "${"%.3f".format(molarity)} M ${if (isCustomFormulaMode && customFormulaInput.isNotBlank()) customFormulaInput else activeChemical.formula}"
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "${activeChemical.nameEn} (${activeChemical.nameBn}) • Safety: ${activeChemical.safetyNote}",
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }

                        // Calculation Results Card
                        item {
                            SciFiCard(borderColor = BorderCyan) {
                                Text(
                                    text = if (currentLang == "bn") "গাণিতিক বিশ্লেষণ ও ঘনমাত্রা ফলাফল" else "Real-time Quantitative Analysis",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanNeon
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SpaceBlack)
                                        .padding(10.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = "1. Moles (n) = mass / MolarMass = ${"%.2f".format(soluteMassGrams)} g / ${"%.2f".format(effectiveMolarMass)} g/mol = ${"%.4f".format(molesSolute)} mol",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 11.sp,
                                            color = EmeraldLight
                                        )
                                        Text(
                                            text = "2. Molarity (M) = n / V = ${"%.4f".format(molesSolute)} mol / ${"%.2f".format(volumeLitres)} L = ${"%.3f".format(molarity)} mol/L (M)",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CyanNeon
                                        )
                                        Text(
                                            text = "3. Mass Conc. (C) = m / V = ${"%.2f".format(massConcentration)} g/L | % w/v = ${"%.2f".format(percentWv)} %",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 11.sp,
                                            color = AmberLight
                                        )
                                        Text(
                                            text = "4. Dilution: If diluted to 2x volume, M_new = ${"%.3f".format(molarity / 2.0)} M",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 10.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        }

                        // Sliders Card
                        item {
                            SciFiCard(borderColor = BorderSubtle) {
                                Text(
                                    text = "${if (currentLang == "bn") "দ্রবের ভর (Solute Mass)" else "Solute Mass"}: ${"%.1f".format(soluteMassGrams)} g (${"%.3f".format(molesSolute)} mol)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Slider(
                                    value = soluteMassGrams,
                                    onValueChange = { soluteMassGrams = it },
                                    valueRange = 0.5f..250f,
                                    colors = SliderDefaults.colors(thumbColor = CyanNeon, activeTrackColor = CyanNeon),
                                    modifier = Modifier.testTag("mass_slider")
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "${if (currentLang == "bn") "দ্রবণের আয়তন (Solution Volume)" else "Solution Volume"}: ${"%.2f".format(volumeLitres)} L (${(volumeLitres * 1000).toInt()} mL)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Slider(
                                    value = volumeLitres,
                                    onValueChange = { volumeLitres = it },
                                    valueRange = 0.05f..3.0f,
                                    colors = SliderDefaults.colors(thumbColor = VioletNeon, activeTrackColor = VioletNeon),
                                    modifier = Modifier.testTag("volume_slider")
                                )
                            }
                        }
                    }

                    // ==========================================
                    // LAB B: REACTION SIMULATOR
                    // ==========================================
                    LabArea.REACTION_SIMULATOR -> {
                        item {
                            Text(
                                text = if (currentLang == "bn")
                                    "⚗️ রাসায়নিক বিক্রিয়া সিমুলেটর: স্টয়কিওমেট্রি ও লিমিটিং বিক্রিয়ক"
                                else
                                    "⚗️ Reaction Simulator: Stoichiometry & Limiting Reagents",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = VioletNeon
                            )
                        }

                        // Reaction Selector
                        item {
                            Text(
                                text = if (currentLang == "bn") "বিক্রিয়া নির্বাচন করুন:" else "Select Reaction Scenario:",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(ChemistryEngine.CURATED_REACTIONS.indices.toList()) { idx ->
                                    val rx = ChemistryEngine.CURATED_REACTIONS[idx]
                                    val isSel = selectedReactionIndex == idx
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSel) SurfaceCardHover else SurfaceNavy)
                                            .border(1.dp, if (isSel) VioletNeon else BorderSubtle, RoundedCornerShape(8.dp))
                                            .clickable {
                                                selectedReactionIndex = idx
                                                reactant1Grams = (rx.stoichReactant1 * ChemistryEngine.calculateMolarMass(rx.reactant1Formula)).toFloat()
                                                reactant2Grams = (rx.stoichReactant2 * ChemistryEngine.calculateMolarMass(rx.reactant2Formula)).toFloat()
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Column {
                                            Text(
                                                text = rx.category,
                                                fontSize = 9.sp,
                                                color = AmberNeon,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = rx.nameEn,
                                                fontSize = 11.sp,
                                                color = if (isSel) CyanNeon else TextPrimary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Balanced Reaction Equation Banner
                        item {
                            SciFiCard(borderColor = VioletNeon) {
                                Text(
                                    text = activeReaction.equation,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberLight
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (currentLang == "bn") activeReaction.nameBn else activeReaction.nameEn,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        // Reactant Input Controls
                        item {
                            SciFiCard(borderColor = BorderSubtle) {
                                Text(
                                    text = "${activeReaction.reactant1Formula} Mass: ${"%.1f".format(reactant1Grams)} g (${"%.3f".format(reactant1Grams / ChemistryEngine.calculateMolarMass(activeReaction.reactant1Formula))} mol)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Slider(
                                    value = reactant1Grams,
                                    onValueChange = { reactant1Grams = it },
                                    valueRange = 1f..150f,
                                    colors = SliderDefaults.colors(thumbColor = CyanNeon, activeTrackColor = CyanNeon)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "${activeReaction.reactant2Formula} Mass: ${"%.1f".format(reactant2Grams)} g (${"%.3f".format(reactant2Grams / ChemistryEngine.calculateMolarMass(activeReaction.reactant2Formula))} mol)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Slider(
                                    value = reactant2Grams,
                                    onValueChange = { reactant2Grams = it },
                                    valueRange = 1f..150f,
                                    colors = SliderDefaults.colors(thumbColor = RoseNeon, activeTrackColor = RoseNeon)
                                )
                            }
                        }

                        // Reaction Outcome & Limiting Reagent Result
                        item {
                            SciFiCard(borderColor = if (reactionOutcome.limitingReagent.contains("Neither")) EmeraldNeon else RoseNeon) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (currentLang == "bn") "স্টয়কিওমেট্রিক ফলাফল" else "Stoichiometric Outcome",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyanNeon
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (reactionOutcome.limitingReagent.contains("Neither")) SurfaceNavy else RoseNeon.copy(alpha = 0.2f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Limiting: ${reactionOutcome.limitingReagent}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (reactionOutcome.limitingReagent.contains("Neither")) EmeraldLight else RoseNeon
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SpaceBlack)
                                        .padding(10.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = "• Theoretical Yield (${reactionOutcome.primaryProduct}): ${"%.2f".format(reactionOutcome.theoreticalYieldGrams)} g (${"%.4f".format(reactionOutcome.theoreticalYieldMoles)} mol)",
                                            fontSize = 11.sp,
                                            color = EmeraldLight,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Text(
                                            text = "• Excess Leftover (${reactionOutcome.excessReagent}): ${"%.2f".format(reactionOutcome.excessLeftoverGrams)} g",
                                            fontSize = 11.sp,
                                            color = AmberLight,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Text(
                                            text = "• ${reactionOutcome.atomConservationProof}",
                                            fontSize = 10.sp,
                                            color = CyanGlow
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "🔬 Observation: ${reactionOutcome.visualObservation}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "💊 Clinical Note: ${reactionOutcome.pharmaceuticalNote}",
                                    fontSize = 10.sp,
                                    color = TextTertiary
                                )
                            }
                        }
                    }

                    // ==========================================
                    // LAB C: EQUATION & BALANCING INVESTIGATOR
                    // ==========================================
                    LabArea.EQUATION_INVESTIGATOR -> {
                        item {
                            Text(
                                text = if (currentLang == "bn")
                                    "⚖️ সমীকরণ সমতাকরণ পরীক্ষাগার: পরমাণু সংরক্ষণ ও সহগ সামঞ্জস্য"
                                else
                                    "⚖️ Equation Balancer: Atom Conservation & Coefficient Steppers",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberNeon
                            )
                        }

                        // Preset Equation selector
                        item {
                            val presetEquations = listOf(
                                Triple("H2 + O2 -> H2O", listOf(2, 1, 2), "Hydrogen Combustion"),
                                Triple("N2 + H2 -> NH3", listOf(1, 3, 2), "Haber Process"),
                                Triple("CH4 + O2 -> CO2 + H2O", listOf(1, 2, 1, 2), "Methane Combustion"),
                                Triple("Fe + O2 -> Fe2O3", listOf(4, 3, 2), "Iron Oxidation"),
                                Triple("Al + HCl -> AlCl3 + H2", listOf(2, 6, 2, 3), "Acid Displacement")
                            )

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(presetEquations.indices.toList()) { idx ->
                                    val isSel = selectedEquatIndex == idx
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSel) SurfaceCardHover else SurfaceNavy)
                                            .border(1.dp, if (isSel) AmberNeon else BorderSubtle, RoundedCornerShape(8.dp))
                                            .clickable {
                                                selectedEquatIndex = idx
                                                coeffA = 1
                                                coeffB = 1
                                                coeffC = 1
                                                coeffD = 1
                                                showWorkedSolution = false
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = presetEquations[idx].third,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSel) AmberNeon else TextPrimary
                                        )
                                    }
                                }
                            }
                        }

                        // Interactive Equation Display with Steppers
                        item {
                            SciFiCard(borderColor = BorderCyan) {
                                Text(
                                    text = if (currentLang == "bn") "সহগ সমন্বয় করুন (Adjust Coefficients):" else "Adjust Integer Coefficients:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanNeon
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                when (selectedEquatIndex) {
                                    0 -> { // H2 + O2 -> H2O
                                        val isBalanced = (coeffA * 2 == coeffC * 2) && (coeffB * 2 == coeffC * 1)
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            CoeffStepper(value = coeffA, onValueChange = { coeffA = it }, label = "a")
                                            Text(" H₂ + ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            CoeffStepper(value = coeffB, onValueChange = { coeffB = it }, label = "b")
                                            Text(" O₂ → ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            CoeffStepper(value = coeffC, onValueChange = { coeffC = it }, label = "c")
                                            Text(" H₂O", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))
                                        AtomCountRow("H (Hydrogen)", lhs = coeffA * 2, rhs = coeffC * 2)
                                        AtomCountRow("O (Oxygen)", lhs = coeffB * 2, rhs = coeffC * 1)

                                        BalanceBanner(isBalanced = isBalanced, currentLang = currentLang)
                                    }
                                    1 -> { // N2 + H2 -> NH3
                                        val isBalanced = (coeffA * 2 == coeffC * 1) && (coeffB * 2 == coeffC * 3)
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            CoeffStepper(value = coeffA, onValueChange = { coeffA = it }, label = "a")
                                            Text(" N₂ + ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            CoeffStepper(value = coeffB, onValueChange = { coeffB = it }, label = "b")
                                            Text(" H₂ → ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            CoeffStepper(value = coeffC, onValueChange = { coeffC = it }, label = "c")
                                            Text(" NH₃", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))
                                        AtomCountRow("N (Nitrogen)", lhs = coeffA * 2, rhs = coeffC * 1)
                                        AtomCountRow("H (Hydrogen)", lhs = coeffB * 2, rhs = coeffC * 3)

                                        BalanceBanner(isBalanced = isBalanced, currentLang = currentLang)
                                    }
                                    2 -> { // CH4 + O2 -> CO2 + H2O
                                        val isBalanced = (coeffA * 1 == coeffC * 1) && (coeffA * 4 == coeffD * 2) && (coeffB * 2 == coeffC * 2 + coeffD * 1)
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            CoeffStepper(value = coeffA, onValueChange = { coeffA = it }, label = "a")
                                            Text(" CH₄ + ", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            CoeffStepper(value = coeffB, onValueChange = { coeffB = it }, label = "b")
                                            Text(" O₂ → ", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            CoeffStepper(value = coeffC, onValueChange = { coeffC = it }, label = "c")
                                            Text(" CO₂ + ", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            CoeffStepper(value = coeffD, onValueChange = { coeffD = it }, label = "d")
                                            Text(" H₂O", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))
                                        AtomCountRow("C (Carbon)", lhs = coeffA * 1, rhs = coeffC * 1)
                                        AtomCountRow("H (Hydrogen)", lhs = coeffA * 4, rhs = coeffD * 2)
                                        AtomCountRow("O (Oxygen)", lhs = coeffB * 2, rhs = coeffC * 2 + coeffD * 1)

                                        BalanceBanner(isBalanced = isBalanced, currentLang = currentLang)
                                    }
                                    3 -> { // Fe + O2 -> Fe2O3
                                        val isBalanced = (coeffA * 1 == coeffC * 2) && (coeffB * 2 == coeffC * 3)
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            CoeffStepper(value = coeffA, onValueChange = { coeffA = it }, label = "a")
                                            Text(" Fe + ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            CoeffStepper(value = coeffB, onValueChange = { coeffB = it }, label = "b")
                                            Text(" O₂ → ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            CoeffStepper(value = coeffC, onValueChange = { coeffC = it }, label = "c")
                                            Text(" Fe₂O₃", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))
                                        AtomCountRow("Fe (Iron)", lhs = coeffA * 1, rhs = coeffC * 2)
                                        AtomCountRow("O (Oxygen)", lhs = coeffB * 2, rhs = coeffC * 3)

                                        BalanceBanner(isBalanced = isBalanced, currentLang = currentLang)
                                    }
                                    else -> { // Al + HCl -> AlCl3 + H2
                                        val isBalanced = (coeffA * 1 == coeffC * 1) && (coeffB * 1 == coeffD * 2) && (coeffB * 1 == coeffC * 3)
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            CoeffStepper(value = coeffA, onValueChange = { coeffA = it }, label = "a")
                                            Text(" Al + ", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            CoeffStepper(value = coeffB, onValueChange = { coeffB = it }, label = "b")
                                            Text(" HCl → ", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            CoeffStepper(value = coeffC, onValueChange = { coeffC = it }, label = "c")
                                            Text(" AlCl₃ + ", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            CoeffStepper(value = coeffD, onValueChange = { coeffD = it }, label = "d")
                                            Text(" H₂", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))
                                        AtomCountRow("Al (Aluminum)", lhs = coeffA * 1, rhs = coeffC * 1)
                                        AtomCountRow("H (Hydrogen)", lhs = coeffB * 1, rhs = coeffD * 2)
                                        AtomCountRow("Cl (Chlorine)", lhs = coeffB * 1, rhs = coeffC * 3)

                                        BalanceBanner(isBalanced = isBalanced, currentLang = currentLang)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                NeonButton(
                                    text = if (showWorkedSolution) "Hide Worked Solution" else "Reveal Balanced Solution",
                                    onClick = { showWorkedSolution = !showWorkedSolution },
                                    color = if (showWorkedSolution) BorderSubtle else CyanNeon,
                                    textColor = if (showWorkedSolution) TextPrimary else SpaceBlack,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                if (showWorkedSolution) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(SpaceBlack)
                                            .padding(10.dp)
                                    ) {
                                        Text(
                                            text = when (selectedEquatIndex) {
                                                0 -> "• Balanced: 2 H₂ + 1 O₂ → 2 H₂O (a=2, b=1, c=2)\nReasoning: 4 H atoms on both sides, 2 O atoms on both sides."
                                                1 -> "• Balanced: 1 N₂ + 3 H₂ → 2 NH₃ (a=1, b=3, c=2)\nReasoning: 2 N atoms, 6 H atoms conserved."
                                                2 -> "• Balanced: 1 CH₄ + 2 O₂ → 1 CO₂ + 2 H₂O (a=1, b=2, c=1, d=2)\nReasoning: 1 C, 4 H, and 4 O atoms balanced."
                                                3 -> "• Balanced: 4 Fe + 3 O₂ → 2 Fe₂O₃ (a=4, b=3, c=2)\nReasoning: 4 Fe atoms, 6 O atoms."
                                                else -> "• Balanced: 2 Al + 6 HCl → 2 AlCl₃ + 3 H₂ (a=2, b=6, c=2, d=3)\nReasoning: 2 Al, 6 H, 6 Cl atoms."
                                            },
                                            fontSize = 11.sp,
                                            color = EmeraldLight,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ==========================================
                    // LAB D: AI LAB ASSISTANT
                    // ==========================================
                    LabArea.AI_LAB_ASSISTANT -> {
                        item {
                            Text(
                                text = if (currentLang == "bn")
                                    "🤖 ভার্চুয়াল ল্যাব AI সহকারী: পরীক্ষাগার ভিত্তিক প্রশ্নোত্তর ও নির্দেশিকা"
                                else
                                    "🤖 Virtual Lab AI Assistant: Context-Aware Lab Inquiries & Safety",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanNeon
                            )
                        }

                        // Context Chip
                        item {
                            SciFiCard(borderColor = BorderSubtle) {
                                Text(
                                    text = "Lab Context: Active substance = ${activeChemical.formula} (${activeChemical.nameEn}) | Active reaction = ${activeReaction.nameEn}",
                                    fontSize = 11.sp,
                                    color = AmberNeon,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Pre-curated Lab Inquiry Chips
                        item {
                            val labPresetChips = listOf(
                                "How to prepare 0.1 M ${activeChemical.formula} solution?",
                                "What is the limiting reagent in this reaction?",
                                "Explain why precipitation or gas evolution happens",
                                "What laboratory safety measures are required?",
                                "How does this chemical relate to pharmacy?"
                            )

                            Text(
                                text = if (currentLang == "bn") "দ্রুত ল্যাব প্রশ্ন নির্বাচন করুন:" else "Quick Lab Inquiries:",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(labPresetChips) { chip ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(SurfaceNavy)
                                            .border(1.dp, BorderCyan, RoundedCornerShape(8.dp))
                                            .clickable(enabled = !isAiLoading) {
                                                aiQueryText = chip
                                                isAiLoading = true
                                                scope.launch {
                                                    val res = aiService.queryTutor(
                                                        TutorContext(
                                                            language = currentLang,
                                                            levelTitle = "Virtual Lab - ${activeChemical.nameEn}",
                                                            objective = "Investigate ${activeChemical.formula} and reaction ${activeReaction.nameEn}",
                                                            conceptExplanation = "Stoichiometry, solution preparation, and reaction mechanics for ${activeChemical.formula}.",
                                                            keyRule = "Conservation of mass and balanced molar proportions.",
                                                            pharmacyRelevance = activeChemical.safetyNote
                                                        ),
                                                        chip
                                                    )
                                                    isAiLoading = false
                                                    aiResponseText = when (res) {
                                                        is TutorResult.Success -> res.responseText
                                                        is TutorResult.IncompleteSetup -> res.offlineFallback
                                                        is TutorResult.Error -> res.offlineFallback
                                                    }
                                                }
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(text = chip, fontSize = 11.sp, color = TextPrimary)
                                    }
                                }
                            }
                        }

                        // Free-form Input for Lab Assistant
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SurfaceCard)
                                    .border(1.dp, BorderCyan, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 12.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    value = aiQueryText,
                                    onValueChange = { aiQueryText = it },
                                    placeholder = {
                                        Text(
                                            text = if (currentLang == "bn") "ল্যাব সম্পর্কিত যেকোনো প্রশ্ন লিখুন..." else "Ask any virtual lab question...",
                                            fontSize = 12.sp,
                                            color = TextTertiary
                                        )
                                    },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    ),
                                    modifier = Modifier.weight(1f)
                                )

                                IconButton(
                                    onClick = {
                                        val q = aiQueryText.trim()
                                        if (q.isNotEmpty()) {
                                            isAiLoading = true
                                            scope.launch {
                                                val res = aiService.queryTutor(
                                                    TutorContext(
                                                        language = currentLang,
                                                        levelTitle = "Virtual Lab - ${activeChemical.nameEn}",
                                                        objective = "Investigate ${activeChemical.formula} and reaction ${activeReaction.nameEn}",
                                                        conceptExplanation = "Stoichiometry, solution preparation, and reaction mechanics for ${activeChemical.formula}.",
                                                        keyRule = "Conservation of mass and balanced molar proportions.",
                                                        pharmacyRelevance = activeChemical.safetyNote
                                                    ),
                                                    q
                                                )
                                                isAiLoading = false
                                                aiResponseText = when (res) {
                                                    is TutorResult.Success -> res.responseText
                                                    is TutorResult.IncompleteSetup -> res.offlineFallback
                                                    is TutorResult.Error -> res.offlineFallback
                                                }
                                            }
                                        }
                                    },
                                    enabled = !isAiLoading && aiQueryText.isNotBlank()
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "Send",
                                        tint = if (aiQueryText.isNotBlank()) CyanNeon else TextTertiary
                                    )
                                }
                            }
                        }

                        // Assistant Response Display
                        item {
                            SciFiCard(borderColor = BorderCyan) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (currentLang == "bn") "AI ল্যাব উত্তর ও ব্যাখ্যা" else "AI Lab Explanation",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyanNeon
                                    )
                                    if (isAiLoading) {
                                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = CyanNeon, strokeWidth = 2.dp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                if (isAiLoading) {
                                    Text(text = "Tutor is computing chemical response...", fontSize = 11.sp, color = TextTertiary)
                                } else if (aiResponseText != null) {
                                    Text(
                                        text = aiResponseText!!,
                                        fontSize = 12.sp,
                                        color = TextPrimary,
                                        lineHeight = 17.sp
                                    )
                                } else {
                                    Text(
                                        text = if (currentLang == "bn")
                                            "উপরে একটি প্রশ্ন নির্বাচন করুন বা যেকোনো রাসায়নিক প্রস্তুতি নিয়ে প্রশ্ন জিজ্ঞাসা করুন।"
                                        else
                                            "Select a quick question above or ask any laboratory chemistry inquiry.",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CoeffStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceNavy)
            .border(1.dp, BorderCyan, RoundedCornerShape(8.dp))
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        IconButton(
            onClick = { if (value > 1) onValueChange(value - 1) },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = CyanNeon, modifier = Modifier.size(14.dp))
        }

        Text(
            text = "$value",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = AmberLight,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        IconButton(
            onClick = { if (value < 8) onValueChange(value + 1) },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Increase", tint = CyanNeon, modifier = Modifier.size(14.dp))
        }
    }
}

@Composable
fun AtomCountRow(
    elementLabel: String,
    lhs: Int,
    rhs: Int
) {
    val matches = lhs == rhs
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = elementLabel, fontSize = 11.sp, color = TextSecondary)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Reactants: $lhs | Products: $rhs",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = if (matches) EmeraldLight else RoseNeon,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = if (matches) Icons.Default.CheckCircle else Icons.Default.Close,
                contentDescription = null,
                tint = if (matches) EmeraldNeon else RoseNeon,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
fun BalanceBanner(
    isBalanced: Boolean,
    currentLang: String
) {
    Spacer(modifier = Modifier.height(10.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isBalanced) EmeraldNeon.copy(alpha = 0.2f) else SurfaceNavy)
            .border(1.dp, if (isBalanced) EmeraldNeon else BorderSubtle, RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isBalanced)
                (if (currentLang == "bn") "🎉 সমীকরণ সমতা সম্পূর্ণ! (Equation Balanced)" else "🎉 Equation Balanced Successfully!")
            else
                (if (currentLang == "bn") "⚠️ উভয় পক্ষে পরমাণুর সংখ্যা সমান নয়" else "⚠️ Atom counts are not equal on both sides"),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isBalanced) EmeraldLight else AmberNeon
        )
    }
}
