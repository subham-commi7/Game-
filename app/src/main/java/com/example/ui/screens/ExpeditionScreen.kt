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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChemistryUniverseData
import com.example.data.Strings
import com.example.model.ChemistryWorld
import com.example.model.UserProgress
import com.example.model.WorldProgress
import com.example.ui.components.NeonButton
import com.example.ui.components.SciFiCard
import com.example.ui.components.SciFiTopBar
import com.example.ui.theme.*

@Composable
fun ExpeditionScreen(
    progress: UserProgress,
    currentLang: String,
    onToggleLang: () -> Unit,
    onSelectLesson: (Int) -> Unit,
    onSelectGame: (Int) -> Unit,
    onBack: () -> Unit
) {
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
                Text(
                    text = if (currentLang == "bn")
                        "১০টি আন্তঃসংযুক্ত রাসায়নিক মহাবিশ্ব অন্বেষণ করুন"
                    else
                        "Explore 10 Interconnected Chemistry Worlds",
                    fontSize = 14.sp,
                    color = CyanGlow,
                    fontWeight = FontWeight.SemiBold
                )
            }

            items(ChemistryUniverseData.WORLDS) { world ->
                val isUnlocked = progress.unlockedWorlds.contains(world.id)
                val wp = progress.worldProgressMap[world.id] ?: WorldProgress(world.id, isUnlocked)

                WorldItemCard(
                    world = world,
                    isUnlocked = isUnlocked,
                    stars = wp.stars,
                    score = wp.score,
                    currentLang = currentLang,
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
    isUnlocked: Boolean,
    stars: Int,
    score: Int,
    currentLang: String,
    onOpenLesson: () -> Unit,
    onOpenGame: () -> Unit
) {
    val borderColor = if (isUnlocked) BorderCyan else BorderSubtle

    SciFiCard(
        borderColor = borderColor,
        modifier = Modifier.testTag("world_card_${world.id}")
    ) {
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

                // Mini Game title tag
                Text(
                    text = "Mini-Game: ${if (currentLang == "bn") world.gameTitleBn else world.gameTitleEn}",
                    fontSize = 10.sp,
                    color = VioletNeon,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Stars
            Row {
                repeat(3) { starIdx ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        tint = if (starIdx < stars) AmberNeon else BorderSubtle,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Lesson Button
            OutlinedButton(
                onClick = onOpenLesson,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .testTag("world_${world.id}_lesson_btn"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CyanNeon
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderCyan)
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Lesson",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (currentLang == "bn") "পাঠ ও তত্ত্ব" else "Lesson",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Game Button
            Button(
                onClick = onOpenGame,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .testTag("world_${world.id}_game_btn"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VioletDeep,
                    contentColor = TextPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (currentLang == "bn") "গেম খেলুন" else "Play Game",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
