package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Strings
import com.example.model.MemoryGameMode
import com.example.ui.components.NeonButton
import com.example.ui.components.SciFiCard
import com.example.ui.components.SciFiTopBar
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun MemoryGameScreen(
    currentLang: String,
    score: Int,
    onToggleLang: () -> Unit,
    onAddScore: (Int) -> Unit,
    onBack: () -> Unit
) {
    var selectedMode by remember { mutableStateOf(MemoryGameMode.ECHO_MAP) }
    var inGame by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SciFiTopBar(
                title = Strings.memoryHubTitle(currentLang),
                score = score,
                currentLang = currentLang,
                onToggleLang = onToggleLang,
                onBack = onBack,
                testTag = "memory_top_bar"
            )
        },
        containerColor = SpaceBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (!inGame) {
                // Mode Selector View
                Text(
                    text = if (currentLang == "bn") "৫টি মৌলিক স্মৃতি প্রশিক্ষণ মোড" else "5 Core Memory Training Modes",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletNeon
                )
                Text(
                    text = if (currentLang == "bn")
                        "রসায়নের জ্ঞান ও মস্তিষ্কের কার্যক্ষমতা বৃদ্ধির জন্য একটি মোড বেছে নিন:"
                    else
                        "Select a specialized mode to train cognitive recall and chemistry retention:",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(4.dp))

                for (mode in MemoryGameMode.values()) {
                    val isSelected = selectedMode == mode
                    SciFiCard(
                        borderColor = if (isSelected) VioletNeon else BorderSubtle,
                        modifier = Modifier
                            .clickable { selectedMode = mode }
                            .testTag("mode_${mode.name}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) VioletDeep else SurfaceNavy),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = mode.name,
                                    tint = if (isSelected) TextPrimary else VioletNeon,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (currentLang == "bn") mode.titleBn else mode.titleEn,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isSelected) TextPrimary else TextSecondary
                                )
                                Text(
                                    text = if (currentLang == "bn") mode.descBn else mode.descEn,
                                    fontSize = 11.sp,
                                    color = TextTertiary,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                NeonButton(
                    text = if (currentLang == "bn") "নির্বাচিত মোড শুরু করুন" else "Enter ${selectedMode.titleEn}",
                    onClick = { inGame = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    color = VioletNeon,
                    testTag = "start_selected_memory_mode_btn"
                )
            } else {
                // Active Game Render based on Mode
                when (selectedMode) {
                    MemoryGameMode.ECHO_MAP -> {
                        EchoMapMiniGame(
                            currentLang = currentLang,
                            onComplete = { pts ->
                                onAddScore(pts)
                                inGame = false
                            },
                            onExit = { inGame = false }
                        )
                    }
                    MemoryGameMode.LINKWEAVER -> {
                        LinkweaverMiniGame(
                            currentLang = currentLang,
                            onComplete = { pts ->
                                onAddScore(pts)
                                inGame = false
                            },
                            onExit = { inGame = false }
                        )
                    }
                    MemoryGameMode.MEMORY_TRAP -> {
                        MemoryTrapMiniGame(
                            currentLang = currentLang,
                            onComplete = { pts ->
                                onAddScore(pts)
                                inGame = false
                            },
                            onExit = { inGame = false }
                        )
                    }
                    MemoryGameMode.TIMEFOLD -> {
                        TimefoldMiniGame(
                            currentLang = currentLang,
                            onComplete = { pts ->
                                onAddScore(pts)
                                inGame = false
                            },
                            onExit = { inGame = false }
                        )
                    }
                    MemoryGameMode.DELAYED_ECHO -> {
                        DelayedEchoMiniGame(
                            currentLang = currentLang,
                            onComplete = { pts ->
                                onAddScore(pts)
                                inGame = false
                            },
                            onExit = { inGame = false }
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// MODE A: ECHO MAP MINI-GAME
// -------------------------------------------------------------
@Composable
private fun EchoMapMiniGame(
    currentLang: String,
    onComplete: (Int) -> Unit,
    onExit: () -> Unit
) {
    val sequence = remember { listOf(0, 3, 1, 2) }
    var currentHighlight by remember { mutableIntStateOf(-1) }
    var isDemonstrating by remember { mutableStateOf(true) }
    val userTaps = remember { mutableStateListOf<Int>() }
    var roundFinished by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }

    // Demonstration animation
    LaunchedEffect(Unit) {
        delay(600)
        for (nodeIndex in sequence) {
            currentHighlight = nodeIndex
            delay(800)
            currentHighlight = -1
            delay(300)
        }
        isDemonstrating = false
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (currentLang == "bn") "মোড এ: প্রতিধ্বনি মানচিত্র (Echo Map)" else "Mode A: Echo Map",
            fontWeight = FontWeight.Bold,
            color = CyanNeon,
            fontSize = 16.sp
        )

        Text(
            text = if (isDemonstrating) {
                if (currentLang == "bn") "কক্ষপথের নোডগুলোর জ্বলন্ত ক্রমটি নিবিড়ভাবে পর্যবেক্ষণ করুন..." else "Observe the glowing sequence of orbital nodes..."
            } else {
                if (currentLang == "bn") "এখন একই ক্রমানুসারে নোডগুলো স্পর্শ করুন (${userTaps.size}/${sequence.size})" else "Now tap the orbital nodes in the exact order (${userTaps.size}/${sequence.size})"
            },
            fontSize = 13.sp,
            color = if (isDemonstrating) AmberLight else CyanGlow
        )

        // 4 Quantum Node Buttons arranged in a 2x2 constellation
        val nodeLabels = listOf("H (1)", "He (2)", "Li (3)", "Be (4)")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceNavy)
                .border(1.dp, BorderCyan, RoundedCornerShape(16.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuantumNode(0, nodeLabels[0], currentHighlight == 0, userTaps.contains(0)) {
                        if (!isDemonstrating && !roundFinished) handleTap(0, sequence, userTaps, onResult = { s -> success = s; roundFinished = true })
                    }
                    QuantumNode(1, nodeLabels[1], currentHighlight == 1, userTaps.contains(1)) {
                        if (!isDemonstrating && !roundFinished) handleTap(1, sequence, userTaps, onResult = { s -> success = s; roundFinished = true })
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuantumNode(2, nodeLabels[2], currentHighlight == 2, userTaps.contains(2)) {
                        if (!isDemonstrating && !roundFinished) handleTap(2, sequence, userTaps, onResult = { s -> success = s; roundFinished = true })
                    }
                    QuantumNode(3, nodeLabels[3], currentHighlight == 3, userTaps.contains(3)) {
                        if (!isDemonstrating && !roundFinished) handleTap(3, sequence, userTaps, onResult = { s -> success = s; roundFinished = true })
                    }
                }
            }
        }

        if (roundFinished) {
            SciFiCard(borderColor = if (success) EmeraldNeon else RoseNeon) {
                Text(
                    text = if (success) (if (currentLang == "bn") "চমৎকার স্মৃতি পুনর্গঠন! +১৫০ পয়েন্ট" else "Flawless Sequence Recall! +150 Points") else (if (currentLang == "bn") "ক্রমটি মেলেনি। আবার চেষ্টা করুন।" else "Sequence mismatch. Train again."),
                    color = if (success) EmeraldLight else RoseLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                NeonButton(
                    text = if (currentLang == "bn") "পরবর্তী মিশন" else "Finish Round",
                    onClick = { onComplete(if (success) 150 else 25) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun handleTap(
    nodeIndex: Int,
    sequence: List<Int>,
    userTaps: MutableList<Int>,
    onResult: (Boolean) -> Unit
) {
    userTaps.add(nodeIndex)
    val currentStep = userTaps.size - 1
    if (userTaps[currentStep] != sequence[currentStep]) {
        onResult(false)
        return
    }
    if (userTaps.size == sequence.size) {
        onResult(true)
    }
}

@Composable
private fun QuantumNode(
    index: Int,
    label: String,
    isHighlighted: Boolean,
    isTapped: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = when {
            isHighlighted -> CyanNeon
            isTapped -> VioletNeon
            else -> SurfaceCard
        },
        label = "node_color"
    )

    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(bgColor)
            .border(2.dp, if (isHighlighted) Color.White else BorderCyan, CircleShape)
            .clickable { onClick() }
            .testTag("node_$index"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isHighlighted || isTapped) SpaceBlack else TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
    }
}

// -------------------------------------------------------------
// MODE D: LINKWEAVER MINI-GAME (Mental Threads)
// -------------------------------------------------------------
@Composable
private fun LinkweaverMiniGame(
    currentLang: String,
    onComplete: (Int) -> Unit,
    onExit: () -> Unit
) {
    val pairs = remember {
        listOf(
            Pair("Na⁺", "+1 (Cation)"),
            Pair("SO₄²⁻", "-2 (Polyatomic Anion)"),
            Pair("Al³⁺", "+3 (Cation)"),
            Pair("Cl⁻", "-1 (Anion)")
        )
    }

    var selectedLeft by remember { mutableStateOf<String?>(null) }
    var selectedRight by remember { mutableStateOf<String?>(null) }
    val solvedLefts = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = if (currentLang == "bn") "মোড ডি: সংযোগ বয়ন (Linkweaver)" else "Mode D: Linkweaver Threads",
            fontWeight = FontWeight.Bold,
            color = VioletNeon,
            fontSize = 16.sp
        )

        Text(
            text = if (currentLang == "bn") "বাম কলামের আয়ন এবং ডান কলামের সঠিক চার্জ মিলিয়ে সংযোগ স্থাপন করুন:" else "Connect each chemical ion to its verified charge and classification:",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left Column (Ions)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for ((left, _) in pairs) {
                    val isSolved = solvedLefts.contains(left)
                    val isSelected = selectedLeft == left
                    SciFiCard(
                        borderColor = if (isSolved) EmeraldNeon else if (isSelected) CyanNeon else BorderSubtle,
                        modifier = Modifier
                            .clickable(enabled = !isSolved) {
                                selectedLeft = left
                                if (selectedRight != null) {
                                    val match = pairs.find { it.first == left && it.second == selectedRight }
                                    if (match != null) {
                                        solvedLefts.add(left)
                                        selectedLeft = null
                                        selectedRight = null
                                    } else {
                                        selectedLeft = null
                                        selectedRight = null
                                    }
                                }
                            }
                            .testTag("link_left_$left")
                    ) {
                        Text(
                            text = left,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = if (isSolved) EmeraldLight else if (isSelected) CyanNeon else TextPrimary
                        )
                    }
                }
            }

            // Right Column (Charges)
            Column(
                modifier = Modifier.weight(1.2f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val shuffledRight = remember { pairs.map { it.second }.shuffled() }
                for (right in shuffledRight) {
                    val matchingLeft = pairs.find { it.second == right }?.first
                    val isSolved = matchingLeft != null && solvedLefts.contains(matchingLeft)
                    val isSelected = selectedRight == right

                    SciFiCard(
                        borderColor = if (isSolved) EmeraldNeon else if (isSelected) VioletNeon else BorderSubtle,
                        modifier = Modifier
                            .clickable(enabled = !isSolved) {
                                selectedRight = right
                                if (selectedLeft != null) {
                                    val match = pairs.find { it.first == selectedLeft && it.second == right }
                                    if (match != null) {
                                        solvedLefts.add(selectedLeft!!)
                                        selectedLeft = null
                                        selectedRight = null
                                    } else {
                                        selectedLeft = null
                                        selectedRight = null
                                    }
                                }
                            }
                            .testTag("link_right_$right")
                    ) {
                        Text(
                            text = right,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSolved) EmeraldLight else if (isSelected) VioletNeon else TextPrimary
                        )
                    }
                }
            }
        }

        if (solvedLefts.size == pairs.size) {
            SciFiCard(borderColor = EmeraldNeon) {
                Text(
                    text = if (currentLang == "bn") "সবগুলো সংযোগ সফলভাবে যুক্ত হয়েছে! +২০০ পয়েন্ট" else "All Chemical Threads Successfully Linked! +200 Points",
                    color = EmeraldLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                NeonButton(
                    text = if (currentLang == "bn") "সম্পন্ন করুন" else "Complete Mission",
                    onClick = { onComplete(200) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// -------------------------------------------------------------
// MODE C: MEMORY TRAP MINI-GAME
// -------------------------------------------------------------
@Composable
private fun MemoryTrapMiniGame(
    currentLang: String,
    onComplete: (Int) -> Unit,
    onExit: () -> Unit
) {
    val options = listOf("Al₂(SO₄)₃", "Al₃(SO₄)₂", "AlSO₄", "Al₂(SO₄)₂")
    val correctIdx = 0
    var selectedIdx by remember { mutableIntStateOf(-1) }
    var submitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = if (currentLang == "bn") "মোড সি: স্মৃতি ফাঁদ (Memory Trap)" else "Mode C: Memory Trap",
            fontWeight = FontWeight.Bold,
            color = RoseNeon,
            fontSize = 16.sp
        )

        Text(
            text = if (currentLang == "bn")
                "ফাঁদ এড়িয়ে চলুন! অ্যালুমিনিয়াম সালফেটের প্রকৃত রাসায়নিক সংকেত কোনটি?"
            else
                "Beware of subtle traps! Which formula correctly represents Aluminium Sulfate?",
            fontSize = 13.sp,
            color = TextSecondary
        )

        for (idx in options.indices) {
            val isSelected = selectedIdx == idx
            val optBorder = if (submitted && idx == correctIdx) EmeraldNeon else if (submitted && isSelected) RoseNeon else if (isSelected) CyanNeon else BorderSubtle

            SciFiCard(
                borderColor = optBorder,
                modifier = Modifier
                    .clickable(enabled = !submitted) { selectedIdx = idx }
                    .testTag("trap_opt_$idx")
            ) {
                Text(
                    text = options[idx],
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) CyanNeon else TextPrimary
                )
            }
        }

        if (submitted) {
            val isCorrect = selectedIdx == correctIdx
            SciFiCard(borderColor = if (isCorrect) EmeraldNeon else RoseNeon) {
                Text(
                    text = if (isCorrect) (if (currentLang == "bn") "সঠিক! Al³⁺ এবং SO₄²⁻ এর নিরপেক্ষ সংকেত Al₂(SO₄)₃ (+১৫০ পয়েন্ট)" else "Correct! Al³⁺ and SO₄²⁻ balance to Al₂(SO₄)₃ (+150 Pts)") else (if (currentLang == "bn") "ভুল সংকেত। বহু-পারমাণবিক মূলকে বন্ধনী ও চার্জের ল.সা.গু গুরুত্বপূর্ণ।" else "Trap sprung! Polyatomic sulfate requires parentheses and LCM = 6."),
                    color = if (isCorrect) EmeraldLight else RoseLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                NeonButton(
                    text = if (currentLang == "bn") "পরবর্তী ধাপ" else "Next Step",
                    onClick = { onComplete(if (isCorrect) 150 else 20) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            NeonButton(
                text = Strings.submit(currentLang),
                onClick = { if (selectedIdx != -1) submitted = true },
                enabled = selectedIdx != -1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// -------------------------------------------------------------
// MODE B: TIMEFOLD MINI-GAME
// -------------------------------------------------------------
@Composable
private fun TimefoldMiniGame(
    currentLang: String,
    onComplete: (Int) -> Unit,
    onExit: () -> Unit
) {
    val options = listOf(
        if (currentLang == "bn") "১. হাইড্রোজেন ও অক্সিজেন গ্যাসের সংঘর্ষ" else "1. H₂ and O₂ molecules collide with activation energy",
        if (currentLang == "bn") "২. H-H ও O=O সমযোজী বন্ধন ভেঙে যাওয়া" else "2. Existing covalent bonds break",
        if (currentLang == "bn") "৩. নতুন O-H বন্ধন সৃষ্টি হয়ে H₂O গঠন" else "3. New polar O-H bonds form liquid water"
    )
    var selectedIdx by remember { mutableIntStateOf(-1) }
    var submitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = if (currentLang == "bn") "মোড বি: টাইমফোল্ড রাসায়নিক পরিবর্তন (Timefold)" else "Mode B: Timefold Reaction Order",
            fontWeight = FontWeight.Bold,
            color = AmberNeon,
            fontSize = 16.sp
        )

        Text(
            text = if (currentLang == "bn")
                "একটি রাসায়নিক বিক্রিয়ার কালানুক্রমিক পরিবর্তনের প্রথম ধাপ কোনটি?"
            else
                "In a chemical transformation timeline, which event occurs first?",
            fontSize = 13.sp,
            color = TextSecondary
        )

        for (idx in options.indices) {
            val isSelected = selectedIdx == idx
            SciFiCard(
                borderColor = if (submitted && idx == 0) EmeraldNeon else if (isSelected) AmberNeon else BorderSubtle,
                modifier = Modifier
                    .clickable(enabled = !submitted) { selectedIdx = idx }
                    .testTag("timefold_opt_$idx")
            ) {
                Text(
                    text = options[idx],
                    fontSize = 13.sp,
                    color = TextPrimary
                )
            }
        }

        if (submitted) {
            val correct = selectedIdx == 0
            SciFiCard(borderColor = if (correct) EmeraldNeon else RoseNeon) {
                Text(
                    text = if (correct) (if (currentLang == "bn") "সঠিক কালানুক্রম! সংঘর্ষ ছাড়া বন্ধন ভাঙা অসম্ভব। +১০০ পয়েন্ট" else "Chronologically accurate! Collisions precede bond cleavage. +100 Pts") else (if (currentLang == "bn") "সঠিক হয়নি। সংঘর্ষই বিক্রিয়ার সূচনা করে।" else "Incorrect. Collision theory governs the initial step."),
                    color = if (correct) EmeraldLight else RoseLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                NeonButton(
                    text = if (currentLang == "bn") "সম্পন্ন" else "Finish",
                    onClick = { onComplete(if (correct) 100 else 20) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            NeonButton(
                text = Strings.submit(currentLang),
                onClick = { if (selectedIdx != -1) submitted = true },
                enabled = selectedIdx != -1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// -------------------------------------------------------------
// MODE E: DELAYED ECHO MINI-GAME
// -------------------------------------------------------------
@Composable
private fun DelayedEchoMiniGame(
    currentLang: String,
    onComplete: (Int) -> Unit,
    onExit: () -> Unit
) {
    val promptBn = "পূর্বের পাঠ থেকে স্মরণ করুন: বিশুদ্ধ পানির pH ৭.০ হলে pOH এর মান কত?"
    val promptEn = "Delayed Recall from past expeditions: If pure water has pH 7.0 at 25°C, what is pOH?"
    val options = listOf("7.0", "14.0", "1.0", "0.0")
    var selectedIdx by remember { mutableIntStateOf(-1) }
    var submitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = if (currentLang == "bn") "মোড ই: বিলম্বিত অনুরণন (Delayed Echo)" else "Mode E: Delayed Echo Spaced Recall",
            fontWeight = FontWeight.Bold,
            color = CyanGlow,
            fontSize = 16.sp
        )

        Text(
            text = if (currentLang == "bn") promptBn else promptEn,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        for (idx in options.indices) {
            val isSelected = selectedIdx == idx
            SciFiCard(
                borderColor = if (submitted && idx == 0) EmeraldNeon else if (isSelected) CyanNeon else BorderSubtle,
                modifier = Modifier
                    .clickable(enabled = !submitted) { selectedIdx = idx }
                    .testTag("delayed_opt_$idx")
            ) {
                Text(text = options[idx], fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
        }

        if (submitted) {
            val correct = selectedIdx == 0
            SciFiCard(borderColor = if (correct) EmeraldNeon else RoseNeon) {
                Text(
                    text = if (correct) (if (currentLang == "bn") "অনবদ্য দীর্ঘমেয়াদী স্মৃতি! pH + pOH = ১৪.০ (+১৫০ পয়েন্ট)" else "Flawless Long-term Memory Retention! pH + pOH = 14.0 (+150 Pts)") else (if (currentLang == "bn") "সঠিক হয়নি। ১৪.০ - ৭.০ = ৭.০।" else "Incorrect. 14.0 - 7.0 = 7.0."),
                    color = if (correct) EmeraldLight else RoseLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                NeonButton(
                    text = if (currentLang == "bn") "সম্পন্ন" else "Finish",
                    onClick = { onComplete(if (correct) 150 else 20) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            NeonButton(
                text = Strings.submit(currentLang),
                onClick = { if (selectedIdx != -1) submitted = true },
                enabled = selectedIdx != -1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
