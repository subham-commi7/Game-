package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Strings
import com.example.model.UserProgress
import com.example.ui.components.SciFiCard
import com.example.ui.components.SciFiTopBar
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    progress: UserProgress,
    currentLang: String,
    onToggleLang: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            SciFiTopBar(
                title = Strings.homeTitle(currentLang),
                score = progress.totalScore,
                currentLang = currentLang,
                onToggleLang = onToggleLang,
                testTag = "home_top_bar"
            )
        },
        containerColor = SpaceBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Hero Banner: Quick Expedition Action
            SciFiCard(
                borderColor = BorderCyan,
                modifier = Modifier
                    .clickable { onNavigate("expedition") }
                    .testTag("home_hero_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(listOf(CyanNeon, VioletDeep))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RocketLaunch,
                            contentDescription = "Expedition",
                            tint = SpaceBlack,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = Strings.startExpedition(currentLang),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanNeon
                        )
                        Text(
                            text = if (currentLang == "bn")
                                "১০টি বিশ্বের মধ্য দিয়ে আপনার যাত্রা চালিয়ে যান"
                            else
                                "Continue voyage through 10 Chemistry Worlds",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Go",
                        tint = CyanNeon
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Streak & Status Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Streak
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocalFireDepartment, contentDescription = "Streak", tint = AmberNeon)
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(Strings.streakLabel(currentLang, progress.streakDays), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(if (currentLang == "bn") "নিয়মিত অনুশীলন" else "Daily Recall", fontSize = 10.sp, color = TextTertiary)
                        }
                    }
                }

                // Worlds unlocked
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Public, contentDescription = "Worlds", tint = EmeraldNeon)
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text("${progress.unlockedWorlds.size}/10 ${if (currentLang == "bn") "বিশ্ব" else "Worlds"}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(if (currentLang == "bn") "উন্মুক্ত অঞ্চল" else "Explored", fontSize = 10.sp, color = TextTertiary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Navigation Grid
            val menuItems = listOf(
                MenuItem("chemistry_universe", Strings.chemistryUniverse(currentLang), if (currentLang == "bn") "১০টি বিজ্ঞান অঞ্চল" else "10 Regions", Icons.Default.AutoAwesome, CyanNeon, "nav_chemistry_universe"),
                MenuItem("ai_tutor", if (currentLang == "bn") "AI রসায়ন টিউটর" else "AI Chemistry Tutor", if (currentLang == "bn") "জেমিনাই বিজ্ঞান সহায়ক" else "Gemini Mentor", Icons.Default.AutoAwesome, CyanGlow, "nav_ai_tutor"),
                MenuItem("memory_universe", Strings.memoryUniverse(currentLang), if (currentLang == "bn") "৫টি স্মৃতি মোড" else "5 Core Modes", Icons.Default.Psychology, VioletNeon, "nav_memory_universe"),
                MenuItem("daily_challenge", Strings.dailyChallenge(currentLang), if (currentLang == "bn") "দৈনিক কুইজ" else "Daily Quest", Icons.Default.Bolt, AmberNeon, "nav_daily_challenge"),
                MenuItem("practice_lab", Strings.practiceLab(currentLang), if (currentLang == "bn") "মুক্ত ল্যাব" else "Sandbox", Icons.Default.Biotech, EmeraldNeon, "nav_practice_lab"),
                MenuItem("formula_forge", Strings.formulaForge(currentLang), if (currentLang == "bn") "আয়ন সিন্থেসিস" else "Ion Crafter", Icons.Default.Science, CyanGlow, "nav_formula_forge"),
                MenuItem("notebook", Strings.chemistryNotebook(currentLang), if (currentLang == "bn") "সূত্র ও সিলেবাস" else "Formulas & Rules", Icons.Default.MenuBook, RoseLight, "nav_notebook"),
                MenuItem("progress", Strings.myProgress(currentLang), if (currentLang == "bn") "বিশ্লেষণ ও পরিসংখ্যান" else "Analytics", Icons.Default.Leaderboard, VioletDeep, "nav_progress"),
                MenuItem("settings", Strings.settings(currentLang), if (currentLang == "bn") "ভাষা ও শব্দ" else "Language & Audio", Icons.Default.Settings, TextSecondary, "nav_settings")
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 12.dp)
            ) {
                items(menuItems.size) { idx ->
                    val item = menuItems[idx]
                    MenuCard(
                        item = item,
                        onClick = { onNavigate(item.destination) }
                    )
                }
            }
        }
    }
}

private data class MenuItem(
    val destination: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accentColor: Color,
    val testTag: String
)

@Composable
private fun MenuCard(
    item: MenuItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .border(1.dp, item.accentColor.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag(item.testTag),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(item.accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = item.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1
                )
                Text(
                    text = item.subtitle,
                    fontSize = 11.sp,
                    color = TextTertiary,
                    maxLines = 1
                )
            }
        }
    }
}
