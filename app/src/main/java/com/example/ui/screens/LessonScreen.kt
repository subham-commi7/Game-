package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TipsAndUpdates
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
import com.example.data.ChemistryUniverseData
import com.example.data.Strings
import com.example.model.ChemistryLesson
import com.example.ui.components.AtomVisualCanvas
import com.example.ui.components.BeakerCanvas
import com.example.ui.components.NeonButton
import com.example.ui.components.SciFiCard
import com.example.ui.components.SciFiTopBar
import com.example.ui.theme.*

@Composable
fun LessonScreen(
    worldId: Int,
    currentLang: String,
    score: Int,
    isAlreadyLearned: Boolean = false,
    onToggleLang: () -> Unit,
    onCompleteLesson: () -> Unit,
    onLaunchGame: () -> Unit,
    onBack: () -> Unit
) {
    val lesson = ChemistryUniverseData.LESSONS[worldId] ?: ChemistryUniverseData.LESSONS[1]!!
    val scrollState = rememberScrollState()
    var isBookmarked by remember { mutableStateOf(false) }
    var hasCompletedLessonStudy by remember(worldId) { mutableStateOf(isAlreadyLearned) }

    Scaffold(
        topBar = {
            SciFiTopBar(
                title = if (currentLang == "bn") lesson.titleBn else lesson.titleEn,
                score = score,
                currentLang = currentLang,
                onToggleLang = onToggleLang,
                onBack = onBack,
                testTag = "lesson_top_bar"
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
            // Bilingual Key Term Badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, BorderCyan, RoundedCornerShape(12.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (currentLang == "bn") "মৌলিক বৈজ্ঞানিক ধারণা" else "Core Scientific Concept",
                        fontSize = 11.sp,
                        color = CyanGlow,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = lesson.bilingualHeader,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                }

                IconButton(
                    onClick = { isBookmarked = !isBookmarked },
                    modifier = Modifier.testTag("bookmark_lesson_btn")
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) AmberNeon else TextSecondary
                    )
                }
            }

            // Visual Explanation Interactive Canvas
            when (lesson.visualType) {
                "ATOM_SHELLS" -> {
                    AtomVisualCanvas(
                        protons = 11,
                        neutrons = 12,
                        electrons = 11
                    )
                }
                "PH_STRIP", "SOLUTION_FLASK", "REACTION_BEAKER" -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceNavy)
                            .border(1.dp, BorderViolet, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BeakerCanvas(
                            liquidLevel = 0.65f,
                            liquidColor = CyanNeon,
                            label = "HCl (Acid, pH 2)"
                        )
                        BeakerCanvas(
                            liquidLevel = 0.50f,
                            liquidColor = RoseNeon,
                            label = "NaOH (Base, pH 12)"
                        )
                    }
                }
                else -> {
                    AtomVisualCanvas(
                        protons = 8,
                        neutrons = 8,
                        electrons = 8
                    )
                }
            }

            // Core Concept Overview
            SciFiCard(borderColor = BorderViolet) {
                Text(
                    text = if (currentLang == "bn") "তাত্ত্বিক ব্যাখ্যা ও মূলনীতি" else "Theoretical Foundation",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletNeon
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (currentLang == "bn") lesson.conceptOverviewBn else lesson.conceptOverviewEn,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 20.sp
                )

                if (lesson.formulaDisplay.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SpaceBlack)
                            .border(1.dp, BorderCyan, RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = lesson.formulaDisplay,
                            color = CyanNeon,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Worked Example
            val we = lesson.workedExample
            SciFiCard(borderColor = BorderCyan) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.TipsAndUpdates,
                        contentDescription = "Example",
                        tint = AmberNeon,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (currentLang == "bn") "উদাহরণ ও সমাধান: ${we.titleBn}" else "Worked Example: ${we.titleEn}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberLight
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (currentLang == "bn") we.problemBn else we.problemEn,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                val steps = if (currentLang == "bn") we.stepsBn else we.stepsEn
                for (step in steps) {
                    Text(
                        text = "• $step",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceNavy)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Ans: ${we.finalAnswer}",
                        color = EmeraldLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            // Key Rule
            SciFiCard(borderColor = BorderSubtle) {
                Text(
                    text = if (currentLang == "bn") "মনে রাখার মূল নিয়ম" else "Golden Rule",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldNeon
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (currentLang == "bn") lesson.keyRuleBn else lesson.keyRuleEn,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }

            // Lesson Completion Action
            if (!hasCompletedLessonStudy) {
                NeonButton(
                    text = if (currentLang == "bn") "পাঠ অধ্যয়ন সম্পন্ন করুন (+৫০ XP)" else "Complete Lesson Study (+50 XP)",
                    onClick = {
                        hasCompletedLessonStudy = true
                        onCompleteLesson()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    color = CyanNeon,
                    textColor = SpaceNavy,
                    testTag = "complete_lesson_btn"
                )
            } else {
                SciFiCard(borderColor = EmeraldNeon) {
                    Text(
                        text = if (currentLang == "bn")
                            "✓ পাঠের ধারণা সমাপ্ত হয়েছে এবং আপনার রিভিশন নোটবুকে যুক্ত হয়েছে।"
                        else
                            "✓ Lesson studied. Core concepts added to your Spaced Review notebook.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldLight
                    )
                }
            }

            // Launch Playable Mini-Game
            NeonButton(
                text = if (currentLang == "bn") "মিনি-গেম চ্যালেঞ্জ খেলুন" else "Launch Mini-Game Challenge",
                onClick = onLaunchGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                color = VioletDeep,
                textColor = TextPrimary,
                testTag = "launch_minigame_btn"
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
