package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
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
import com.example.data.Strings
import com.example.model.TopicMastery
import com.example.model.UserProgress
import com.example.ui.components.NeonButton
import com.example.ui.components.SciFiCard
import com.example.ui.components.SciFiTopBar
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    progress: UserProgress,
    currentLang: String,
    onToggleLang: () -> Unit,
    onLaunchDailyChallenge: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            SciFiTopBar(
                title = Strings.progressTitle(currentLang),
                score = progress.totalScore,
                currentLang = currentLang,
                onToggleLang = onToggleLang,
                onBack = onBack,
                testTag = "dashboard_top_bar"
            )
        },
        containerColor = SpaceBlack
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Stats Overview Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = if (currentLang == "bn") "মোট স্কোর" else "Total Score",
                        value = "${progress.totalScore}",
                        accentColor = AmberNeon,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = if (currentLang == "bn") "ধারাবাহিকতা" else "Streak",
                        value = "${progress.streakDays} ${if (currentLang == "bn") "দিন" else "Days"}",
                        accentColor = RoseNeon,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = if (currentLang == "bn") "অন্বেষিত বিশ্ব" else "Worlds",
                        value = "${progress.unlockedWorlds.size}/10",
                        accentColor = CyanNeon,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Daily Challenge Card
            item {
                SciFiCard(borderColor = AmberNeon) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(AmberNeon.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Bolt, contentDescription = "Daily", tint = AmberNeon)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = Strings.dailyChallenge(currentLang),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = if (currentLang == "bn") "প্রতিদিনের দ্রুত রসায়ন ও মেমোরি পরীক্ষা (+২৫০ পয়েন্ট)" else "Daily cognitive recall quest (+250 Pts)",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        Button(
                            onClick = onLaunchDailyChallenge,
                            colors = ButtonDefaults.buttonColors(containerColor = AmberNeon, contentColor = SpaceBlack),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("launch_daily_btn")
                        ) {
                            Text(text = if (currentLang == "bn") "শুরু" else "Play", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Chemistry Mastery by Topic
            item {
                Text(
                    text = if (currentLang == "bn") "বিষয়ভিত্তিক রসায়ন পারদর্শিতা" else "Chemistry Mastery by Topic",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanNeon
                )
            }

            val topics = listOf(
                Pair("atoms", if (currentLang == "bn") "পরমাণু ও ইলেকট্রন স্তর" else "Atoms & Subatomic Particles"),
                Pair("periodic", if (currentLang == "bn") "পর্যায় সারণি ও মৌল" else "Periodic Table & Trends"),
                Pair("bonding", if (currentLang == "bn") "রাসায়নিক বন্ধন" else "Chemical Bonding"),
                Pair("formula", if (currentLang == "bn") "যৌগের সংকেত ও চার্জ" else "Formula Writing & Charges"),
                Pair("reactions", if (currentLang == "bn") "সমীকরণ সমতাকরণ" else "Reaction Balancing"),
                Pair("moles", if (currentLang == "bn") "মোল ও অ্যাভোগাড্রো গণনা" else "Mole Concept & Calculations"),
                Pair("ph", if (currentLang == "bn") "pH, অম্ল ও ক্ষার" else "pH & Acid-Base"),
                Pair("solutions", if (currentLang == "bn") "মোলারিটি ও লঘুকরণ" else "Molarity & Dilution"),
                Pair("organic", if (currentLang == "bn") "জৈব রসায়ন ও কার্যকরী মূলক" else "Organic Functional Groups"),
                Pair("pharma", if (currentLang == "bn") "ফার্মাসিউটিক্যাল ইনঅর্গানিক রসায়ন" else "Pharmaceutical Chemistry")
            )

            items(topics.size) { idx ->
                val (key, title) = topics[idx]
                val mastery = progress.topicMasteryMap[key] ?: TopicMastery(key)
                TopicMasteryRow(title = title, mastery = mastery, currentLang = currentLang)
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 10.sp, color = TextTertiary, maxLines = 1)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = accentColor)
        }
    }
}

@Composable
private fun TopicMasteryRow(
    title: String,
    mastery: TopicMastery,
    currentLang: String
) {
    SciFiCard(borderColor = BorderSubtle) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(
                    text = if (mastery.totalAttempts > 0)
                        "${if (currentLang == "bn") "সফলতা" else "Accuracy"}: ${mastery.accuracyPercentage}% (${mastery.correctCount}/${mastery.totalAttempts})"
                    else
                        (if (currentLang == "bn") "এখনো পর্যাপ্ত ডেটা নেই" else "Not enough data yet"),
                    fontSize = 11.sp,
                    color = if (mastery.totalAttempts > 0) EmeraldLight else TextTertiary
                )
            }

            // Mini progress bar
            val progressVal = if (mastery.totalAttempts > 0) (mastery.correctCount.toFloat() / mastery.totalAttempts) else 0f
            LinearProgressIndicator(
                progress = { progressVal },
                modifier = Modifier
                    .width(80.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = CyanNeon,
                trackColor = SurfaceNavy
            )
        }
    }
}
