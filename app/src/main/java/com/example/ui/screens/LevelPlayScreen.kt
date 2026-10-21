package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Star
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
import com.example.data.CampaignLevelsData
import com.example.model.CampaignLevel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun LevelPlayScreen(
    levelNumber: Int,
    currentLang: String,
    score: Int,
    isLevelAlreadyCompleted: Boolean = false,
    savedStars: Int = 0,
    reducedMotion: Boolean = false,
    visualEffectsHigh: Boolean = true,
    onToggleLang: () -> Unit,
    onCompleteLevel: (levelNum: Int, earnedScore: Int, stars: Int, conceptKey: String) -> Int,
    onOpenAiTutor: (level: CampaignLevel, userLastAnswer: String, isCorrect: Boolean) -> Unit,
    onBack: () -> Unit
) {
    val level = remember(levelNumber) {
        CampaignLevelsData.getLevel(levelNumber)
    }

    if (level == null) {
        Scaffold(
            topBar = {
                SciFiTopBar(
                    title = if (currentLang == "bn") "ত্রুটি: স্তর পাওয়া যায়নি" else "Level Not Found",
                    score = score,
                    currentLang = currentLang,
                    onToggleLang = onToggleLang,
                    onBack = onBack,
                    testTag = "invalid_level_top_bar"
                )
            },
            containerColor = SpaceBlack
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                SciFiCard(borderColor = RoseNeon) {
                    Text(
                        text = if (currentLang == "bn") "অনুরোধকৃত স্তর ($levelNumber) অভিযান মানচিত্রে বিদ্যমান নেই।" else "Requested campaign level ($levelNumber) does not exist.",
                        fontSize = 14.sp,
                        color = RoseLight,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    NeonButton(
                        text = if (currentLang == "bn") "অভিযান মানচিত্রে ফিরে যান" else "Return to Expedition",
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        return
    }

    var selectedOption by remember(level.levelNumber) { mutableIntStateOf(-1) }
    var isSubmitted by remember(level.levelNumber) { mutableStateOf(false) }
    var usedHint by remember(level.levelNumber) { mutableStateOf(false) }
    var mistakeCount by remember(level.levelNumber) { mutableIntStateOf(0) }
    var awardedPoints by remember(level.levelNumber) { mutableIntStateOf(-1) }

    val q = level.question
    val isCorrect = selectedOption == q.correctIndex

    val starsEarned = remember(isSubmitted, isCorrect, usedHint, mistakeCount) {
        if (!isSubmitted || !isCorrect) {
            0
        } else if (mistakeCount == 0 && !usedHint) {
            3
        } else if (mistakeCount <= 1) {
            2
        } else {
            1
        }
    }

    val calculatedScore = remember(starsEarned, usedHint) {
        val base = level.xpReward
        val raw = if (starsEarned == 3) base else if (starsEarned == 2) (base * 0.85).toInt() else (base * 0.70).toInt()
        if (usedHint) maxOf(10, raw - 15) else raw
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            SciFiTopBar(
                title = if (currentLang == "bn") level.titleBn else level.titleEn,
                score = score,
                currentLang = currentLang,
                onToggleLang = onToggleLang,
                onBack = onBack,
                testTag = "level_play_top_bar"
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
            // Header: Level Info & Stars
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${if (currentLang == "bn") "বিশ্ব" else "World"} ${level.worldId} • ${if (level.isBoss) "BOSS LEVEL" else "STAGE ${level.levelNumber}"}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (level.isBoss) AmberNeon else CyanNeon
                    )
                    Text(
                        text = if (currentLang == "bn") level.subtitleBn else level.subtitleEn,
                        fontSize = 11.sp,
                        color = TextTertiary
                    )
                }

                // Stars Display
                Row {
                    val displayStars = if (isSubmitted && isCorrect) starsEarned else savedStars
                    repeat(3) { starIdx ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = if (starIdx < displayStars) AmberNeon else BorderSubtle,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Step 1: Objective & Concept Brief
            SciFiCard(borderColor = BorderCyan) {
                Text(
                    text = if (currentLang == "bn") "শিক্ষণ উদ্দেশ্য ও মৌলিক নীতি" else "Learning Objective & Core Principle",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanNeon
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (currentLang == "bn") level.objectiveBn else level.objectiveEn,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (currentLang == "bn") level.conceptExplanationBn else level.conceptExplanationEn,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }

            // Step 2: Interactive Scientific Visual & Lab for this World
            when (level.worldId) {
                1 -> InteractiveAtomVisual(levelNumber = level.levelNumber, currentLang = currentLang, reducedMotion = reducedMotion, visualEffectsHigh = visualEffectsHigh)
                2 -> InteractivePeriodicTableVisual(levelNumber = level.levelNumber, currentLang = currentLang)
                3 -> InteractiveBondingVisual(levelNumber = level.levelNumber, currentLang = currentLang)
                4 -> InteractiveFormulaForgeVisual(levelNumber = level.levelNumber, currentLang = currentLang)
                5 -> InteractiveReactionReactorVisual(levelNumber = level.levelNumber, currentLang = currentLang)
                6 -> InteractiveMoleMatrixVisual(levelNumber = level.levelNumber, currentLang = currentLang)
                7 -> InteractivePHScaleVisual(levelNumber = level.levelNumber, currentLang = currentLang)
                8 -> InteractiveSolutionLabVisual(levelNumber = level.levelNumber, currentLang = currentLang)
                9 -> InteractiveOrganicForestVisual(levelNumber = level.levelNumber, currentLang = currentLang)
                else -> InteractivePharmaVaultVisual(levelNumber = level.levelNumber, currentLang = currentLang)
            }

            // Step 3: Worked Example
            SciFiCard(borderColor = BorderViolet) {
                Text(
                    text = "💡 ${if (currentLang == "bn") level.workedExample.titleBn else level.workedExample.titleEn}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberLight
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (currentLang == "bn") level.workedExample.problemBn else level.workedExample.problemEn,
                    fontSize = 12.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                val steps = if (currentLang == "bn") level.workedExample.stepsBn else level.workedExample.stepsEn
                for (s in steps) {
                    Text("• $s", fontSize = 11.sp, color = TextSecondary, lineHeight = 15.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ans: ${level.workedExample.finalAnswer}",
                    color = EmeraldLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Step 4: Interactive Challenge Puzzle
            val options = if (currentLang == "bn") q.optionsBn else q.optionsEn
            SciFiCard(borderColor = if (isSubmitted) (if (isCorrect) EmeraldNeon else RoseNeon) else BorderCyan) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (currentLang == "bn") "পর্যাপ্ত দক্ষতা যাচাই পরীক্ষা" else "Level Challenge Puzzle",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanNeon
                    )
                    Text(
                        text = "+${level.xpReward} XP",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberNeon,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (currentLang == "bn") q.promptBn else q.promptEn,
                    fontSize = 14.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                options.forEachIndexed { idx, opt ->
                    val isOptSelected = selectedOption == idx
                    val isOptCorrect = idx == q.correctIndex

                    val optBorderColor = when {
                        isSubmitted && isOptCorrect -> EmeraldNeon
                        isSubmitted && isOptSelected && !isOptCorrect -> RoseNeon
                        isOptSelected -> CyanNeon
                        else -> BorderSubtle
                    }

                    val optBgColor = when {
                        isSubmitted && isOptCorrect -> EmeraldNeon.copy(alpha = 0.20f)
                        isSubmitted && isOptSelected && !isOptCorrect -> RoseNeon.copy(alpha = 0.20f)
                        isOptSelected -> CyanNeon.copy(alpha = 0.15f)
                        else -> SurfaceCard
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(optBgColor)
                            .border(1.dp, optBorderColor, RoundedCornerShape(10.dp))
                            .clickable(enabled = !isSubmitted || !isCorrect) {
                                selectedOption = idx
                                if (!isCorrect) {
                                    isSubmitted = false
                                }
                            }
                            .padding(12.dp)
                            .testTag("level_option_$idx")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${('A' + idx)}.  ",
                                fontWeight = FontWeight.Bold,
                                color = if (isOptSelected) CyanNeon else TextSecondary,
                                fontSize = 13.sp
                            )
                            Text(
                                text = opt,
                                fontSize = 13.sp,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            if (isSubmitted && isOptCorrect) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = EmeraldNeon, modifier = Modifier.size(18.dp))
                            } else if (isSubmitted && isOptSelected && !isOptCorrect) {
                                Icon(Icons.Default.Close, contentDescription = "Wrong", tint = RoseNeon, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }

                // Hint Option
                if (!isSubmitted) {
                    Spacer(modifier = Modifier.height(6.dp))
                    TextButton(
                        onClick = { usedHint = true },
                        modifier = Modifier.testTag("level_hint_btn")
                    ) {
                        Icon(Icons.Default.HelpOutline, contentDescription = "Hint", tint = AmberLight, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (usedHint)
                                (if (currentLang == "bn") "হিন্ট: ${q.hintBn}" else "Hint: ${q.hintEn}")
                            else
                                (if (currentLang == "bn") "হিন্ট দেখুন (স্কোর নিয়ম: -১৫ XP)" else "View Hint (-15 XP on score)"),
                            fontSize = 11.sp,
                            color = AmberLight
                        )
                    }
                }

                // Feedback after submit
                if (isSubmitted) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isCorrect) EmeraldNeon.copy(alpha = 0.15f) else RoseNeon.copy(alpha = 0.15f))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = if (isCorrect)
                                    (if (currentLang == "bn") "✓ সঠিক উত্তর! পরবর্তী ধাপে এগিয়ে যান।" else "✓ Correct! Step completed.")
                                else
                                    (if (currentLang == "bn") "✗ সঠিক নয়। আবার চিন্তা করুন ও সমাধান বিশ্লেষণ করুন।" else "✗ Incorrect. Review the reasoning below."),
                                fontWeight = FontWeight.Bold,
                                color = if (isCorrect) EmeraldLight else RoseLight,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (currentLang == "bn") q.explanationBn else q.explanationEn,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Submit or Retry Button
                if (!isSubmitted || !isCorrect) {
                    NeonButton(
                        text = if (selectedOption == -1)
                            (if (currentLang == "bn") "একটি বিকল্প নির্বাচন করুন" else "Select an Option")
                        else
                            (if (currentLang == "bn") "উত্তর নিশ্চিত করুন" else "Submit Answer"),
                        onClick = {
                            if (selectedOption != -1) {
                                isSubmitted = true
                                if (selectedOption == q.correctIndex) {
                                    if (awardedPoints == -1) {
                                        val pts = onCompleteLevel(level.levelNumber, calculatedScore, starsEarned, level.conceptKey)
                                        awardedPoints = pts
                                    }
                                } else {
                                    mistakeCount++
                                }
                            }
                        },
                        enabled = selectedOption != -1,
                        modifier = Modifier.weight(1f),
                        testTag = "submit_level_btn"
                    )
                }

                // AI Tutor Button
                OutlinedButton(
                    onClick = {
                        val selectedText = if (selectedOption >= 0 && selectedOption < options.size) options[selectedOption] else ""
                        onOpenAiTutor(level, selectedText, isCorrect)
                    },
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .testTag("ask_ai_tutor_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanNeon),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderCyan)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "AI Tutor", tint = CyanNeon, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (currentLang == "bn") "AI টিউটর" else "AI Tutor",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Level Completion Banner
            if (isSubmitted && isCorrect) {
                SciFiCard(borderColor = EmeraldNeon) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (currentLang == "bn") "অভিনন্দন! স্তর সম্পন্ন হয়েছে" else "Level Completed!",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldNeon
                            )
                            Text(
                                text = if (awardedPoints > 0)
                                    "+$awardedPoints XP Awarded • $starsEarned Stars Earned"
                                else
                                    "Replay Complete ($starsEarned Stars) • No Duplicate XP",
                                fontSize = 11.sp,
                                color = AmberLight
                            )
                        }

                        Button(
                            onClick = onBack,
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon, contentColor = SpaceBlack),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("continue_expedition_btn")
                        ) {
                            Text(
                                text = if (currentLang == "bn") "পরবর্তী" else "Continue",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
