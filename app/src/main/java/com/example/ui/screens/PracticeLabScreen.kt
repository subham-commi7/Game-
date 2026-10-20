package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Strings
import com.example.engine.ChemistryEngine
import com.example.ui.components.BeakerCanvas
import com.example.ui.components.SciFiCard
import com.example.ui.components.SciFiTopBar
import com.example.ui.theme.*

@Composable
fun PracticeLabScreen(
    currentLang: String,
    score: Int,
    onToggleLang: () -> Unit,
    onBack: () -> Unit
) {
    var soluteMassGrams by remember { mutableFloatStateOf(58.44f) } // Default 1 mole of NaCl
    var molarMass by remember { mutableFloatStateOf(58.44f) }
    var volumeLitres by remember { mutableFloatStateOf(1.0f) }

    val moles = remember(soluteMassGrams, molarMass) {
        ChemistryEngine.calculateMolesFromMass(soluteMassGrams.toDouble(), molarMass.toDouble())
    }

    val molarity = remember(moles, volumeLitres) {
        ChemistryEngine.calculateMolarity(moles, volumeLitres.toDouble())
    }

    val scrollState = rememberScrollState()

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
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (currentLang == "bn")
                    "ভার্চুয়াল রসায়নাগারে স্বাধীনভাবে দ্রবণ তৈরি ও ঘনমাত্রা পরিবর্তন পর্যবেক্ষণ করুন"
                else
                    "Freely experiment with solution preparation and live molarity calculations",
                fontSize = 13.sp,
                color = CyanGlow,
                fontWeight = FontWeight.SemiBold
            )

            // Live Animated Beaker Reaction Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceNavy)
                    .border(1.dp, BorderCyan, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                val concentrationColor = when {
                    molarity > 2.0 -> RoseNeon
                    molarity > 0.8 -> VioletNeon
                    molarity > 0.3 -> CyanNeon
                    else -> EmeraldNeon
                }

                BeakerCanvas(
                    liquidLevel = (volumeLitres / 2.0f).coerceIn(0.2f, 0.95f),
                    liquidColor = concentrationColor,
                    label = "Molarity: ${"%.3f".format(molarity)} mol/L"
                )
            }

            // Calculation Results Ledger
            SciFiCard(borderColor = BorderCyan) {
                Text(
                    text = if (currentLang == "bn") "গাণিতিক সমীকরণ ও বিশ্লেষণ" else "Real-time Mathematical Solution",
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
                    Column {
                        Text(
                            text = "n = mass / MolarMass = ${"%.2f".format(soluteMassGrams)} g / ${"%.2f".format(molarMass)} g/mol = ${"%.3f".format(moles)} mol",
                            fontFamily = FontFamily.Monospace,
                            color = EmeraldLight,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "M = n / V = ${"%.3f".format(moles)} mol / ${"%.2f".format(volumeLitres)} L = ${"%.3f".format(molarity)} M",
                            fontFamily = FontFamily.Monospace,
                            color = CyanNeon,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Sliders for Solute Mass & Volume
            SciFiCard(borderColor = BorderSubtle) {
                Text(
                    text = "${if (currentLang == "bn") "দ্রবের ভর (Solute Mass)" else "Solute Mass"}: ${"%.1f".format(soluteMassGrams)} g",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Slider(
                    value = soluteMassGrams,
                    onValueChange = { soluteMassGrams = it },
                    valueRange = 5f..200f,
                    colors = SliderDefaults.colors(thumbColor = CyanNeon, activeTrackColor = CyanNeon),
                    modifier = Modifier.testTag("mass_slider")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "${if (currentLang == "bn") "দ্রবণের চূড়ান্ত আয়তন (Solution Volume)" else "Solution Volume"}: ${"%.2f".format(volumeLitres)} L (${(volumeLitres * 1000).toInt()} mL)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Slider(
                    value = volumeLitres,
                    onValueChange = { volumeLitres = it },
                    valueRange = 0.1f..2.5f,
                    colors = SliderDefaults.colors(thumbColor = VioletNeon, activeTrackColor = VioletNeon),
                    modifier = Modifier.testTag("volume_slider")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
