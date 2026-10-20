package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Remove
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
import com.example.model.ChemistryQuestion
import com.example.ui.components.AtomVisualCanvas
import com.example.ui.components.NeonButton
import com.example.ui.components.SciFiCard
import com.example.ui.components.SciFiTopBar
import com.example.ui.theme.*

@Composable
fun ChemistryGameScreen(
    worldId: Int,
    currentLang: String,
    score: Int,
    onToggleLang: () -> Unit,
    onRecordQuizResult: (worldId: Int, topicKey: String, correct: Boolean, scoreEarned: Int) -> Unit,
    onBack: () -> Unit
) {
    val world = ChemistryUniverseData.WORLDS.find { it.id == worldId } ?: ChemistryUniverseData.WORLDS[0]
    val questions = ChemistryUniverseData.QUESTIONS_PER_WORLD[worldId] ?: ChemistryUniverseData.QUESTIONS_PER_WORLD[1]!!
    var questionIndex by remember { mutableIntStateOf(0) }
    val currentQuestion = questions[questionIndex % questions.size]

    var selectedOption by remember(currentQuestion.id) { mutableIntStateOf(-1) }
    var submitted by remember(currentQuestion.id) { mutableStateOf(false) }
    var showHint by remember(currentQuestion.id) { mutableStateOf(false) }

    // Interactive Atom Assembler state for World 1
    var pCount by remember { mutableIntStateOf(6) }
    var nCount by remember { mutableIntStateOf(6) }
    var eCount by remember { mutableIntStateOf(6) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            SciFiTopBar(
                title = if (currentLang == "bn") world.gameTitleBn else world.gameTitleEn,
                score = score,
                currentLang = currentLang,
                onToggleLang = onToggleLang,
                onBack = onBack,
                testTag = "chemistry_game_top_bar"
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header: World & Mode Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${if (currentLang == "bn") "মিশন" else "Mission"} ${world.id} • ${world.key.uppercase()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanNeon
                )

                Text(
                    text = "Q: ${questionIndex + 1}/${questions.size}",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // If World 1, show interactive Atom Assembler builder widget
            if (worldId == 1) {
                SciFiCard(borderColor = BorderCyan) {
                    Text(
                        text = if (currentLang == "bn") "ইন্টারেক্টিভ পরমাণু সংযোজক" else "Interactive Atom Assembler",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanNeon
                    )
                    Text(
                        text = if (currentLang == "bn")
                            "প্রোটন, নিউট্রন ও ইলেকট্রন পরিবর্তন করে পরমাণুর চার্জ ও ভর পর্যবেক্ষণ করুন:"
                        else
                            "Adjust subatomic particles to verify Z, A, and Net Charge:",
                        fontSize = 11.sp,
                        color = TextTertiary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    AtomVisualCanvas(
                        protons = pCount,
                        neutrons = nCount,
                        electrons = eCount
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Controls for Protons, Neutrons, Electrons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ParticleCounter(
                            label = "p⁺",
                            color = CyanNeon,
                            count = pCount,
                            onIncrement = { if (pCount < 18) pCount++ },
                            onDecrement = { if (pCount > 1) pCount-- }
                        )
                        ParticleCounter(
                            label = "n⁰",
                            color = VioletNeon,
                            count = nCount,
                            onIncrement = { if (nCount < 22) nCount++ },
                            onDecrement = { if (nCount > 0) nCount-- }
                        )
                        ParticleCounter(
                            label = "e⁻",
                            color = AmberLight,
                            count = eCount,
                            onIncrement = { if (eCount < 18) eCount++ },
                            onDecrement = { if (eCount > 0) eCount-- }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val z = ChemistryEngine.calculateAtomicNumber(pCount)
                    val a = ChemistryEngine.calculateMassNumber(pCount, nCount)
                    val charge = ChemistryEngine.calculateIonCharge(pCount, eCount)
                    val chargeStr = when {
                        charge > 0 -> "+$charge"
                        charge < 0 -> "$charge"
                        else -> "0 (Neutral)"
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceNavy)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text("Z = $z", color = CyanNeon, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("A = $a", color = VioletNeon, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Charge = $chargeStr", color = if (charge == 0) EmeraldNeon else RoseNeon, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            // Question Card
            SciFiCard(borderColor = BorderViolet) {
                Text(
                    text = if (currentLang == "bn") currentQuestion.promptBn else currentQuestion.promptEn,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Options
                val opts = if (currentLang == "bn") currentQuestion.optionsBn else currentQuestion.optionsEn
                for (idx in opts.indices) {
                    val isSelected = selectedOption == idx
                    val isCorrect = idx == currentQuestion.correctIndex

                    val optBgColor = when {
                        submitted && isCorrect -> EmeraldNeon.copy(alpha = 0.25f)
                        submitted && isSelected && !isCorrect -> RoseNeon.copy(alpha = 0.25f)
                        isSelected -> SurfaceNavy
                        else -> SurfaceCard
                    }

                    val optBorderColor = when {
                        submitted && isCorrect -> EmeraldNeon
                        submitted && isSelected && !isCorrect -> RoseNeon
                        isSelected -> CyanNeon
                        else -> BorderSubtle
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(optBgColor)
                            .border(1.dp, optBorderColor, RoundedCornerShape(12.dp))
                            .clickable(enabled = !submitted) { selectedOption = idx }
                            .padding(14.dp)
                            .testTag("option_$idx"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) CyanNeon else SurfaceCardHover)
                                .border(1.dp, if (isSelected) CyanNeon else BorderSubtle, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${('A' + idx)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) SpaceBlack else TextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = opts[idx],
                            fontSize = 13.sp,
                            color = TextPrimary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            // Hint toggle button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { showHint = !showHint },
                    modifier = Modifier.testTag("hint_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Hint",
                        tint = AmberNeon,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = Strings.hint(currentLang),
                        color = AmberNeon,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (showHint) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceCard)
                        .border(1.dp, BorderViolet, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = if (currentLang == "bn") currentQuestion.hintBn else currentQuestion.hintEn,
                        fontSize = 12.sp,
                        color = AmberLight,
                        lineHeight = 16.sp
                    )
                }
            }

            // Feedback & Scientific Explanation
            if (submitted) {
                val isAnswerCorrect = selectedOption == currentQuestion.correctIndex
                SciFiCard(
                    borderColor = if (isAnswerCorrect) EmeraldNeon else RoseNeon
                ) {
                    Text(
                        text = if (isAnswerCorrect) Strings.correct(currentLang) else Strings.incorrect(currentLang),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isAnswerCorrect) EmeraldLight else RoseLight
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (currentLang == "bn") currentQuestion.explanationBn else currentQuestion.explanationEn,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    NeonButton(
                        text = if (questionIndex < questions.size - 1)
                            Strings.next(currentLang)
                        else
                            (if (currentLang == "bn") "পরবর্তী বিশ্বে যান" else "Return to Universe"),
                        onClick = {
                            if (questionIndex < questions.size - 1) {
                                questionIndex++
                            } else {
                                onBack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        color = CyanNeon,
                        testTag = "next_question_btn"
                    )
                }
            } else {
                NeonButton(
                    text = Strings.submit(currentLang),
                    onClick = {
                        if (selectedOption != -1) {
                            submitted = true
                            val correct = selectedOption == currentQuestion.correctIndex
                            val pts = if (correct) currentQuestion.points else 0
                            onRecordQuizResult(worldId, world.key, correct, pts)
                        }
                    },
                    enabled = selectedOption != -1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    color = CyanNeon,
                    testTag = "submit_answer_btn"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ParticleCounter(
    label: String,
    color: Color,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            IconButton(
                onClick = onDecrement,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(SurfaceCardHover)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Minus", tint = TextPrimary, modifier = Modifier.size(16.dp))
            }
            Text(
                text = "$count",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(
                onClick = onIncrement,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(SurfaceCardHover)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Plus", tint = TextPrimary, modifier = Modifier.size(16.dp))
            }
        }
    }
}
