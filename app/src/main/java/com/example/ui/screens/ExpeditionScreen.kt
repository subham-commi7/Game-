package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CampaignLevelsData
import com.example.data.ChemistryUniverseData
import com.example.data.Strings
import com.example.model.ChemistryWorld
import com.example.model.UserProgress
import com.example.model.WorldProgress
import com.example.ui.components.SciFiCard
import com.example.ui.components.SciFiTopBar
import com.example.ui.theme.*

@Composable
fun ExpeditionScreen(
    progress: UserProgress,
    currentLang: String,
    onToggleLang: () -> Unit,
    onSelectLevel: (Int) -> Unit,
    onSelectLesson: (Int) -> Unit,
    onSelectGame: (Int) -> Unit,
    onBack: () -> Unit
) {
    val totalLevelsCompleted = progress.completedLevels.size
    val totalStarsCollected = progress.levelStars.values.sum()

    Scaffold(
        topBar = {
            SciFiTopBar(
                title = Strings.expeditionTitle(currentLang),
                score = progress.totalScore,
                currentLang = currentLang,
                onToggleLang = onToggleLang,
                onBack = onBack,
                testTag = "expedition_top_bar"
            )
        },
        containerColor = SpaceBlack
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            item {
                // Campaign Summary Status Card with Continue Action
                val nextPlayableLevel = remember(progress.unlockedLevels, progress.completedLevels) {
                    (1..50).firstOrNull { progress.unlockedLevels.contains(it) && !progress.completedLevels.contains(it) }
                        ?: (progress.unlockedLevels.maxOrNull() ?: 1)
                }

                SciFiCard(borderColor = BorderCyan) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (currentLang == "bn") "৫০-স্তরের মহাবিশ্ব অভিযান" else "50-Level Campaign Expedition",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CyanNeon
                                )
                                Text(
                                    text = if (currentLang == "bn")
                                        "১০টি বিশ্ব • প্রতি বিশ্বে ৫টি শিক্ষণীয় স্তর"
                                    else
                                        "10 Worlds • 5 Guided Scientific Stages Each",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "$totalLevelsCompleted / 50 ${if (currentLang == "bn") "সম্পন্ন" else "Done"}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldNeon
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Stars",
                                        tint = AmberNeon,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "$totalStarsCollected / 150 ★",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AmberLight
                                    )
                                }
                            }
                        }

                        // Continue Expedition Banner & Action
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceNavy)
                                .border(1.dp, CyanNeon.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (currentLang == "bn") "পরবর্তী খেলার স্তর:" else "Next Playable Stage:",
                                    fontSize = 10.sp,
                                    color = TextTertiary
                                )
                                Text(
                                    text = "${if (currentLang == "bn") "স্তর" else "Stage"} $nextPlayableLevel",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberLight
                                )
                            }

                            Button(
                                onClick = { onSelectLevel(nextPlayableLevel) },
                                colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = SpaceBlack),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("continue_expedition_action_btn")
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (currentLang == "bn") "অভিযান চালান" else "Continue Expedition",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            items(ChemistryUniverseData.WORLDS) { world ->
                val isUnlocked = progress.unlockedWorlds.contains(world.id)
                val wp = progress.worldProgressMap[world.id] ?: WorldProgress(world.id, isUnlocked)

                WorldItemCard(
                    world = world,
                    progress = progress,
                    isUnlocked = isUnlocked,
                    quizStars = wp.stars,
                    quizScore = wp.score,
                    currentLang = currentLang,
                    onSelectLevel = onSelectLevel,
                    onOpenLesson = { onSelectLesson(world.id) },
                    onOpenGame = { onSelectGame(world.id) }
                )
            }
        }
    }
}

@Composable
private fun WorldItemCard(
    world: ChemistryWorld,
    progress: UserProgress,
    isUnlocked: Boolean,
    quizStars: Int,
    quizScore: Int,
    currentLang: String,
    onSelectLevel: (Int) -> Unit,
    onOpenLesson: () -> Unit,
    onOpenGame: () -> Unit
) {
    val borderColor = if (isUnlocked) BorderCyan else BorderSubtle
    val worldLevels = remember(world.id) { CampaignLevelsData.getLevelsForWorld(world.id) }
    val stagesDone = remember(progress.completedLevels, world.id) {
        worldLevels.count { progress.completedLevels.contains(it.levelNumber) }
    }
    val campaignStarsEarned = remember(progress.levelStars, world.id) {
        worldLevels.sumOf { progress.levelStars[it.levelNumber] ?: 0 }
    }

    SciFiCard(
        borderColor = borderColor,
        modifier = Modifier.testTag("world_card_${world.id}")
    ) {
        // World Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // World Number Badge
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(if (isUnlocked) SurfaceNavy else SurfaceCardHover)
                    .border(1.5.dp, if (isUnlocked) CyanNeon else BorderSubtle, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Text(
                        text = "${world.id}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = CyanNeon
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = TextTertiary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (currentLang == "bn") world.titleBn else world.titleEn,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) TextPrimary else TextSecondary
                )
                Text(
                    text = if (currentLang == "bn") world.subtitleBn else world.subtitleEn,
                    fontSize = 11.sp,
                    color = TextTertiary,
                    maxLines = 2,
                    lineHeight = 15.sp
                )

                // Distinct Campaign vs Quiz Stats
                Row(
                    modifier = Modifier.padding(top = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Campaign: $stagesDone/5 Done ($campaignStarsEarned/15 ★)",
                        fontSize = 10.sp,
                        color = EmeraldLight,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "• Quiz: $quizStars/3 ★",
                        fontSize = 10.sp,
                        color = VioletNeon,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // World Campaign Stars
            Row {
                val fullStars = (campaignStarsEarned / 5).coerceIn(0, 3)
                repeat(3) { starIdx ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        tint = if (starIdx < fullStars) AmberNeon else BorderSubtle,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Campaign Levels Selection (5 levels per world)
        Text(
            text = if (currentLang == "bn") "বিশ্বের স্তরসমূহ (১-৫):" else "Campaign Stages (Levels 1-5):",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isUnlocked) CyanGlow else TextTertiary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            worldLevels.forEach { lvl ->
                val isLvlUnlocked = isUnlocked && progress.unlockedLevels.contains(lvl.levelNumber)
                val isLvlCompleted = progress.completedLevels.contains(lvl.levelNumber)
                val lvlStars = progress.levelStars[lvl.levelNumber] ?: 0
                val lvlScore = progress.levelHighScores[lvl.levelNumber] ?: 0

                val itemBg = when {
                    isLvlCompleted -> SurfaceNavy
                    isLvlUnlocked -> SurfaceCardHover
                    else -> SurfaceCard.copy(alpha = 0.5f)
                }
                val itemBorder = when {
                    isLvlCompleted -> BorderCyan
                    isLvlUnlocked -> CyanNeon
                    else -> BorderSubtle
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(itemBg)
                        .border(1.dp, itemBorder, RoundedCornerShape(8.dp))
                        .clickable(enabled = isLvlUnlocked) {
                            if (isLvlUnlocked) {
                                onSelectLevel(lvl.levelNumber)
                            }
                        }
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .testTag("level_chip_${lvl.levelNumber}"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Level Indicator Badge
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (isLvlCompleted) EmeraldNeon.copy(alpha = 0.2f)
                                    else if (isLvlUnlocked) CyanNeon.copy(alpha = 0.2f)
                                    else SurfaceCard
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isLvlUnlocked) {
                                Text(
                                    text = "${lvl.levelNumber}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (isLvlCompleted) EmeraldLight else CyanNeon
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Locked",
                                    tint = TextTertiary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (currentLang == "bn") lvl.titleBn else lvl.titleEn,
                                    fontSize = 12.sp,
                                    fontWeight = if (isLvlUnlocked) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isLvlUnlocked) TextPrimary else TextTertiary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (lvl.isBoss) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "[BOSS]",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = AmberNeon
                                    )
                                }
                            }
                            Text(
                                text = if (currentLang == "bn") lvl.subtitleBn else lvl.subtitleEn,
                                fontSize = 10.sp,
                                color = TextTertiary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Level Status / Stars
                    if (isLvlCompleted) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(3) { sIdx ->
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Star",
                                    tint = if (sIdx < lvlStars) AmberNeon else BorderSubtle,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${lvlScore}p",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldLight
                            )
                        }
                    } else if (isLvlUnlocked) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CyanNeon)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (currentLang == "bn") "খেলুন" else "PLAY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = SpaceNavy
                            )
                        }
                    } else {
                        Text(
                            text = if (currentLang == "bn") "লকড" else "LOCKED",
                            fontSize = 10.sp,
                            color = TextTertiary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Action Buttons Row (Lesson and Mini-game)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Lesson Button
            OutlinedButton(
                onClick = onOpenLesson,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .testTag("world_${world.id}_lesson_btn"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CyanNeon
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderCyan),
                enabled = isUnlocked
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Lesson",
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if (currentLang == "bn") "পাঠ ও তত্ত্ব" else "Lesson",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Game Button
            Button(
                onClick = onOpenGame,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .testTag("world_${world.id}_game_btn"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VioletDeep,
                    contentColor = TextPrimary
                ),
                enabled = isUnlocked
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if (currentLang == "bn") "মিনি-গেম" else "Mini-Game",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
