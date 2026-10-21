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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.WaterDrop
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
import com.example.engine.ChemistryEngine
import com.example.model.ElementData
import com.example.ui.theme.*
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.log10
import kotlin.math.sin

// =====================================================================
// WORLD 1 — ATOM OBSERVATORY INTERACTIVE SIMULATOR & NUCLIDE BUILDER
// =====================================================================

data class AtomMission(
    val nameEn: String,
    val nameBn: String,
    val targetP: Int,
    val targetN: Int,
    val targetE: Int,
    val hintEn: String,
    val hintBn: String
)

@Composable
fun InteractiveAtomVisual(
    levelNumber: Int = 1,
    currentLang: String,
    reducedMotion: Boolean = false,
    visualEffectsHigh: Boolean = true,
    onDiscoveryApplied: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val mission = remember(levelNumber) {
        when (levelNumber) {
            1 -> AtomMission("Neutral Carbon-12", "চার্জহীন কার্বন-১২", 6, 6, 6, "Protons must equal electrons for neutral atom (p=6, e=6).", "নিরপেক্ষ পরমাণুর জন্য প্রোটন ও ইলেকট্রন সংখ্যা সমান (p=৬, e=৬)।")
            2 -> AtomMission("Stable Sodium-23 (²³Na)", "স্থিতিশীল সোডিয়াম-২৩", 11, 12, 11, "A = 23 requires 11 protons and 12 neutrons.", "ভর সংখ্যা ২৩ এর জন্য ১১টি প্রোটন ও ১২টি নিউট্রন প্রয়োজন।")
            3 -> AtomMission("Neon Octet (¹⁰Ne)", "নিয়ন অষ্টক (¹⁰Ne)", 10, 10, 10, "Fill K shell (2) and L shell (8) completely.", "K শক্তিস্তরে ২টি এবং L স্তরে ৮টি ইলেকট্রন পূর্ণ করুন।")
            4 -> AtomMission("Sodium Cation (Na⁺)", "সোডিয়াম ক্যাটায়ন (Na⁺)", 11, 12, 10, "Remove 1 electron from neutral Na to get +1 net charge.", "নিট চার্জ +১ করার জন্য নিরপেক্ষ Na থেকে ১টি ইলেকট্রন হ্রাস করুন।")
            else -> AtomMission("Carbon-14 Isotope (¹⁴C)", "কার্বন-১৪ আইসোটোপ", 6, 8, 6, "Z=6, A=14 nuclide contains 6 protons and 8 neutrons.", "Z=৬, A=১৪ নিউক্লাইডে ৬টি প্রোটন ও ৮টি নিউট্রন থাকে।")
        }
    }

    var protons by remember(levelNumber) { mutableIntStateOf(if (levelNumber == 1) 6 else if (levelNumber == 3) 10 else 11) }
    var neutrons by remember(levelNumber) { mutableIntStateOf(if (levelNumber == 1) 6 else if (levelNumber == 3) 10 else 12) }
    var electrons by remember(levelNumber) { mutableIntStateOf(if (levelNumber == 4) 11 else if (levelNumber == 3) 10 else 6) }

    var validationStatus by remember(levelNumber) { mutableStateOf<Boolean?>(null) }
    var validationMessage by remember(levelNumber) { mutableStateOf("") }

    val z = protons
    val a = protons + neutrons
    val charge = protons - electrons

    val isTargetReached = (protons == mission.targetP) && (neutrons == mission.targetN) && (electrons == mission.targetE)

    val infiniteTransition = rememberInfiniteTransition(label = "atom_orbit_transition")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit_rot"
    )
    val curAngle = if (reducedMotion) 35f else angle

    SciFiCard(borderColor = if (validationStatus == true) EmeraldNeon else BorderCyan, modifier = modifier) {
        // Mission Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (currentLang == "bn") "বিশ্ব ১ • পরমাণু ল্যাবরেটরি মিশন" else "World 1 • Atom Lab Mission",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanNeon
                )
                Text(
                    text = if (currentLang == "bn") mission.nameBn else mission.nameEn,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberLight
                )
            }
            Text(
                text = "Z = $z • A = $a",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AmberNeon,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Visual Atom Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceNavy),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val baseRadius = size.height * 0.40f

                // K Shell (Capacity 2)
                drawCircle(color = BorderCyan.copy(alpha = 0.5f), radius = baseRadius * 0.35f, center = center, style = Stroke(1.5f))
                // L Shell (Capacity 8)
                if (electrons > 2) {
                    drawCircle(color = BorderViolet.copy(alpha = 0.5f), radius = baseRadius * 0.65f, center = center, style = Stroke(1.5f))
                }
                // M Shell (Capacity 8/18)
                if (electrons > 10) {
                    drawCircle(color = CyanGlow.copy(alpha = 0.6f), radius = baseRadius * 0.95f, center = center, style = Stroke(1.5f))
                }

                // Nucleus: Draw Protons (Cyan) & Neutrons (Violet)
                val drawP = protons.coerceAtMost(12)
                for (i in 0 until drawP) {
                    val aRad = (i * (360f / drawP)) * (Math.PI / 180f)
                    val pOffset = Offset(center.x + (10f * cos(aRad)).toFloat(), center.y + (10f * sin(aRad)).toFloat())
                    drawCircle(color = CyanNeon, radius = 5.5f, center = pOffset)
                }
                val drawN = neutrons.coerceAtMost(12)
                for (j in 0 until drawN) {
                    val aRad = ((j * (360f / drawN)) + 25f) * (Math.PI / 180f)
                    val nOffset = Offset(center.x + (14f * cos(aRad)).toFloat(), center.y + (14f * sin(aRad)).toFloat())
                    drawCircle(color = VioletNeon, radius = 5f, center = nOffset)
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

            // Central Nucleus Tag
            Text(
                text = "Z=$z",
                color = Color.White.copy(alpha = 0.85f),
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Particle Counter Steppers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ParticleAdjuster(
                label = "p⁺ (Protons)",
                count = protons,
                color = CyanNeon,
                onInc = { if (protons < 18) { protons++; validationStatus = null } },
                onDec = { if (protons > 1) { protons--; validationStatus = null } }
            )
            ParticleAdjuster(
                label = "n⁰ (Neutrons)",
                count = neutrons,
                color = VioletNeon,
                onInc = { if (neutrons < 22) { neutrons++; validationStatus = null } },
                onDec = { if (neutrons > 0) { neutrons--; validationStatus = null } }
            )
            ParticleAdjuster(
                label = "e⁻ (Electrons)",
                count = electrons,
                color = AmberNeon,
                onInc = { if (electrons < 18) { electrons++; validationStatus = null } },
                onDec = { if (electrons > 0) { electrons--; validationStatus = null } }
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Z = $z", color = CyanNeon, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            Text("A = $a", color = VioletNeon, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            val chargeStr = if (charge > 0) "+$charge (Cation)" else if (charge < 0) "$charge (Anion)" else "0 (Neutral)"
            val chargeColor = if (charge > 0) EmeraldNeon else if (charge < 0) RoseNeon else AmberNeon
            Text(chargeStr, color = chargeColor, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Check Configuration Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    if (isTargetReached) {
                        validationStatus = true
                        validationMessage = if (currentLang == "bn")
                            "✓ সঠিক গঠন! লক্ষ্য নিউক্লাইড তৈরি সম্পন্ন হয়েছে (p=$protons, n=$neutrons, e=$electrons)। নিট চার্জ $charge।"
                        else
                            "✓ Correct configuration! Target nuclide successfully assembled (p=$protons, n=$neutrons, e=$electrons). Net charge: $charge."
                        onDiscoveryApplied?.invoke("p=$protons, n=$neutrons, e=$electrons, Charge=$charge")
                    } else {
                        validationStatus = false
                        validationMessage = if (currentLang == "bn")
                            "লক্ষ্য মেলেনি। নির্দেশ: ${mission.hintBn} (বর্তমান: p=$protons, n=$neutrons, e=$electrons)"
                        else
                            "Target not matched. Hint: ${mission.hintEn} (Current: p=$protons, n=$neutrons, e=$electrons)"
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (validationStatus == true) EmeraldNeon else CyanNeon,
                    contentColor = SpaceBlack
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(if (validationStatus == true) Icons.Default.CheckCircle else Icons.Default.Science, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (currentLang == "bn") "গঠন যাচাই করুন" else "Verify Configuration",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            OutlinedButton(
                onClick = {
                    protons = mission.targetP
                    neutrons = mission.targetN
                    electrons = mission.targetE
                    validationStatus = true
                    validationMessage = if (currentLang == "bn") "লক্ষ্য মান সেট করা হয়েছে!" else "Target values configured!"
                },
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Preset", tint = CyanNeon, modifier = Modifier.size(14.dp))
            }
        }

        if (validationMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = validationMessage,
                color = if (validationStatus == true) EmeraldLight else RoseLight,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// =====================================================================
// WORLD 2 — PERIODIC GALAXY INTERACTIVE TABLE & TREND EXPLORER
// =====================================================================

enum class PeriodicTrend { ALL, RADIUS, ELECTRONEGATIVITY, VALENCE, METALS }

@Composable
fun InteractivePeriodicTableVisual(
    levelNumber: Int = 6,
    currentLang: String,
    modifier: Modifier = Modifier
) {
    val elements = remember {
        listOf(
            ElementData(1, "H", "Hydrogen", "হাইড্রোজেন", 1.008, 1, 1, "Nonmetal", "অধাতু", 1, 1, "1s¹"),
            ElementData(2, "He", "Helium", "হিলিয়াম", 4.003, 18, 1, "Noble Gas", "নিষ্ক্রিয় গ্যাস", 0, 2, "1s²"),
            ElementData(3, "Li", "Lithium", "লিথিয়াম", 6.941, 1, 2, "Alkali Metal", "ক্ষার ধাতু", 1, 1, "[He] 2s¹"),
            ElementData(4, "Be", "Beryllium", "বেরিলিয়াম", 9.012, 2, 2, "Alkaline Earth", "মৃৎক্ষার ধাতু", 2, 2, "[He] 2s²"),
            ElementData(5, "B", "Boron", "বোরন", 10.81, 13, 2, "Metalloid", "উপধাতু", 3, 3, "[He] 2s² 2p¹"),
            ElementData(6, "C", "Carbon", "কার্বন", 12.011, 14, 2, "Nonmetal", "অধাতু", 4, 4, "[He] 2s² 2p²"),
            ElementData(7, "N", "Nitrogen", "নাইট্রোজেন", 14.007, 15, 2, "Nonmetal", "অধাতু", 3, 5, "[He] 2s² 2p³"),
            ElementData(8, "O", "Oxygen", "অক্সিজেন", 15.999, 16, 2, "Nonmetal", "অধাতু", 2, 6, "[He] 2s² 2p⁴"),
            ElementData(9, "F", "Fluorine", "ফ্লোরিন", 18.998, 17, 2, "Halogen", "হ্যালোজেন", 1, 7, "[He] 2s² 2p⁵"),
            ElementData(10, "Ne", "Neon", "নিয়ন", 20.180, 18, 2, "Noble Gas", "নিষ্ক্রিয় গ্যাস", 0, 8, "[He] 2s² 2p⁶"),
            ElementData(11, "Na", "Sodium", "সোডিয়াম", 22.990, 1, 3, "Alkali Metal", "ক্ষার ধাতু", 1, 1, "[Ne] 3s¹"),
            ElementData(12, "Mg", "Magnesium", "ম্যাগনেসিয়াম", 24.305, 2, 3, "Alkaline Earth", "মৃৎক্ষার ধাতু", 2, 2, "[Ne] 3s²"),
            ElementData(13, "Al", "Aluminium", "অ্যালুমিনিয়াম", 26.982, 13, 3, "Post-Transition", "ধাতু", 3, 3, "[Ne] 3s² 3p¹"),
            ElementData(14, "Si", "Silicon", "সিলিকন", 28.085, 14, 3, "Metalloid", "উপধাতু", 4, 4, "[Ne] 3s² 3p²"),
            ElementData(15, "P", "Phosphorus", "ফসফরাস", 30.974, 15, 3, "Nonmetal", "অধাতু", 3, 5, "[Ne] 3s² 3p³"),
            ElementData(16, "S", "Sulfur", "সালফার", 32.06, 16, 3, "Nonmetal", "অধাতু", 2, 6, "[Ne] 3s² 3p⁴"),
            ElementData(17, "Cl", "Chlorine", "ক্লোরিন", 35.45, 17, 3, "Halogen", "হ্যালোজেন", 1, 7, "[Ne] 3s² 3p⁵"),
            ElementData(18, "Ar", "Argon", "আর্গন", 39.948, 18, 3, "Noble Gas", "নিষ্ক্রিয় গ্যাস", 0, 8, "[Ne] 3s² 3p⁶"),
            ElementData(19, "K", "Potassium", "পটাশিয়াম", 39.098, 1, 4, "Alkali Metal", "ক্ষার ধাতু", 1, 1, "[Ar] 4s¹"),
            ElementData(20, "Ca", "Calcium", "ক্যালসিয়াম", 40.078, 2, 4, "Alkaline Earth", "মৃৎক্ষার ধাতু", 2, 2, "[Ar] 4s²"),
            ElementData(26, "Fe", "Iron", "আয়রন", 55.845, 8, 4, "Transition Metal", "রূপান্তর ধাতু", 2, 2, "[Ar] 3d⁶ 4s²"),
            ElementData(29, "Cu", "Copper", "কপার", 63.546, 11, 4, "Transition Metal", "রূপান্তর ধাতু", 2, 1, "[Ar] 3d¹⁰ 4s¹")
        )
    }

    var selectedTrend by remember { mutableStateOf(PeriodicTrend.ALL) }
    var selectedElement by remember(levelNumber) {
        val initialSymbol = when (levelNumber) {
            6 -> "F"
            7 -> "Na"
            8 -> "Ar"
            9 -> "Ca"
            else -> "Fe"
        }
        mutableStateOf(elements.firstOrNull { it.symbol == initialSymbol } ?: elements[8])
    }

    val targetSymbol = when (levelNumber) {
        6 -> "F"
        7 -> "Na"
        8 -> "Ar"
        9 -> "Ca"
        else -> "Fe"
    }

    val isTargetMatched = selectedElement.symbol == targetSymbol

    SciFiCard(borderColor = BorderCyan, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (currentLang == "bn") "পর্যায় সারণি ও পর্যায়বৃত্ত ধর্ম" else "Periodic Table & Periodic Law",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CyanNeon
            )
            Text(
                text = if (isTargetMatched) "TARGET LOCATED ✓" else "SEARCHING...",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isTargetMatched) EmeraldLight else AmberNeon
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Trend Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf(
                Pair(PeriodicTrend.ALL, if (currentLang == "bn") "সকল" else "All"),
                Pair(PeriodicTrend.ELECTRONEGATIVITY, if (currentLang == "bn") "তড়িৎ-ঋণাত্মকতা" else "Electroneg"),
                Pair(PeriodicTrend.RADIUS, if (currentLang == "bn") "ব্যাসার্ধ" else "Radius"),
                Pair(PeriodicTrend.VALENCE, if (currentLang == "bn") "যোজ্যতা e⁻" else "Valence e⁻")
            ).forEach { (tr, label) ->
                val isSel = selectedTrend == tr
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSel) CyanNeon.copy(alpha = 0.25f) else SurfaceCard)
                        .border(1.dp, if (isSel) CyanNeon else BorderSubtle, RoundedCornerShape(6.dp))
                        .clickable { selectedTrend = tr }
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isSel) CyanNeon else TextSecondary)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Scrollable Elements Row
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(elements) { el ->
                val isSel = el.symbol == selectedElement.symbol
                val isTarget = el.symbol == targetSymbol

                val badgeColor = when (selectedTrend) {
                    PeriodicTrend.ELECTRONEGATIVITY -> if (el.symbol in listOf("F", "O", "Cl", "N")) RoseNeon else CyanNeon
                    PeriodicTrend.RADIUS -> if (el.period == 4) AmberNeon else CyanNeon
                    PeriodicTrend.VALENCE -> if (el.valenceElectrons == 8 || (el.symbol == "He" && el.valenceElectrons == 2)) EmeraldNeon else VioletNeon
                    else -> if (el.category.contains("Metal")) CyanNeon else AmberNeon
                }

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) badgeColor.copy(alpha = 0.30f) else SurfaceNavy)
                        .border(1.5.dp, if (isSel) badgeColor else BorderSubtle, RoundedCornerShape(8.dp))
                        .clickable { selectedElement = el }
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${el.atomicNumber}", fontSize = 8.sp, color = TextTertiary, fontFamily = FontFamily.Monospace)
                        Text(el.symbol, fontSize = 13.sp, fontWeight = FontWeight.Black, color = if (isSel) badgeColor else TextPrimary)
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
            Column(modifier = Modifier.weight(1f)) {
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
                Text("Valence e⁻: ${selectedElement.valenceElectrons}", fontSize = 10.sp, color = AmberLight, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (isTargetMatched)
                (if (currentLang == "bn") "✓ এই মৌলটি বর্তমান স্তরের অনুসন্ধান লক্ষ্য পূরণ করে!" else "✓ Target element located! Meets the campaign level criteria.")
            else
                (if (currentLang == "bn") "অন্যান্য মৌল ট্যাপ করে পর্যায়বৃত্ত ধর্মের পরিবর্তন পর্যবেক্ষণ করুন।" else "Tap elements to explore electronic configuration, valency & periodic law."),
            fontSize = 11.sp,
            color = if (isTargetMatched) EmeraldLight else TextSecondary,
            fontWeight = if (isTargetMatched) FontWeight.Bold else FontWeight.Normal
        )
    }
}

// =====================================================================
// WORLD 3 — BONDING DIMENSION INTERACTIVE BONDING BENCH
// =====================================================================

@Composable
fun InteractiveBondingVisual(
    levelNumber: Int = 11,
    currentLang: String,
    modifier: Modifier = Modifier
) {
    var bondMode by remember(levelNumber) { mutableIntStateOf(if (levelNumber <= 13) 0 else 1) } // 0: Ionic, 1: Covalent
    var transferredElectrons by remember(levelNumber) { mutableIntStateOf(0) }
    var sharedPairs by remember(levelNumber) { mutableIntStateOf(1) }

    val isOctetSatisfied = if (bondMode == 0) transferredElectrons >= 1 else sharedPairs >= 2

    SciFiCard(borderColor = if (isOctetSatisfied) EmeraldNeon else BorderViolet, modifier = modifier) {
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
                TextButton(onClick = { bondMode = 0 }) {
                    Text("Ionic", color = if (bondMode == 0) CyanNeon else TextTertiary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
                TextButton(onClick = { bondMode = 1 }) {
                    Text("Covalent", color = if (bondMode == 1) EmeraldNeon else TextTertiary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Canvas Representation of Electron Transfer / Sharing
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
                if (bondMode == 0) {
                    // Ionic Transfer: Na (Metal) to Cl (Nonmetal)
                    val c1 = Offset(size.width * 0.28f, cy)
                    val c2 = Offset(size.width * 0.72f, cy)
                    drawCircle(color = CyanNeon, radius = 28f, center = c1, style = Stroke(2f))
                    drawCircle(color = VioletNeon, radius = 34f, center = c2, style = Stroke(2f))

                    // Transfer path
                    if (transferredElectrons > 0) {
                        drawLine(color = AmberNeon, start = Offset(c1.x + 30f, cy), end = Offset(c2.x - 36f, cy), strokeWidth = 2.5f)
                        drawCircle(color = AmberLight, radius = 5f, center = Offset(size.width * 0.50f, cy))
                    }
                } else {
                    // Covalent Sharing: Central O sharing with 2 H
                    val cO = Offset(size.width * 0.50f, cy)
                    val cH1 = Offset(size.width * 0.25f, cy)
                    val cH2 = Offset(size.width * 0.75f, cy)
                    drawCircle(color = RoseNeon, radius = 30f, center = cO, style = Stroke(2f))
                    drawCircle(color = CyanNeon, radius = 18f, center = cH1, style = Stroke(2f))
                    drawCircle(color = CyanNeon, radius = 18f, center = cH2, style = Stroke(2f))

                    // Shared electron pair dots
                    for (i in 0 until sharedPairs) {
                        val dy = if (i == 0) -5f else 5f
                        drawCircle(color = AmberNeon, radius = 4f, center = Offset((cO.x + cH1.x) / 2f, cy + dy))
                        drawCircle(color = AmberNeon, radius = 4f, center = Offset((cO.x + cH2.x) / 2f, cy + dy))
                    }
                }
            }

            if (bondMode == 0) {
                Row(modifier = Modifier.fillMaxWidth(0.85f), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(if (transferredElectrons > 0) "Na⁺ (+1)" else "Na (1 valence e⁻)", color = CyanNeon, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Text(if (transferredElectrons > 0) "⚡ e⁻ Transferred" else "➔ Transfer e⁻", color = AmberLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(if (transferredElectrons > 0) "Cl⁻ (8e⁻ Octet)" else "Cl (7 valence e⁻)", color = VioletNeon, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth(0.9f), horizontalArrangement = Arrangement.SpaceAround) {
                    Text("H (1s)", color = CyanNeon, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Text("Shared $sharedPairs Pairs", color = AmberLight, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Text("H (1s)", color = CyanNeon, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Interactive Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (bondMode == 0) {
                Button(
                    onClick = { transferredElectrons = if (transferredElectrons == 0) 1 else 0 },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = SpaceBlack),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (transferredElectrons == 0)
                            (if (currentLang == "bn") "ইলেকট্রন স্থানান্তর করুন (Na ➔ Cl)" else "Transfer Valence Electron (Na ➔ Cl)")
                        else
                            (if (currentLang == "bn") "পুনরায় সেট করুন" else "Reset Ion Transfer"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            } else {
                Button(
                    onClick = { sharedPairs = if (sharedPairs == 1) 2 else 1 },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon, contentColor = SpaceBlack),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (sharedPairs == 1)
                            (if (currentLang == "bn") "শেয়ার্ড ইলেকট্রন জোড় যোগ করুন" else "Share Electron Pair (Double Bond)")
                        else
                            (if (currentLang == "bn") "একক বন্ধনে ফিরে যান" else "Single Bond"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (bondMode == 0) {
                if (currentLang == "bn") "আয়নিক বন্ধনে সম্পূর্ণ ইলেকট্রন স্থানান্তরের মাধ্যমে বিপরীত আধানের আয়ন তৈরি হয় যা কুলম্বীয় আকর্ষণে আবদ্ধ থাকে।"
                else "In ionic bonding, complete valence electron transfer creates oppositely charged ions bound by strong Coulombic forces."
            } else {
                if (currentLang == "bn") "সমযোজী বন্ধনে পরমাণুসমূহ পারস্পরিক ইলেকট্রন জোড় শেয়ার করে স্থিতিশীল অষ্টক বা দ্বিত্ব নিয়ম পূরণ করে।"
                else "In covalent bonding, atoms mutually share localized electron pairs satisfying the octet/duet rule without full charge separation."
            },
            fontSize = 11.sp,
            color = TextSecondary
        )
    }
}

// =====================================================================
// WORLD 4 — FORMULA FORGE IONIC CHARGE-BALANCE COMPILER
// =====================================================================

@Composable
fun InteractiveFormulaForgeVisual(
    levelNumber: Int = 16,
    currentLang: String,
    modifier: Modifier = Modifier
) {
    val cations = remember {
        listOf(
            Triple("Na⁺", 1, false),
            Triple("Ca²⁺", 2, false),
            Triple("Al³⁺", 3, false),
            Triple("NH₄⁺", 1, true),
            Triple("Fe³⁺", 3, false)
        )
    }
    val anions = remember {
        listOf(
            Triple("Cl⁻", -1, false),
            Triple("O²⁻", -2, false),
            Triple("SO₄²⁻", -2, true),
            Triple("CO₃²⁻", -2, true),
            Triple("PO₄³⁻", -3, true)
        )
    }

    var selectedCationIndex by remember(levelNumber) {
        val idx = when (levelNumber) {
            16 -> 0 // Na+
            17 -> 1 // Ca2+
            18 -> 2 // Al3+
            19 -> 3 // NH4+
            else -> 2 // Al3+
        }
        mutableIntStateOf(idx)
    }
    var selectedAnionIndex by remember(levelNumber) {
        val idx = when (levelNumber) {
            16 -> 0 // Cl-
            17 -> 0 // Cl-
            18 -> 1 // O2-
            19 -> 2 // SO4 2-
            else -> 4 // PO4 3-
        }
        mutableIntStateOf(idx)
    }

    val cation = cations[selectedCationIndex]
    val anion = anions[selectedAnionIndex]

    val (catCount, anCount) = remember(cation, anion) {
        ChemistryEngine.calculateNeutralRatio(cation.second, anion.second)
    }

    val formula = remember(cation, anion) {
        val catClean = cation.first.replace(Regex("[⁺²³¹]"), "")
        val anClean = anion.first.replace(Regex("[⁻²³¹]"), "")
        ChemistryEngine.formatIonicFormula(
            cationSymbol = catClean,
            cationCharge = cation.second,
            isCationPolyatomic = cation.third,
            anionSymbol = anClean,
            anionCharge = anion.second,
            isAnionPolyatomic = anion.third
        )
    }

    val totalPositiveCharge = catCount * cation.second
    val totalNegativeCharge = anCount * abs(anion.second)
    val isNeutral = totalPositiveCharge == totalNegativeCharge

    SciFiCard(borderColor = CyanNeon, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (currentLang == "bn") "সংকেত কামারশালা — চার্জ ব্যালান্স" else "Formula Forge — Charge Balance",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CyanNeon
            )
            Text(
                text = if (isNeutral) "NET CHARGE = 0 ✓" else "UNBALANCED",
                color = if (isNeutral) EmeraldLight else RoseNeon,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Cations selector
        Text(
            text = if (currentLang == "bn") "ক্যাটায়ন নির্বাচন (ধনাত্মক):" else "Select Cation (Positive):",
            fontSize = 10.sp,
            color = TextSecondary
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            cations.forEachIndexed { idx, item ->
                val isSelected = idx == selectedCationIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) CyanNeon else SurfaceCard)
                        .clickable { selectedCationIndex = idx }
                        .padding(vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.first,
                        color = if (isSelected) SpaceNavy else CyanNeon,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Anions selector
        Text(
            text = if (currentLang == "bn") "অ্যানায়ন নির্বাচন (ঋণাত্মক):" else "Select Anion (Negative):",
            fontSize = 10.sp,
            color = TextSecondary
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            anions.forEachIndexed { idx, item ->
                val isSelected = idx == selectedAnionIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) VioletNeon else SurfaceCard)
                        .clickable { selectedAnionIndex = idx }
                        .padding(vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.first,
                        color = if (isSelected) SpaceNavy else VioletNeon,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Resulting neutral compound display
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceNavy)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (currentLang == "bn") "তড়িৎ-নিরপেক্ষ রাসায়নিক সংকেত:" else "Neutral Empirical Formula:",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
                Text(
                    text = formula,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = AmberLight,
                    fontFamily = FontFamily.Monospace
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Ratio: ${catCount} : ${anCount}",
                    color = EmeraldNeon,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "(+$totalPositiveCharge) + (-$totalNegativeCharge) = 0",
                    color = TextTertiary,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (currentLang == "bn")
                "যৌগের সামগ্রিক চার্জ শূন্য হতে হবে। চার্জের অনুপাত অনুযায়ী ক্যাটায়ন ও অ্যানায়ন সর্বনিম্ন পূর্ণসংখ্যায় যুক্ত হয়।"
            else
                "An ionic compound must achieve electrostatic neutrality. Subscripts represent the smallest whole-number ratio cancelling net charges.",
            fontSize = 11.sp,
            color = TextSecondary
        )
    }
}

// =====================================================================
// WORLD 5 — REACTION REACTOR STOICHIOMETRIC BALANCING LEDGER
// =====================================================================

data class ReactionDef(
    val nameEn: String,
    val nameBn: String,
    val r1: String, val r1Atoms: Map<String, Int>,
    val r2: String, val r2Atoms: Map<String, Int>,
    val p1: String, val p1Atoms: Map<String, Int>,
    val p2: String? = null, val p2Atoms: Map<String, Int>? = null,
    val targetC1: Int, val targetC2: Int, val targetC3: Int, val targetC4: Int = 1
)

@Composable
fun InteractiveReactionReactorVisual(
    levelNumber: Int = 21,
    currentLang: String,
    modifier: Modifier = Modifier
) {
    val reaction = remember(levelNumber) {
        when (levelNumber) {
            21 -> ReactionDef("Water Synthesis", "পানি উৎপাদন", "H₂", mapOf("H" to 2), "O₂", mapOf("O" to 2), "H₂O", mapOf("H" to 2, "O" to 1), null, null, 2, 1, 2)
            22 -> ReactionDef("Haber Ammonia Synthesis", "হেবার অ্যামোনিয়া সংশ্লেষণ", "N₂", mapOf("N" to 2), "H₂", mapOf("H" to 2), "NH₃", mapOf("N" to 1, "H" to 3), null, null, 1, 3, 2)
            23 -> ReactionDef("Methane Combustion", "মিথেন দহন", "CH₄", mapOf("C" to 1, "H" to 4), "O₂", mapOf("O" to 2), "CO₂", mapOf("C" to 1, "O" to 2), "H₂O", mapOf("H" to 2, "O" to 1), 1, 2, 1, 2)
            24 -> ReactionDef("Aluminium Acid Reaction", "অ্যালুমিনিয়াম-অ্যাসিড বিক্রিয়া", "Al", mapOf("Al" to 1), "HCl", mapOf("H" to 1, "Cl" to 1), "AlCl₃", mapOf("Al" to 1, "Cl" to 3), "H₂", mapOf("H" to 2), 2, 6, 2, 3)
            else -> ReactionDef("Iron Oxidation (Rust)", "লৌহের জারণ", "Fe", mapOf("Fe" to 1), "O₂", mapOf("O" to 2), "Fe₂O₃", mapOf("Fe" to 2, "O" to 3), null, null, 4, 3, 2)
        }
    }

    var c1 by remember(levelNumber) { mutableIntStateOf(1) }
    var c2 by remember(levelNumber) { mutableIntStateOf(1) }
    var c3 by remember(levelNumber) { mutableIntStateOf(1) }
    var c4 by remember(levelNumber) { mutableIntStateOf(1) }

    // Calculate atom ledger
    val reactantLedger = mutableMapOf<String, Int>()
    reaction.r1Atoms.forEach { (elem, cnt) -> reactantLedger[elem] = (reactantLedger[elem] ?: 0) + (cnt * c1) }
    reaction.r2Atoms.forEach { (elem, cnt) -> reactantLedger[elem] = (reactantLedger[elem] ?: 0) + (cnt * c2) }

    val productLedger = mutableMapOf<String, Int>()
    reaction.p1Atoms.forEach { (elem, cnt) -> productLedger[elem] = (productLedger[elem] ?: 0) + (cnt * c3) }
    reaction.p2Atoms?.forEach { (elem, cnt) -> productLedger[elem] = (productLedger[elem] ?: 0) + (cnt * c4) }

    val allElements = (reactantLedger.keys + productLedger.keys).distinct()
    val isBalanced = allElements.all { (reactantLedger[it] ?: 0) == (productLedger[it] ?: 0) }

    SciFiCard(borderColor = if (isBalanced) EmeraldNeon else RoseNeon, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (currentLang == "bn") "বিক্রিয়া চুল্লি — সমতাকরণ লেজার" else "Reaction Reactor — Balancing Ledger",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isBalanced) EmeraldNeon else CyanNeon
                )
                Text(
                    text = if (currentLang == "bn") reaction.nameBn else reaction.nameEn,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberLight
                )
            }
            Text(
                text = if (isBalanced) "BALANCED ✓" else "UNBALANCED",
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
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoefficientStepper(coef = c1, onInc = { if (c1 < 6) c1++ }, onDec = { if (c1 > 1) c1-- })
            Text(" ${reaction.r1} + ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanNeon, fontFamily = FontFamily.Monospace)
            CoefficientStepper(coef = c2, onInc = { if (c2 < 6) c2++ }, onDec = { if (c2 > 1) c2-- })
            Text(" ${reaction.r2} ➔ ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AmberLight, fontFamily = FontFamily.Monospace)
            CoefficientStepper(coef = c3, onInc = { if (c3 < 6) c3++ }, onDec = { if (c3 > 1) c3-- })
            Text(" ${reaction.p1}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldLight, fontFamily = FontFamily.Monospace)
            if (reaction.p2 != null) {
                Text(" + ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AmberLight)
                CoefficientStepper(coef = c4, onInc = { if (c4 < 6) c4++ }, onDec = { if (c4 > 1) c4-- })
                Text(" ${reaction.p2}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldLight, fontFamily = FontFamily.Monospace)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Atom Count Comparison Table
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceCard)
                .padding(8.dp)
        ) {
            allElements.forEach { el ->
                val rCnt = reactantLedger[el] ?: 0
                val pCnt = productLedger[el] ?: 0
                val elMatch = rCnt == pCnt
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Atom $el:", color = CyanNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("Reactants: $rCnt", color = if (elMatch) EmeraldNeon else AmberNeon, fontSize = 11.sp)
                    Text("vs", color = TextTertiary, fontSize = 10.sp)
                    Text("Products: $pCnt", color = if (elMatch) EmeraldNeon else RoseNeon, fontSize = 11.sp)
                    Text(if (elMatch) "✓ Balanced" else "✗ Mismatch", color = if (elMatch) EmeraldLight else RoseLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (isBalanced)
                (if (currentLang == "bn") "✓ ভর সংরক্ষণ সূত্র নিশ্চিত: বিক্রিয়ার উভয় পাশে প্রতিটি মৌলের পরমাণু সংখ্যা সমান।" else "✓ Law of Conservation of Mass satisfied: atom count on both sides is perfectly conserved.")
            else
                (if (currentLang == "bn") "সহগ (Coefficients) সমন্বয় করে পরমাণুর সংখ্যা উভয় পাশে সমান করুন।" else "Adjust integer coefficients until every element matches across reactant and product sides."),
            fontSize = 11.sp,
            color = if (isBalanced) EmeraldLight else TextSecondary,
            fontWeight = if (isBalanced) FontWeight.Bold else FontWeight.Normal
        )
    }
}

// =====================================================================
// WORLD 6 — MOLE MATRIX AVOGADRO CONVERTER
// =====================================================================

data class MoleSubstance(val formula: String, val nameEn: String, val nameBn: String, val molarMass: Float)

@Composable
fun InteractiveMoleMatrixVisual(
    levelNumber: Int = 26,
    currentLang: String,
    modifier: Modifier = Modifier
) {
    val substances = remember {
        listOf(
            MoleSubstance("H₂O", "Water", "পানি", 18.02f),
            MoleSubstance("NaCl", "Table Salt", "সোডিয়াম ক্লোরাইড", 58.44f),
            MoleSubstance("C₆H₁₂O₆", "Glucose", "গ্লুকোজ", 180.16f),
            MoleSubstance("CaCO₃", "Calcium Carbonate", "ক্যালসিয়াম কার্বনেট", 100.09f),
            MoleSubstance("C₈H₉NO₂", "Paracetamol", "প্যারাসিটামল", 151.16f)
        )
    }

    var selectedSubstanceIndex by remember(levelNumber) {
        val idx = when (levelNumber) {
            26 -> 0
            27 -> 1
            28 -> 2
            29 -> 3
            else -> 4
        }
        mutableIntStateOf(idx)
    }

    val substance = substances[selectedSubstanceIndex]
    var grams by remember(selectedSubstanceIndex) { mutableFloatStateOf(substance.molarMass * 2f) }

    val moles = grams / substance.molarMass
    val particles = moles * 6.022f

    SciFiCard(borderColor = BorderCyan, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (currentLang == "bn") "মোল রূপান্তর ও অ্যাভোগাড্রো গণনা" else "Mole Conversion & Avogadro Scale",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CyanNeon
            )
            Text(
                text = "M = ${substance.molarMass} g/mol",
                fontSize = 11.sp,
                color = AmberNeon,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Substance Selector Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            substances.forEachIndexed { idx, s ->
                val isSel = idx == selectedSubstanceIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSel) CyanNeon else SurfaceCard)
                        .clickable { selectedSubstanceIndex = idx }
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(s.formula, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) SpaceBlack else CyanNeon)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Mass Slider
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Mass m: ${grams.toInt()} g", color = AmberLight, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(95.dp))
            Slider(
                value = grams,
                onValueChange = { grams = it },
                valueRange = (substance.molarMass * 0.5f)..(substance.molarMass * 5f),
                steps = 18,
                modifier = Modifier.weight(1f)
            )
        }

        // Conversion calculation panel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceNavy)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (currentLang == "bn") "ভর (m)" else "Mass (m)", fontSize = 10.sp, color = TextTertiary)
                Text("${grams.toInt()} g", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AmberLight)
            }
            Text("÷", fontSize = 14.sp, color = TextSecondary)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (currentLang == "bn") "মোলার ভর (M)" else "Molar Mass", fontSize = 10.sp, color = TextTertiary)
                Text("${substance.molarMass} g/mol", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanNeon)
            }
            Text("=", fontSize = 14.sp, color = TextSecondary)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (currentLang == "bn") "মোল (n)" else "Moles (n)", fontSize = 10.sp, color = TextTertiary)
                Text(String.format("%.2f mol", moles), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldNeon)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Total Particles N = n × Nₐ = ${String.format("%.2f", particles)} × 10²³ ${substance.formula} molecules",
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = CyanGlow,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// =====================================================================
// WORLD 7 — pH DIMENSION SCALE & INDICATOR SIMULATOR
// =====================================================================

enum class IndicatorChoice { UNIVERSAL, LITMUS, PHENOLPHTHALEIN, METHYL_ORANGE }

@Composable
fun InteractivePHScaleVisual(
    levelNumber: Int = 31,
    currentLang: String,
    modifier: Modifier = Modifier
) {
    var phValue by remember(levelNumber) {
        val initial = when (levelNumber) {
            31 -> 7.0f
            32 -> 1.5f // Gastric
            33 -> 7.4f // Blood
            34 -> 10.5f // Antacid
            else -> 8.4f
        }
        mutableFloatStateOf(initial)
    }

    var indicator by remember { mutableStateOf(IndicatorChoice.UNIVERSAL) }

    val indicatorColor = remember(phValue, indicator) {
        when (indicator) {
            IndicatorChoice.UNIVERSAL -> when {
                phValue < 3.0f -> Color(0xFFEF4444)
                phValue < 6.0f -> Color(0xFFF59E0B)
                phValue < 8.0f -> Color(0xFF10B981)
                phValue < 11.0f -> Color(0xFF00E5FF)
                else -> Color(0xFF8B5CF6)
            }
            IndicatorChoice.LITMUS -> if (phValue < 7.0f) Color(0xFFEF4444) else Color(0xFF3B82F6)
            IndicatorChoice.PHENOLPHTHALEIN -> if (phValue < 8.2f) Color(0xFFCBD5E1).copy(alpha = 0.5f) else Color(0xFFEC4899)
            IndicatorChoice.METHYL_ORANGE -> if (phValue < 3.1f) Color(0xFFDC2626) else if (phValue < 4.4f) Color(0xFFF97316) else Color(0xFFFACC15)
        }
    }

    val sampleFluid = remember(phValue) {
        when {
            phValue < 2.0f -> if (currentLang == "bn") "পাকস্থলীর রস (HCl, pH ~1.5)" else "Gastric Acid (HCl, pH ~1.5)"
            phValue < 4.0f -> if (currentLang == "bn") "কমলার রস / ভিনেগার (pH ~3.0)" else "Vinegar / Citrus (pH ~3.0)"
            phValue < 6.5f -> if (currentLang == "bn") "কালো কফি / মূত্র (pH ~5.5-6.0)" else "Coffee / Urine (pH ~6.0)"
            phValue < 7.5f -> if (currentLang == "bn") "বিশুদ্ধ পানি / মানব রক্ত (pH 7.4)" else "Pure Water / Blood (pH 7.4)"
            phValue < 9.0f -> if (currentLang == "bn") "সোডিয়াম বাইকার্বনেট (pH ~8.4)" else "Sodium Bicarbonate (pH ~8.4)"
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
                text = if (currentLang == "bn") "pH স্কেল ও নির্দেশক বর্ণালী" else "pH Scale & Indicator Simulator",
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

        // Indicator Selector Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf(
                Pair(IndicatorChoice.UNIVERSAL, "Universal"),
                Pair(IndicatorChoice.PHENOLPHTHALEIN, "Phenolpth"),
                Pair(IndicatorChoice.LITMUS, "Litmus"),
                Pair(IndicatorChoice.METHYL_ORANGE, "Methyl Org")
            ).forEach { (ind, label) ->
                val isSel = indicator == ind
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSel) indicatorColor.copy(alpha = 0.3f) else SurfaceCard)
                        .border(1.dp, if (isSel) indicatorColor else BorderSubtle, RoundedCornerShape(6.dp))
                        .clickable { indicator = ind }
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isSel) TextPrimary else TextSecondary)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Color Bar Preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFEF4444),
                            Color(0xFFF59E0B),
                            Color(0xFF10B981),
                            Color(0xFF00E5FF),
                            Color(0xFF8B5CF6)
                        )
                    )
                )
        )

        Slider(
            value = phValue,
            onValueChange = { phValue = it },
            valueRange = 0f..14f,
            steps = 140,
            modifier = Modifier.fillMaxWidth()
        )

        // Readout Details
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceNavy)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(sampleFluid, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            val pOH = 14.0f - phValue
            Text(String.format("pOH = %.1f • [H⁺]=10^-(%.1f)", pOH, phValue), color = TextSecondary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        }
    }
}

// =====================================================================
// WORLD 8 — SOLUTION LABORATORY DILUTION BENCH
// =====================================================================

@Composable
fun InteractiveSolutionLabVisual(
    levelNumber: Int = 36,
    currentLang: String,
    modifier: Modifier = Modifier
) {
    var stockC1 by remember(levelNumber) { mutableFloatStateOf(2.0f) }
    var stockV1 by remember(levelNumber) { mutableFloatStateOf(25f) } // mL
    var addedWater by remember(levelNumber) { mutableFloatStateOf(75f) } // mL

    val totalV2 = stockV1 + addedWater
    val finalC2 = (stockC1 * stockV1) / totalV2
    val totalMolesMmol = stockC1 * stockV1 // mmol

    SciFiCard(borderColor = BorderCyan, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (currentLang == "bn") "লঘুকরণ সূত্র: C₁V₁ = C₂V₂" else "Dilution Bench: C₁V₁ = C₂V₂",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CyanNeon
            )
            Text(
                text = "Solute = ${totalMolesMmol.toInt()} mmol",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldNeon,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Stock V1 slider
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Stock Vol V₁: ${stockV1.toInt()} mL", color = AmberLight, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(130.dp))
            Slider(
                value = stockV1,
                onValueChange = { stockV1 = it },
                valueRange = 10f..100f,
                steps = 9,
                modifier = Modifier.weight(1f)
            )
        }

        // Added Water Slider
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("+ Solvent: ${addedWater.toInt()} mL", color = CyanNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(130.dp))
            Slider(
                value = addedWater,
                onValueChange = { addedWater = it },
                valueRange = 0f..200f,
                steps = 20,
                modifier = Modifier.weight(1f)
            )
        }

        // Calculation readout
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
                Text("$stockC1 M", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanNeon)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Stock V₁", fontSize = 10.sp, color = TextTertiary)
                Text("${stockV1.toInt()} mL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AmberLight)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Total V₂", fontSize = 10.sp, color = TextTertiary)
                Text("${totalV2.toInt()} mL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VioletNeon)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Result C₂", fontSize = 10.sp, color = TextTertiary)
                Text(String.format("%.2f M", finalC2), fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = EmeraldNeon)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (currentLang == "bn")
                "দ্রাবক যোগ করলে দ্রবণের ঘনমাত্রা হ্রাস পায়, কিন্তু মোট দ্রবের মোল সংখ্যা (n = C × V) অপরিবর্তিত থাকে।"
            else
                "Adding solvent increases total volume and lowers concentration, but total solute moles remain strictly conserved.",
            fontSize = 11.sp,
            color = TextSecondary
        )
    }
}

// =====================================================================
// WORLD 9 — ORGANIC PHARMACOPHORE 2D STRUCTURE INSPECTOR
// =====================================================================

data class PharmaDrug(
    val nameEn: String,
    val nameBn: String,
    val formula: String,
    val groups: List<String>,
    val descriptionEn: String,
    val descriptionBn: String
)

@Composable
fun InteractiveOrganicForestVisual(
    levelNumber: Int = 41,
    currentLang: String,
    modifier: Modifier = Modifier
) {
    val drugs = remember {
        listOf(
            PharmaDrug("Aspirin", "অ্যাসপিরিন", "C₉H₈O₄", listOf("COOH", "OCOCH3", "Aromatic"), "Contains carboxylic acid & acetyl ester groups. Irreversibly inhibits COX-1.", "কার্বক্সিলিক অ্যাসিড ও এসিটাইল এস্টার মূলক বিদ্যমান।"),
            PharmaDrug("Paracetamol", "প্যারাসিটামল", "C₈H₉NO₂", listOf("OH", "NHCOCH3", "Aromatic"), "Contains phenolic -OH and acetamide groups. Analgesic and antipyretic.", "ফেনোলিক -OH ও এসিটামাইড মূলক থাকে। বেদনানাশক ও জ্বরনাশক।"),
            PharmaDrug("Ibuprofen", "আইবুপ্রোফেন", "C₁₃H₁₈O₂", listOf("COOH", "Isobutyl", "Aromatic"), "Propionic acid derivative NSAID. Inhibits prostaglandin synthesis.", "প্রোপিওনিক অ্যাসিড ডেরিভেটিভ বেদনানাশক।"),
            PharmaDrug("Dopamine", "ডোপামিন", "C₈H₁₁NO₂", listOf("OH", "NH2", "Aromatic"), "Catecholamine with 2 phenolic -OH and primary amine -NH₂.", "ক্যাটেকোলামাইন নিউরোট্রান্সমিটার जिसमें ২-OH ও -NH₂ মূলক আছে।")
        )
    }

    var selectedDrugIndex by remember(levelNumber) {
        val idx = when (levelNumber) {
            41 -> 0
            42 -> 1
            43 -> 2
            else -> 3
        }
        mutableIntStateOf(idx)
    }

    var highlightedGroup by remember { mutableStateOf("COOH") }
    val drug = drugs[selectedDrugIndex]

    SciFiCard(borderColor = EmeraldNeon, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (currentLang == "bn") "জৈব কার্যকরী মূলক ফার্মাকোফোর" else "Organic Pharmacophore Inspector",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldNeon
            )
            Text(
                text = "${drug.formula}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AmberNeon,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Drug Selector Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            drugs.forEachIndexed { idx, d ->
                val isSel = idx == selectedDrugIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSel) EmeraldNeon else SurfaceCard)
                        .clickable {
                            selectedDrugIndex = idx
                            highlightedGroup = d.groups.first()
                        }
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(d.nameEn, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) SpaceBlack else TextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2D Molecular Ring Drawing Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceNavy),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val r = 32f

                // Draw Benzene Ring
                val pts = (0 until 6).map { i ->
                    val angle = (i * 60f - 30f) * (Math.PI / 180f)
                    Offset(cx + (r * cos(angle)).toFloat(), cy + (r * sin(angle)).toFloat())
                }
                for (i in 0 until 6) {
                    val next = (i + 1) % 6
                    drawLine(color = EmeraldNeon, start = pts[i], end = pts[next], strokeWidth = 2.5f)
                }
                // Inner Delocalized Circle
                drawCircle(color = EmeraldNeon.copy(alpha = 0.5f), radius = r * 0.55f, center = Offset(cx, cy), style = Stroke(1.5f))

                // Substituent Lines
                drawLine(color = AmberNeon, start = pts[0], end = Offset(pts[0].x + 35f, pts[0].y - 20f), strokeWidth = 2.5f)
                drawLine(color = CyanNeon, start = pts[1], end = Offset(pts[1].x + 35f, pts[1].y + 20f), strokeWidth = 2.5f)
            }

            Text(
                text = "${drug.nameEn} Core",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Functional Groups in Drug
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            drug.groups.forEach { grp ->
                val isSel = highlightedGroup == grp
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) EmeraldNeon.copy(alpha = 0.25f) else SurfaceCard)
                        .border(1.dp, if (isSel) EmeraldNeon else BorderSubtle, RoundedCornerShape(8.dp))
                        .clickable { highlightedGroup = grp }
                        .padding(vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("-$grp", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) EmeraldNeon else TextSecondary, fontFamily = FontFamily.Monospace)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (currentLang == "bn") drug.descriptionBn else drug.descriptionEn,
            color = TextSecondary,
            fontSize = 11.sp,
            lineHeight = 15.sp
        )
    }
}

// =====================================================================
// WORLD 10 — PHARMACEUTICAL CRYSTAL VAULT BUFFER & TONICITY SIMULATOR
// =====================================================================

@Composable
fun InteractivePharmaVaultVisual(
    levelNumber: Int = 46,
    currentLang: String,
    modifier: Modifier = Modifier
) {
    var mode by remember(levelNumber) { mutableIntStateOf(if (levelNumber <= 48) 0 else 1) } // 0: Buffer, 1: Saline Tonicity
    var acidHa by remember { mutableFloatStateOf(0.10f) } // M
    var baseA by remember { mutableFloatStateOf(0.10f) }  // M
    val pKa = 4.76f // Acetate buffer
    val bufferPh = pKa + log10((baseA / acidHa).toDouble()).toFloat()

    var naclPercent by remember { mutableFloatStateOf(0.9f) } // % w/v
    val osmolarity = (naclPercent * 10f * 2f * 1000f) / 58.44f // mOsm/L

    SciFiCard(borderColor = AmberNeon, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (currentLang == "bn") "ফার্মাসিউটিক্যাল বাফার ও স্যালাইন" else "Pharma Buffer & Isotonic Vault",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AmberNeon
            )
            Row {
                TextButton(onClick = { mode = 0 }) {
                    Text("Buffer", color = if (mode == 0) CyanNeon else TextTertiary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
                TextButton(onClick = { mode = 1 }) {
                    Text("Tonicity", color = if (mode == 1) EmeraldNeon else TextTertiary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (mode == 0) {
            // Buffer simulator (Henderson-Hasselbalch)
            Text(
                text = "pH = pKa + log([A⁻] / [HA])",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CyanNeon
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Base [A⁻]: ${String.format("%.2f", baseA)} M", color = AmberLight, fontSize = 10.sp, modifier = Modifier.width(110.dp))
                Slider(value = baseA, onValueChange = { baseA = it }, valueRange = 0.02f..0.50f, steps = 24, modifier = Modifier.weight(1f))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Acid [HA]: ${String.format("%.2f", acidHa)} M", color = VioletNeon, fontSize = 10.sp, modifier = Modifier.width(110.dp))
                Slider(value = acidHa, onValueChange = { acidHa = it }, valueRange = 0.02f..0.50f, steps = 24, modifier = Modifier.weight(1f))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceNavy)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("pKa = 4.76 (Acetate)", color = TextSecondary, fontSize = 11.sp)
                Text(String.format("Buffer pH = %.2f", bufferPh), color = EmeraldNeon, fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
            }
        } else {
            // Saline tonicity simulator
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("NaCl %: ${String.format("%.1f", naclPercent)}% w/v", color = AmberLight, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(110.dp))
                Slider(value = naclPercent, onValueChange = { naclPercent = it }, valueRange = 0.2f..2.0f, steps = 18, modifier = Modifier.weight(1f))
            }

            val tonicityStatus = when {
                osmolarity < 280f -> if (currentLang == "bn") "হাইপোটনিক (লোহিত রক্তকণিকা স্ফীত হয়ে ফেটে যাওয়ার ঝুঁকি)" else "Hypotonic (RBC swelling / hemolysis danger)"
                osmolarity <= 320f -> if (currentLang == "bn") "আইসোটনিক সম-অভিস্রবণিক (~৩০০ mOsm/L, RBC নিরাপদ)" else "Isotonic Normal Saline (~308 mOsm/L, Safe for RBC)"
                else -> if (currentLang == "bn") "হাইপারটনিক (রক্তকণিকা সংকুচিত / ক্রিলেশন)" else "Hypertonic (RBC crenation / shriveling)"
            }

            val tonicityColor = if (osmolarity in 280f..320f) EmeraldNeon else RoseNeon

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceNavy)
                    .padding(8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(String.format("Osmolarity: %.0f mOsm/L", osmolarity), color = CyanNeon, fontWeight = FontWeight.Bold, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    Text(if (osmolarity in 280f..320f) "0.9% NORMAL SALINE" else "NON-ISOTONIC", color = tonicityColor, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(tonicityStatus, color = tonicityColor, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (currentLang == "bn")
                "০.৯% সোডিয়াম ক্লোরাইড দ্রবণ রক্তের প্লাজমার সম-অভিস্রবণিক হওয়ায় এটি রক্তনালীতে ইনফিউশনের জন্য আদর্শ (BP106T)।"
            else
                "0.9% w/v NaCl is isotonic with human blood plasma (~300 mOsm/L), preventing red blood cell hemolysis or crenation.",
            fontSize = 11.sp,
            color = TextSecondary
        )
    }
}

// =====================================================================
// HELPER STEPPERS
// =====================================================================

@Composable
private fun ParticleAdjuster(
    label: String,
    count: Int,
    color: Color,
    onInc: () -> Unit,
    onDec: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = color, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDec, modifier = Modifier.size(26.dp)) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = color, modifier = Modifier.size(15.dp))
            }
            Text("$count", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp, modifier = Modifier.width(20.dp), textAlign = TextAlign.Center)
            IconButton(onClick = onInc, modifier = Modifier.size(26.dp)) {
                Icon(Icons.Default.Add, contentDescription = "Increase", tint = color, modifier = Modifier.size(15.dp))
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
            .padding(horizontal = 2.dp, vertical = 1.dp)
    ) {
        IconButton(onClick = onDec, modifier = Modifier.size(20.dp)) {
            Icon(Icons.Default.Remove, contentDescription = "Dec", tint = CyanNeon, modifier = Modifier.size(12.dp))
        }
        Text("$coef", fontWeight = FontWeight.Bold, color = AmberLight, fontSize = 12.sp, modifier = Modifier.width(14.dp), textAlign = TextAlign.Center)
        IconButton(onClick = onInc, modifier = Modifier.size(20.dp)) {
            Icon(Icons.Default.Add, contentDescription = "Inc", tint = CyanNeon, modifier = Modifier.size(12.dp))
        }
    }
}
