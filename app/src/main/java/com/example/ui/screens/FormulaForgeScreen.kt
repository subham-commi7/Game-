package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChemistryUniverseData
import com.example.data.Strings
import com.example.engine.ChemistryEngine
import com.example.model.IonData
import com.example.ui.components.NeonButton
import com.example.ui.components.SciFiCard
import com.example.ui.components.SciFiTopBar
import com.example.ui.theme.*

@Composable
fun FormulaForgeScreen(
    currentLang: String,
    score: Int,
    synthesizedFormulas: Set<String> = emptySet(),
    onToggleLang: () -> Unit,
    onSynthesizeCompound: (String) -> Int,
    onBack: () -> Unit
) {
    val cations = remember { ChemistryUniverseData.COMMON_IONS.filter { it.charge > 0 } }
    val anions = remember { ChemistryUniverseData.COMMON_IONS.filter { it.charge < 0 } }

    var selectedCation by remember { mutableStateOf(cations[0]) } // Na+
    var selectedAnion by remember { mutableStateOf(anions[0]) }  // Cl-
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    var feedbackSuccess by remember { mutableStateOf(false) }

    val (catCount, anCount) = remember(selectedCation, selectedAnion) {
        ChemistryEngine.calculateNeutralRatio(selectedCation.charge, selectedAnion.charge)
    }

    val synthesizedFormula = remember(selectedCation, selectedAnion) {
        ChemistryEngine.formatIonicFormula(
            cationSymbol = selectedCation.symbolClean,
            cationCharge = selectedCation.charge,
            isCationPolyatomic = selectedCation.isPolyatomic,
            anionSymbol = selectedAnion.symbolClean,
            anionCharge = selectedAnion.charge,
            isAnionPolyatomic = selectedAnion.isPolyatomic
        )
    }

    // Match with curated compound info if exists
    val matchedCompound = remember(synthesizedFormula) {
        val clean = synthesizedFormula.replace("₀", "0").replace("₁", "1").replace("₂", "2")
            .replace("₃", "3").replace("₄", "4").replace("₅", "5")
        ChemistryUniverseData.CURATED_COMPOUNDS.find {
            it.formula.replace("₂", "2").replace("₃", "3").replace("₄", "4") == clean ||
            it.formula == synthesizedFormula
        }
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            SciFiTopBar(
                title = Strings.formulaForgeTitle(currentLang),
                score = score,
                currentLang = currentLang,
                onToggleLang = onToggleLang,
                onBack = onBack,
                testTag = "formula_forge_top_bar"
            )
        },
        containerColor = SpaceBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Description
            Text(
                text = if (currentLang == "bn")
                    "ক্যাটায়ন ও অ্যানায়ন নির্বাচন করে তড়িৎ নিরপেক্ষ যৌগ সংশ্লেষণ করুন"
                else
                    "Select cations and anions to forge electrically neutral compounds",
                fontSize = 13.sp,
                color = CyanGlow,
                fontWeight = FontWeight.SemiBold
            )

            // Select Cation Row
            Column {
                Text(
                    text = if (currentLang == "bn") "১. ক্যাটায়ন নির্বাচন (+ চার্জ)" else "1. Select Cation (+ Charge)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanNeon
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(cations) { ion ->
                        val isSelected = selectedCation == ion
                        IonChip(
                            ion = ion,
                            isSelected = isSelected,
                            accentColor = CyanNeon,
                            currentLang = currentLang,
                            onClick = { selectedCation = ion }
                        )
                    }
                }
            }

            // Select Anion Row
            Column {
                Text(
                    text = if (currentLang == "bn") "২. অ্যানায়ন নির্বাচন (- চার্জ)" else "2. Select Anion (- Charge)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletNeon
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(anions) { ion ->
                        val isSelected = selectedAnion == ion
                        IonChip(
                            ion = ion,
                            isSelected = isSelected,
                            accentColor = VioletNeon,
                            currentLang = currentLang,
                            onClick = { selectedAnion = ion }
                        )
                    }
                }
            }

            // Real-Time Electrical Neutrality Ledger
            SciFiCard(borderColor = BorderCyan) {
                Text(
                    text = if (currentLang == "bn") "তড়িৎ নিরপেক্ষতার সমীকরণ (Net Charge = 0)" else "Charge Neutrality Verification",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldLight
                )

                Spacer(modifier = Modifier.height(8.dp))

                val totalPositive = selectedCation.charge * catCount
                val totalNegative = selectedAnion.charge * anCount

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceNavy)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text("$catCount × (${selectedCation.formula}) = +$totalPositive", color = CyanNeon, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("+", color = TextSecondary, fontSize = 12.sp)
                    Text("$anCount × (${selectedAnion.formula}) = $totalNegative", color = VioletNeon, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("=", color = TextSecondary, fontSize = 12.sp)
                    Text("0", color = EmeraldNeon, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (currentLang == "bn")
                        "ক্ষুদ্রতম পূর্ণসংখ্যা অনুপাত: $catCount টি ${selectedCation.formula} এবং $anCount টি ${selectedAnion.formula}"
                    else
                        "Simplest whole-number ratio: $catCount × ${selectedCation.formula} and $anCount × ${selectedAnion.formula}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            // Synthesized Compound Forge Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceNavy)
                    .border(2.dp, CyanNeon, RoundedCornerShape(16.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Science,
                        contentDescription = "Synthesized",
                        tint = CyanNeon,
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = synthesizedFormula,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )

                    if (matchedCompound != null) {
                        Text(
                            text = if (currentLang == "bn") matchedCompound.nameBn else matchedCompound.nameEn,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberLight,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = "Molar Mass: ${matchedCompound.molarMass} g/mol",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (currentLang == "bn")
                                "ঔষধীয় গুরুত্ব: ${matchedCompound.pharmaceuticalRelevanceBn}"
                            else
                                "Pharmaceutical Relevance: ${matchedCompound.pharmaceuticalRelevanceEn}",
                            fontSize = 12.sp,
                            color = EmeraldLight,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Synthesize Button & Feedback
            val canonical = remember(synthesizedFormula) { ChemistryEngine.normalizeFormulaText(synthesizedFormula) }
            val alreadyForged = synthesizedFormulas.contains(canonical)

            if (feedbackMessage != null) {
                SciFiCard(borderColor = if (feedbackSuccess) EmeraldNeon else AmberNeon) {
                    Text(
                        text = feedbackMessage!!,
                        color = if (feedbackSuccess) EmeraldLight else AmberLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            NeonButton(
                text = if (alreadyForged) {
                    if (currentLang == "bn") "সংগ্রহশালায় সংরক্ষিত (০ পয়েন্ট)" else "Already in Codex (0 Pts)"
                } else {
                    Strings.synthesize(currentLang)
                },
                onClick = {
                    val pts = onSynthesizeCompound(synthesizedFormula)
                    if (pts > 0) {
                        feedbackSuccess = true
                        feedbackMessage = if (currentLang == "bn") "নতুন যৌগ সংশ্লেষিত! +$pts পয়েন্ট অর্জিত!" else "New compound synthesized! +$pts Points Awarded!"
                    } else {
                        feedbackSuccess = false
                        feedbackMessage = if (currentLang == "bn") "এই যৌগটি ইতোমধ্যেই সংগৃহীত। পুনঃপ্রচেষ্টায় পয়েন্ট নেই।" else "This compound is already registered. No duplicate points."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                color = if (alreadyForged) SurfaceCardHover else CyanNeon,
                textColor = if (alreadyForged) TextSecondary else SpaceBlack,
                testTag = "synthesize_compound_btn"
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun IonChip(
    ion: IonData,
    isSelected: Boolean,
    accentColor: Color,
    currentLang: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) accentColor.copy(alpha = 0.25f) else SurfaceCard)
            .border(1.dp, if (isSelected) accentColor else BorderSubtle, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag("ion_chip_${ion.symbolClean}")
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = ion.formula,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = if (isSelected) accentColor else TextPrimary
            )
            Text(
                text = if (currentLang == "bn") ion.nameBn else ion.nameEn,
                fontSize = 9.sp,
                color = TextTertiary,
                maxLines = 1
            )
        }
    }
}
