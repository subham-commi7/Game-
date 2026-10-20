package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
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
import com.example.data.Strings
import com.example.model.DailyChallengeState
import com.example.ui.components.NeonButton
import com.example.ui.components.SciFiCard
import com.example.ui.components.SciFiTopBar
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import java.util.Random

@Composable
fun DailyChallengeScreen(
    currentLang: String,
    score: Int,
    dailyState: DailyChallengeState,
    onToggleLang: () -> Unit,
    onCompleteDailyChallenge: () -> Int,
    onBack: () -> Unit
) {
    // Generate deterministic seed based on device calendar date string
    val seed = remember(dailyState.dateString) {
        var h = 1125899906842597L
        for (c in dailyState.dateString) {
            h = 31 * h + c.code
        }
        h
    }

    // Seeded sequence for 4 nodes
    val sequence = remember(seed) {
        val rand = Random(seed)
        val list = mutableListOf<Int>()
        for (i in 0 until 4) {
            list.add(rand.nextInt(4))
        }
        list
    }

    var isCompletedToday by remember(dailyState.completed) { mutableStateOf(dailyState.completed) }
    var currentHighlight by remember { mutableIntStateOf(-1) }
    var isDemonstrating by remember { mutableStateOf(!isCompletedToday) }
    val userTaps = remember { mutableStateListOf<Int>() }
    var roundFinished by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var awardedPoints by remember { mutableIntStateOf(0) }

    LaunchedEffect(isCompletedToday, seed) {
        if (!isCompletedToday) {
            delay(500)
            for (nodeIndex in sequence) {
                currentHighlight = nodeIndex
                delay(700)
                currentHighlight = -1
                delay(250)
            }
            isDemonstrating = false
        }
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            SciFiTopBar(
                title = Strings.dailyChallenge(currentLang),
                score = score,
                currentLang = currentLang,
                onToggleLang = onToggleLang,
                onBack = onBack,
                testTag = "daily_challenge_top_bar"
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
            // Distinct Daily Quest Header Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceNavy)
                    .border(1.dp, AmberNeon, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(AmberNeon.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = "Daily Quest", tint = AmberNeon)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (currentLang == "bn") "দৈনিক মেমোরি মিশন" else "Daily Cognitive Quest",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AmberLight
                        )
                        Text(
                            text = "${if (currentLang == "bn") "তারিখ:" else "Date:"} ${dailyState.dateString}  •  ${if (currentLang == "bn") "বোনাস:" else "Reward:"} +250 Pts",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TextSecondary
                        )
                    }

                    if (isCompletedToday) {
                        Badge(containerColor = EmeraldNeon.copy(alpha = 0.2f)) {
                            Text(
                                text = if (currentLang == "bn") "সম্পন্ন" else "Completed",
                                color = EmeraldLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            if (isCompletedToday && !roundFinished) {
                // Completed State Card
                SciFiCard(borderColor = EmeraldNeon) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Done", tint = EmeraldNeon)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLang == "bn") "আজকের দৈনিক চ্যালেঞ্জ সম্পন্ন হয়েছে!" else "Daily Challenge Completed for Today!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldLight
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (currentLang == "bn")
                            "আপনি আজকের ২৫০ পয়েন্ট বোনাস অর্জন করেছেন। নতুন চ্যালেঞ্জের জন্য আগামীকাল পুনরায় আসুন।"
                        else
                            "You have already collected today's 250-point bonus. Come back tomorrow for the next challenge.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    NeonButton(
                        text = if (currentLang == "bn") "পুনরায় অনুশীলন করুন" else "Practice Again",
                        onClick = {
                            isCompletedToday = false
                            isDemonstrating = true
                            userTaps.clear()
                            roundFinished = false
                            success = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        color = AmberNeon,
                        testTag = "replay_daily_practice_btn"
                    )
                }
            } else {
                // Active Puzzle Interaction
                Text(
                    text = if (isDemonstrating) {
                        if (currentLang == "bn") "আজকের নির্দিষ্ট কোয়ান্টাম ক্রমটি লক্ষ্য করুন..." else "Observe today's deterministic orbital sequence..."
                    } else {
                        if (currentLang == "bn") "একই ক্রমানুসারে স্পর্শ করুন (${userTaps.size}/${sequence.size})" else "Repeat the sequence in order (${userTaps.size}/${sequence.size})"
                    },
                    fontSize = 13.sp,
                    color = if (isDemonstrating) AmberLight else CyanGlow,
                    fontWeight = FontWeight.SemiBold
                )

                // 2x2 Daily Orbital Nodes
                val nodeLabels = listOf("H (1)", "C (6)", "N (7)", "O (8)")
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
                            DailyNode(0, nodeLabels[0], currentHighlight == 0, userTaps.contains(0)) {
                                if (!isDemonstrating && !roundFinished) {
                                    handleDailyTap(0, sequence, userTaps) { s ->
                                        success = s
                                        roundFinished = true
                                        if (s) {
                                            awardedPoints = onCompleteDailyChallenge()
                                            isCompletedToday = true
                                        }
                                    }
                                }
                            }
                            DailyNode(1, nodeLabels[1], currentHighlight == 1, userTaps.contains(1)) {
                                if (!isDemonstrating && !roundFinished) {
                                    handleDailyTap(1, sequence, userTaps) { s ->
                                        success = s
                                        roundFinished = true
                                        if (s) {
                                            awardedPoints = onCompleteDailyChallenge()
                                            isCompletedToday = true
                                        }
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            DailyNode(2, nodeLabels[2], currentHighlight == 2, userTaps.contains(2)) {
                                if (!isDemonstrating && !roundFinished) {
                                    handleDailyTap(2, sequence, userTaps) { s ->
                                        success = s
                                        roundFinished = true
                                        if (s) {
                                            awardedPoints = onCompleteDailyChallenge()
                                            isCompletedToday = true
                                        }
                                    }
                                }
                            }
                            DailyNode(3, nodeLabels[3], currentHighlight == 3, userTaps.contains(3)) {
                                if (!isDemonstrating && !roundFinished) {
                                    handleDailyTap(3, sequence, userTaps) { s ->
                                        success = s
                                        roundFinished = true
                                        if (s) {
                                            awardedPoints = onCompleteDailyChallenge()
                                            isCompletedToday = true
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (roundFinished) {
                    SciFiCard(borderColor = if (success) EmeraldNeon else RoseNeon) {
                        Text(
                            text = if (success) {
                                if (awardedPoints > 0)
                                    (if (currentLang == "bn") "অভিনন্দন! দৈনিক মিশন সফল: +$awardedPoints পয়েন্ট!" else "Success! Daily Quest Completed: +$awardedPoints Points!")
                                else
                                    (if (currentLang == "bn") "অনুশীলন সফল! (আজকের বোনাস ইতোমধ্যেই সংগৃহীত)" else "Practice successful! (Today's bonus already claimed)")
                            } else {
                                if (currentLang == "bn") "ক্রমটি মেলেনি। আবার চেষ্টা করুন।" else "Sequence mismatch. Please try again."
                            },
                            color = if (success) EmeraldLight else RoseLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        NeonButton(
                            text = if (currentLang == "bn") "ড্যাশবোর্ডে ফিরে যান" else "Return to Dashboard",
                            onClick = onBack,
                            modifier = Modifier.fillMaxWidth(),
                            color = if (success) EmeraldNeon else CyanNeon,
                            testTag = "daily_finish_btn"
                        )
                    }
                }
            }
        }
    }
}

private fun handleDailyTap(
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
private fun DailyNode(
    index: Int,
    label: String,
    isHighlighted: Boolean,
    isTapped: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = when {
            isHighlighted -> AmberNeon
            isTapped -> CyanNeon
            else -> SurfaceCard
        },
        label = "daily_node_color"
    )

    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(bgColor)
            .border(2.dp, if (isHighlighted) Color.White else AmberNeon, CircleShape)
            .clickable { onClick() }
            .testTag("daily_node_$index"),
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
