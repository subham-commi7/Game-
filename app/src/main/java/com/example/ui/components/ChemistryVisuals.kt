package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChemistryUniverseData
import com.example.model.ElementData
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * World 1 — Atom Observatory Interactive Visual
 * Labeled protons, neutrons, electrons, live Atomic Number (Z), Mass Number (A), Net Charge.
 */
@Composable
fun InteractiveAtomVisual(
    currentLang: String,
    reducedMotion: Boolean = false,
    visualEffectsHigh: Boolean = true,
    modifier: Modifier = Modifier
) {
    var protons by remember { mutableIntStateOf(11) } // Sodium default
    var neutrons by remember { mutableIntStateOf(12) }
    var electrons by remember { mutableIntStateOf(10) } // Na+
    var infoText by remember { mutableStateOf<String?>(null) }

    val z = protons
    val a = protons + neutrons
    val charge = protons - electrons

    val infiniteTransition = rememberInfiniteTransition(label = "atom_shells")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit_rot"
    )
    val curAngle = if (reducedMotion) 30f else angle

    SciFiCard(borderColor = BorderCyan, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (currentLang == "bn") "পরমাণু মডেল ও উপ-পারমাণবিক চার্জ" else "Atom Model & Subatomic Particles",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CyanNeon
            )
            Text(
                text = "Z = $z • A = $a",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AmberNeon,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Canvas Atom Representation
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceNavy)
                .clickable {
                    infoText = if (currentLang == "bn")
                        "ট্যাপ করা হয়েছে: পরমাণুর কেন্দ্রে নিউক্লিয়াস (p⁺, n⁰) এবং বাইরে ইলেকট্রন (e⁻) শক্তিস্তর রয়েছে।"
                    else
                        "Tapped: Central nucleus contains protons & neutrons; electrons occupy quantized shells."
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val baseRadius = size.height * 0.40f

                // Orbit 1: K shell
                drawCircle(color = BorderCyan, radius = baseRadius * 0.35f, center = center, style = Stroke(1.5f))
                // Orbit 2: L shell
                if (electrons > 2) {
                    drawCircle(color = BorderViolet, radius = baseRadius * 0.65f, center = center, style = Stroke(1.5f))
                }
                // Orbit 3: M shell
                if (electrons > 10) {
                    drawCircle(color = CyanGlow.copy(alpha = 0.6f), radius = baseRadius * 0.95f, center = center, style = Stroke(1.5f))
                }

                // Nucleus: Protons (Cyan) and Neutrons (Violet)
                val drawP = protons.coerceAtMost(10)
                for (i in 0 until drawP) {
                    val aRad = (i * (360f / drawP)) * (Math.PI / 180f)
                    val pOffset = Offset(center.x + (10f * cos(aRad)).toFloat(), center.y + (10f * sin(aRad)).toFloat())
                    drawCircle(color = CyanNeon, radius = 6f, center = pOffset)
                }
                val drawN = neutrons.coerceAtMost(10)
                for (j in 0 until drawN) {
                    val aRad = ((j * (360f / drawN)) + 20f) * (Math.PI / 180f)
                    val nOffset = Offset(center.x + (14f * cos(aRad)).toFloat(), center.y + (14f * sin(aRad)).toFloat())
                    drawCircle(color = VioletNeon, radius = 5.5f, center = nOffset)
                }

                // Orbiting Electrons
                val kElectrons = electrons.coerceAtMost(2)
                for (k in 0 until kElectrons) {
                    val kAngle = ((k * 180f) + curAngle) * (Math.PI / 180f)
                    val ePos = Offset(center.x + (baseRadius * 0.35f * cos(kAngle)).toFloat(), center.y + (baseRadius * 0.35f * sin(kAngle)).toFloat())
                    drawCircle(color = AmberLight, radius = 4f, center = ePos)
                }
                val lElectrons = (electrons - 2).coerceIn(0, 8)
                for (l in 0 until lElectrons) {
                    val lAngle = ((l * 45f) - (curAngle * 0.7f)) * (Math.PI / 180f)
                    val ePos = Offset(center.x + (baseRadius * 0.65f * cos(lAngle)).toFloat(), center.y + (baseRadius * 0.65f * sin(lAngle)).toFloat())
                    drawCircle(color = AmberNeon, radius = 3.5f, center = ePos)
                }
                val mElectrons = (electrons - 10).coerceIn(0, 8)
                for (m in 0 until mElectrons) {
                    val mAngle = ((m * 45f) + (curAngle * 0.5f)) * (Math.PI / 180f)
                    val ePos = Offset(center.x + (baseRadius * 0.95f * cos(mAngle)).toFloat(), center.y + (baseRadius * 0.95f * sin(mAngle)).toFloat())
                    drawCircle(color = AmberNeon, radius = 3.5f, center = ePos)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Particle Adjusters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ParticleAdjuster(
                label = "p⁺",
                count = protons,
                color = CyanNeon,
                onInc = { if (protons < 18) protons++ },
                onDec = { if (protons > 1) protons-- }
            )
            ParticleAdjuster(
                label = "n⁰",
                count = neutrons,
                color = VioletNeon,
                onInc = { if (neutrons < 22) neutrons++ },
                onDec = { if (neutrons > 0) neutrons-- }
            )
            ParticleAdjuster(
                label = "e⁻",
                count = electrons,
                color = AmberNeon,
                onInc = { if (electrons < 18) electrons++ },
                onDec = { if (electrons > 0) electrons-- }
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Live Readout Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceCard)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Z = $z (p⁺)", color = CyanNeon, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            Text("A = $a (p+n)", color = VioletNeon, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            val chargeStr = if (charge > 0) "+$charge (Cation)" else if (charge < 0) "$charge (Anion)" else "0 (Neutral)"
            val chargeColor = if (charge > 0) EmeraldNeon else if (charge < 0) RoseNeon else AmberNeon
            Text(chargeStr, color = chargeColor, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }

        if (infoText != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = infoText!!, color = TextSecondary, fontSize = 10.sp, lineHeight = 14.sp)
        }
    }
}

/**
 * World 2 — Periodic Galaxy Interactive Periodic Table Strip
 */
@Composable
fun InteractivePeriodicTableVisual(
    currentLang: String,
    modifier: Modifier = Modifier
) {
    val sampleElements = remember {
        listOf(
            ElementData(1, "H", "Hydrogen", "হাইড্রোজেন", 1.008, 1, 1, "Nonmetal", "অধাতু", 1, 1, "1s¹"),
            ElementData(6, "C", "Carbon", "কার্বন", 12.011, 14, 2, "Nonmetal", "অধাতু", 4, 4, "[He] 2s² 2p²"),
            ElementData(7, "N", "Nitrogen", "নাইট্রোজেন", 14.007, 15, 2, "Nonmetal", "অধাতু", 3, 5, "[He] 2s² 2p³"),
            ElementData(8, "O", "Oxygen", "অক্সিজেন", 15.999, 16, 2, "Nonmetal", "অধাতু", 2, 6, "[He] 2s² 2p⁴"),
            ElementData(9, "F", "Fluorine", "ফ্লোরিন", 18.998, 17, 2, "Halogen", "হ্যালোজেন", 1, 7, "[He] 2s² 2p⁵"),
            ElementData(11, "Na", "Sodium", "সোডিয়াম", 22.990, 1, 3, "Alkali Metal", "ক্ষার ধাতু", 1, 1, "[Ne] 3s¹"),
            ElementData(12, "Mg", "Magnesium", "ম্যাগনেসিয়াম", 24.305, 2, 3, "Alkaline Earth", "মৃৎক্ষার ধাতু", 2, 2, "[Ne] 3s²"),
            ElementData(13, "Al", "Aluminium", "অ্যালুমিনিয়াম", 26.982, 13, 3, "Post-Transition", "ধাতু", 3, 3, "[Ne] 3s² 3p¹"),
            ElementData(17, "Cl", "Chlorine", "ক্লোরিন", 35.45, 17, 3, "Halogen", "হ্যালোজেন", 1, 7, "[Ne] 3s² 3p⁵"),
            ElementData(19, "K", "Potassium", "পটাশিয়াম", 39.098, 1, 4, "Alkali Metal", "ক্ষার ধাতু", 1, 1, "[Ar] 4s¹"),
            ElementData(20, "Ca", "Calcium", "ক্যালসিয়াম", 40.078, 2, 4, "Alkaline Earth", "মৃৎক্ষার ধাতু", 2, 2, "[Ar] 4s²")
        )
    }

    var selectedElement by remember { mutableStateOf(sampleElements[5]) } // Na

    SciFiCard(borderColor = BorderCyan, modifier = modifier) {
        Text(
            text = if (currentLang == "bn") "পর্যায় সারণি এক্সপ্লোরার (মৌল ট্যাপ করুন)" else "Periodic Table Explorer (Tap Elements)",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = CyanNeon
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Element Chips Row
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(sampleElements) { el ->
                val isSelected = el.symbol == selectedElement.symbol
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) CyanNeon.copy(alpha = 0.25f) else SurfaceNavy)
                        .border(1.dp, if (isSelected) CyanNeon else BorderSubtle, RoundedCornerShape(8.dp))
                        .clickable { selectedElement = el }
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${el.atomicNumber}", fontSize = 8.sp, color = TextTertiary, fontFamily = FontFamily.Monospace)
                        Text(el.symbol, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (isSelected) CyanNeon else TextPrimary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Selected Element Inspector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceCard)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${selectedElement.symbol} — ${if (currentLang == "bn") selectedElement.nameBn else selectedElement.nameEn}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberNeon
                )
                Text(
                    text = "${if (currentLang == "bn") "গ্রুপ" else "Group"} ${selectedElement.group} • ${if (currentLang == "bn") "পর্যায়" else "Period"} ${selectedElement.period} • ${if (currentLang == "bn") selectedElement.categoryBn else selectedElement.category}",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Text(
                    text = "Config: ${selectedElement.electronConfig}",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = CyanGlow
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Valency: ${selectedElement.commonValency}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldNeon)
                Text("Mass: ${selectedElement.mass} u", fontSize = 10.sp, color = TextTertiary)
            }
        }
    }
}

/**
 * World 3 — Bonding Dimension Interactive Bond Builder Visual
 */
@Composable
fun InteractiveBondingVisual(
    currentLang: String,
    modifier: Modifier = Modifier
) {
    var mode by remember { mutableIntStateOf(0) } // 0: Ionic (NaCl), 1: Covalent (H2O)

    SciFiCard(borderColor = BorderViolet, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (currentLang == "bn") "রাসায়নিক বন্ধন সিমুলেটর" else "Chemical Bonding Simulator",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = VioletNeon
            )
            Row {
                TextButton(onClick = { mode = 0 }) {
                    Text("Ionic", color = if (mode == 0) CyanNeon else TextTertiary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
                TextButton(onClick = { mode = 1 }) {
                    Text("Covalent", color = if (mode == 1) EmeraldNeon else TextTertiary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceNavy),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cy = size.height / 2f
                if (mode == 0) {
                    // Ionic Transfer: Na+ gives e- to Cl-
                    val c1 = Offset(size.width * 0.30f, cy)
                    val c2 = Offset(size.width * 0.70f, cy)
                    // Na circle
                    drawCircle(color = CyanNeon, radius = 26f, center = c1, style = Stroke(2f))
                    // Cl circle
                    drawCircle(color = VioletNeon, radius = 34f, center = c2, style = Stroke(2f))
                    // Electron transfer path
                    drawLine(color = AmberNeon, start = Offset(c1.x + 28f, cy), end = Offset(c2.x - 36f, cy), strokeWidth = 2f)
                    drawCircle(color = AmberLight, radius = 5f, center = Offset(size.width * 0.50f, cy))
                } else {
                    // Covalent: Central O sharing with 2 H
                    val cO = Offset(size.width * 0.50f, cy)
                    val cH1 = Offset(size.width * 0.25f, cy)
                    val cH2 = Offset(size.width * 0.75f, cy)
                    drawCircle(color = RoseNeon, radius = 30f, center = cO, style = Stroke(2f))
                    drawCircle(color = CyanNeon, radius = 18f, center = cH1, style = Stroke(2f))
                    drawCircle(color = CyanNeon, radius = 18f, center = cH2, style = Stroke(2f))
                    // Overlap dots (shared pairs)
                    drawCircle(color = AmberNeon, radius = 4f, center = Offset((cO.x + cH1.x) / 2f, cy - 4f))
                    drawCircle(color = AmberNeon, radius = 4f, center = Offset((cO.x + cH1.x) / 2f, cy + 4f))
                    drawCircle(color = AmberNeon, radius = 4f, center = Offset((cO.x + cH2.x) / 2f, cy - 4f))
                    drawCircle(color = AmberNeon, radius = 4f, center = Offset((cO.x + cH2.x) / 2f, cy + 4f))
                }
            }
            if (mode == 0) {
                Row(modifier = Modifier.fillMaxWidth(0.85f), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Na⁺ (Cation)", color = CyanNeon, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Text("e⁻ Transfer ➔", color = AmberLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("Cl⁻ (Anion)", color = VioletNeon, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth(0.9f), horizontalArrangement = Arrangement.SpaceAround) {
                    Text("H", color = CyanNeon, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("H : O : H (Shared Pairs)", color = AmberLight, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Text("H", color = CyanNeon, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (mode == 0) {
                if (currentLang == "bn") "আয়নিক বন্ধন: Na সম্পূর্ণ ১টি ইলেকট্রন Cl-কে দান করে স্থিতিশীল Na⁺ ও Cl⁻ গঠন করে।"
                else "Ionic Bond: Complete electron transfer creates oppositely charged ions held by Coulombic attraction."
            } else {
                if (currentLang == "bn") "সমযোজী বন্ধন: অক্সিজেন ও হাইড্রোজেন ইলেকট্রন জোড় শেয়ার করে অষ্টক ও দ্বিত্ব পূরণ করে।"
                else "Covalent Bond: Atoms share electron pairs to mutually satisfy their octets without transferring charges."
            },
            fontSize = 11.sp,
            color = TextSecondary
        )
    }
}

/**
 * World 5 — Reaction Reactor Stoichiometric Ledger Visual
 */
@Composable
fun InteractiveReactionReactorVisual(
    currentLang: String,
    modifier: Modifier = Modifier
) {
    var coefH2 by remember { mutableIntStateOf(2) }
    var coefO2 by remember { mutableIntStateOf(1) }
    var coefH2O by remember { mutableIntStateOf(2) }

    val leftH = coefH2 * 2
    val leftO = coefO2 * 2
    val rightH = coefH2O * 2
    val rightO = coefH2O * 1
    val isBalanced = (leftH == rightH) && (leftO == rightO)

    SciFiCard(borderColor = if (isBalanced) EmeraldNeon else RoseNeon, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (currentLang == "bn") "বিক্রিয়া চুল্লি — সমতাকরণ লেজার" else "Reaction Reactor — Balancing Ledger",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isBalanced) EmeraldNeon else RoseNeon
            )
            Text(
                text = if (isBalanced) "BALANCED" else "UNBALANCED",
                color = if (isBalanced) EmeraldLight else RoseNeon,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Equation Adjuster Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceNavy)
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoefficientStepper(coef = coefH2, onInc = { if (coefH2 < 4) coefH2++ }, onDec = { if (coefH2 > 1) coefH2-- })
            Text(" H₂  +  ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CyanNeon, fontFamily = FontFamily.Monospace)
            CoefficientStepper(coef = coefO2, onInc = { if (coefO2 < 4) coefO2++ }, onDec = { if (coefO2 > 1) coefO2-- })
            Text(" O₂  ➔  ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AmberLight, fontFamily = FontFamily.Monospace)
            CoefficientStepper(coef = coefH2O, onInc = { if (coefH2O < 4) coefH2O++ }, onDec = { if (coefH2O > 1) coefH2O-- })
            Text(" H₂O", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EmeraldLight, fontFamily = FontFamily.Monospace)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Atom Count Comparison Table
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceCard)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text("Reactant H: $leftH", color = if (leftH == rightH) EmeraldNeon else RoseNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text("vs", color = TextTertiary, fontSize = 11.sp)
            Text("Product H: $rightH", color = if (leftH == rightH) EmeraldNeon else RoseNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reactant O: $leftO", color = if (leftO == rightO) EmeraldNeon else RoseNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text("vs", color = TextTertiary, fontSize = 11.sp)
            Text("Product O: $rightO", color = if (leftO == rightO) EmeraldNeon else RoseNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

/**
 * World 6 — Mole Matrix Visual
 */
@Composable
fun InteractiveMoleMatrixVisual(
    currentLang: String,
    modifier: Modifier = Modifier
) {
    var grams by remember { mutableFloatStateOf(36.0f) }
    val molarMass = 18.0f // H2O
    val moles = grams / molarMass
    val particles = moles * 6.022f

    SciFiCard(borderColor = BorderCyan, modifier = modifier) {
        Text(
            text = if (currentLang == "bn") "মোল রূপান্তর ত্রিভুজ (n = m / M)" else "Mole Conversion Triangle (n = m / M)",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = CyanNeon
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Slider for mass
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Mass m: ${grams.toInt()} g", color = AmberLight, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(90.dp))
            Slider(
                value = grams,
                onValueChange = { grams = it },
                valueRange = 9f..180f,
                steps = 18,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceNavy)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (currentLang == "bn") "ভর (m)" else "Mass (m)", fontSize = 10.sp, color = TextTertiary)
                Text("${grams.toInt()} g", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AmberLight)
            }
            Text("÷", fontSize = 16.sp, color = TextSecondary)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (currentLang == "bn") "মোলার ভর (M)" else "Molar Mass", fontSize = 10.sp, color = TextTertiary)
                Text("18 g/mol", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CyanNeon)
            }
            Text("=", fontSize = 16.sp, color = TextSecondary)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (currentLang == "bn") "মোল (n)" else "Moles (n)", fontSize = 10.sp, color = TextTertiary)
                Text(String.format("%.2f mol", moles), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EmeraldNeon)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Total Particles N = n × Nₐ = ${String.format("%.2f", particles)} × 10²³ H₂O molecules",
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = CyanGlow,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * World 7 — pH Dimension Scale Visual
 */
@Composable
fun InteractivePHScaleVisual(
    currentLang: String,
    modifier: Modifier = Modifier
) {
    var phValue by remember { mutableFloatStateOf(7.4f) } // Blood pH

    val indicatorColor = remember(phValue) {
        when {
            phValue < 3.0f -> Color(0xFFEF4444) // Strong acid: Red
            phValue < 6.0f -> Color(0xFFF59E0B) // Weak acid: Orange/Yellow
            phValue < 8.0f -> Color(0xFF10B981) // Neutral: Green
            phValue < 11.0f -> Color(0xFF00E5FF) // Weak base: Cyan
            else -> Color(0xFF8B5CF6) // Strong base: Violet
        }
    }

    val sampleFluid = remember(phValue) {
        when {
            phValue < 2.0f -> if (currentLang == "bn") "পাকস্থলীর রস (HCl, pH ~1.5)" else "Gastric Juice (HCl, pH ~1.5)"
            phValue < 4.0f -> if (currentLang == "bn") "কমলার রস / ভিনেগার (pH ~3.0)" else "Vinegar / Citrus (pH ~3.0)"
            phValue < 6.5f -> if (currentLang == "bn") "কালো কফি / মূত্র (pH ~5.5-6.0)" else "Black Coffee / Urine (pH ~6.0)"
            phValue < 7.5f -> if (currentLang == "bn") "বিশুদ্ধ পানি / মানব রক্ত (pH 7.4)" else "Pure Water / Blood (pH 7.4)"
            phValue < 9.0f -> if (currentLang == "bn") "সোডিয়াম বাইকার্বনেট / ওআরএস" else "Bicarbonate / ORS (pH ~8.4)"
            phValue < 11.0f -> if (currentLang == "bn") "মিল্ক অব ম্যাগনেসিয়া (pH 10.5)" else "Milk of Magnesia (pH 10.5)"
            else -> if (currentLang == "bn") "ব্লিচ / সোডিয়াম হাইড্রক্সাইড (pH ~13)" else "Bleach / NaOH (pH ~13.0)"
        }
    }

    SciFiCard(borderColor = indicatorColor, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (currentLang == "bn") "pH স্কেল ও নির্দেশক বর্ণালী" else "pH Scale & Physiological Fluids",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = indicatorColor
            )
            Text(
                text = String.format("pH = %.1f", phValue),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = indicatorColor,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Visual Gradient Color Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFEF4444), // 0 Red
                            Color(0xFFF59E0B), // 4 Yellow
                            Color(0xFF10B981), // 7 Green
                            Color(0xFF00E5FF), // 10 Cyan
                            Color(0xFF8B5CF6)  // 14 Violet
                        )
                    )
                )
        )

        Slider(
            value = phValue,
            onValueChange = { phValue = it },
            valueRange = 0f..14f,
            steps = 14,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceNavy)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(sampleFluid, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            val pOH = 14.0f - phValue
            Text(String.format("pOH = %.1f", pOH), color = TextSecondary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        }
    }
}

/**
 * World 8 — Solution Laboratory Dilution Visual
 */
@Composable
fun InteractiveSolutionLabVisual(
    currentLang: String,
    modifier: Modifier = Modifier
) {
    var v1 by remember { mutableFloatStateOf(25f) } // mL
    val c1 = 2.0f // M
    val v2 = 100f // mL
    val c2 = (c1 * v1) / v2 // M

    SciFiCard(borderColor = BorderCyan, modifier = modifier) {
        Text(
            text = if (currentLang == "bn") "লঘুকরণ ল্যাবরেটরি: C₁V₁ = C₂V₂" else "Solution Lab: Dilution Law (C₁V₁ = C₂V₂)",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = CyanNeon
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Stock Vol V₁: ${v1.toInt()} mL", color = AmberLight, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(130.dp))
            Slider(
                value = v1,
                onValueChange = { v1 = it },
                valueRange = 10f..50f,
                steps = 8,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceNavy)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Stock C₁", fontSize = 10.sp, color = TextTertiary)
                Text("$c1 M", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanNeon)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Stock V₁", fontSize = 10.sp, color = TextTertiary)
                Text("${v1.toInt()} mL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AmberLight)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Final V₂", fontSize = 10.sp, color = TextTertiary)
                Text("${v2.toInt()} mL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VioletNeon)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Result C₂", fontSize = 10.sp, color = TextTertiary)
                Text(String.format("%.2f M", c2), fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = EmeraldNeon)
            }
        }
    }
}

/**
 * World 9 — Organic Molecule Forest Visual
 */
@Composable
fun InteractiveOrganicForestVisual(
    currentLang: String,
    modifier: Modifier = Modifier
) {
    var selectedGroup by remember { mutableStateOf("COOH") }

    val details = remember(selectedGroup) {
        when (selectedGroup) {
            "COOH" -> Pair(
                if (currentLang == "bn") "কার্বক্সিল মূলক (-COOH)" else "Carboxyl Group (-COOH)",
                if (currentLang == "bn") "কার্বনিল (C=O) ও হাইড্রক্সিল (-OH) এর সমন্বয়। অ্যাসপিরিন ও আইবুপ্রোফেনে উপস্থিত।" else "Carbonyl + Hydroxyl on same carbon. Gives acidic character to Aspirin & Ibuprofen."
            )
            "OH" -> Pair(
                if (currentLang == "bn") "হাইড্রক্সিল মূলক (-OH)" else "Hydroxyl Group (-OH)",
                if (currentLang == "bn") "পানির সাথে হাইড্রোজেন বন্ধন তৈরি করে দ্রবণীয়তা বাড়ায়। প্যারাসিটামলে বিদ্যমান।" else "Forms strong hydrogen bonds with water. Found in Paracetamol (phenolic OH)."
            )
            "NH2" -> Pair(
                if (currentLang == "bn") "অ্যামিনো মূলক (-NH₂)" else "Amino Group (-NH₂)",
                if (currentLang == "bn") "ক্ষারকীয় নাইট্রোজেন মূলক। নিউরোট্রান্সমিটার ডোপামিন ও সেরোটোনিনে থাকে।" else "Weakly basic nitrogen with lone pair. Present in dopamine, amphetamines, antihistamines."
            )
            else -> Pair(
                if (currentLang == "bn") "এস্টার মূলক (-COOR)" else "Ester Group (-COOR)",
                if (currentLang == "bn") "অ্যাসিড ও অ্যালকোহলের ঘনীভবনে তৈরি সুগন্ধযুক্ত মূলক। অ্যাসপিরিনে থাকে।" else "Acid-alcohol condensation link. Present in acetylsalicylic acid (Aspirin)."
            )
        }
    }

    SciFiCard(borderColor = EmeraldNeon, modifier = modifier) {
        Text(
            text = if (currentLang == "bn") "জৈব কার্যকরী মূলক ফার্মাকোফোর" else "Organic Functional Group Pharmacophores",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = EmeraldNeon
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("COOH", "OH", "NH2", "COOR").forEach { grp ->
                val isSel = selectedGroup == grp
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) EmeraldNeon.copy(alpha = 0.25f) else SurfaceNavy)
                        .border(1.dp, if (isSel) EmeraldNeon else BorderSubtle, RoundedCornerShape(8.dp))
                        .clickable { selectedGroup = grp }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "-$grp",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) EmeraldNeon else TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceCard)
                .padding(10.dp)
        ) {
            Text(details.first, fontWeight = FontWeight.Bold, color = EmeraldLight, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(details.second, color = TextSecondary, fontSize = 11.sp, lineHeight = 15.sp)
        }
    }
}

/**
 * World 10 — Pharmaceutical Crystal Vault Visual
 */
@Composable
fun InteractivePharmaVaultVisual(
    currentLang: String,
    modifier: Modifier = Modifier
) {
    SciFiCard(borderColor = AmberNeon, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (currentLang == "bn") "ফার্মাসিউটিক্যাল ক্রিস্টাল ও স্যালাইন ব্যালেন্স" else "Pharma Salt & Isotonic Saline Balance",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AmberNeon
            )
            Text("0.9% w/v NaCl", fontSize = 11.sp, color = CyanNeon, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceNavy)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Na⁺ (Sodium)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanNeon)
                Text("154 mEq/L", fontSize = 10.sp, color = TextSecondary)
            }
            Text("+", fontSize = 14.sp, color = TextTertiary)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Cl⁻ (Chloride)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VioletNeon)
                Text("154 mEq/L", fontSize = 10.sp, color = TextSecondary)
            }
            Text("=", fontSize = 14.sp, color = TextTertiary)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Osmolarity", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldNeon)
                Text("308 mOsm/L", fontSize = 10.sp, color = EmeraldLight)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (currentLang == "bn")
                "০.৯% সোডিয়াম ক্লোরাইড দ্রবণ রক্তের প্লাজমার সম-অভিস্রবণিক হওয়ায় লোহিত রক্তকণিকা অক্ষত রাখে (BP106T-U4)।"
            else
                "0.9% Normal Saline is isotonic with human plasma osmolarity (~300 mOsm/L), preventing red blood cell hemolysis.",
            fontSize = 11.sp,
            color = TextSecondary,
            lineHeight = 15.sp
        )
    }
}

@Composable
private fun ParticleAdjuster(
    label: String,
    count: Int,
    color: Color,
    onInc: () -> Unit,
    onDec: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = color, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDec, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = color, modifier = Modifier.size(16.dp))
            }
            Text("$count", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp, modifier = Modifier.width(20.dp), textAlign = TextAlign.Center)
            IconButton(onClick = onInc, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Add, contentDescription = "Increase", tint = color, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun CoefficientStepper(
    coef: Int,
    onInc: () -> Unit,
    onDec: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(SurfaceCard)
            .border(1.dp, BorderSubtle, RoundedCornerShape(6.dp))
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        IconButton(onClick = onDec, modifier = Modifier.size(20.dp)) {
            Icon(Icons.Default.Remove, contentDescription = "Dec", tint = CyanNeon, modifier = Modifier.size(12.dp))
        }
        Text("$coef", fontWeight = FontWeight.Bold, color = AmberLight, fontSize = 13.sp, modifier = Modifier.width(14.dp), textAlign = TextAlign.Center)
        IconButton(onClick = onInc, modifier = Modifier.size(20.dp)) {
            Icon(Icons.Default.Add, contentDescription = "Inc", tint = CyanNeon, modifier = Modifier.size(12.dp))
        }
    }
}
