package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Strings
import com.example.model.AppSettings
import com.example.ui.components.NeonButton
import com.example.ui.components.SciFiCard
import com.example.ui.components.SciFiTopBar
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    settings: AppSettings,
    score: Int,
    onUpdateSettings: (AppSettings) -> Unit,
    onResetProgress: () -> Unit,
    onBack: () -> Unit
) {
    var showResetDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            SciFiTopBar(
                title = Strings.settingsTitle(settings.language),
                score = score,
                currentLang = settings.language,
                onToggleLang = {
                    val nextLang = if (settings.language == "bn") "en" else "bn"
                    onUpdateSettings(settings.copy(language = nextLang))
                },
                onBack = onBack,
                testTag = "settings_top_bar"
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
            // Language Selection Card
            SciFiCard(borderColor = BorderCyan) {
                Text(
                    text = if (settings.language == "bn") "ভাষা নির্বাচন (Language)" else "Language Selection",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanNeon
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val isBn = settings.language == "bn"
                    Button(
                        onClick = { onUpdateSettings(settings.copy(language = "bn")) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("lang_bn_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBn) CyanNeon else SurfaceCardHover,
                            contentColor = if (isBn) SpaceBlack else TextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("বাংলা (Default)", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onUpdateSettings(settings.copy(language = "en")) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("lang_en_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isBn) CyanNeon else SurfaceCardHover,
                            contentColor = if (!isBn) SpaceBlack else TextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("English", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Audio & Sensory Toggles
            SciFiCard(borderColor = BorderViolet) {
                Text(
                    text = if (settings.language == "bn") "শব্দ ও হ্যাপটিক প্রতিক্রিয়া" else "Audio & Sensory Feedback",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletNeon
                )
                Spacer(modifier = Modifier.height(10.dp))

                SettingSwitchRow(
                    title = if (settings.language == "bn") "শব্দ প্রভাব (Sound Effects)" else "Sound Effects",
                    checked = settings.soundEnabled,
                    onCheckedChange = { onUpdateSettings(settings.copy(soundEnabled = it)) },
                    testTag = "switch_sound"
                )

                SettingSwitchRow(
                    title = if (settings.language == "bn") "হ্যাপটিক কম্পন (Haptics)" else "Haptic Vibration",
                    checked = settings.hapticsEnabled,
                    onCheckedChange = { onUpdateSettings(settings.copy(hapticsEnabled = it)) },
                    testTag = "switch_haptics"
                )

                SettingSwitchRow(
                    title = if (settings.language == "bn") "সীমিত মোশন মোড (Reduced Motion)" else "Reduced Motion",
                    checked = settings.reducedMotion,
                    onCheckedChange = { onUpdateSettings(settings.copy(reducedMotion = it)) },
                    testTag = "switch_reduced_motion"
                )

                SettingSwitchRow(
                    title = if (settings.language == "bn") "উচ্চ ভিজ্যুয়াল ইফেক্টস (High Effects)" else "High Visual Effects",
                    checked = settings.visualEffectsHigh,
                    onCheckedChange = { onUpdateSettings(settings.copy(visualEffectsHigh = it)) },
                    testTag = "switch_visual_effects"
                )
            }

            // Adaptive Challenge Pace
            SciFiCard(borderColor = BorderSubtle) {
                Text(
                    text = if (settings.language == "bn") "চ্যালেঞ্জের গতি ও কাঠিন্য" else "Adaptive Challenge Pace",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldNeon
                )
                Spacer(modifier = Modifier.height(8.dp))

                val levels = listOf("Relaxed", "Balanced", "Challenging")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (lvl in levels) {
                        val isSelected = settings.challengeLevel == lvl
                        OutlinedButton(
                            onClick = { onUpdateSettings(settings.copy(challengeLevel = lvl)) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("pace_$lvl"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) EmeraldNeon.copy(alpha = 0.2f) else SurfaceCard,
                                contentColor = if (isSelected) EmeraldLight else TextSecondary
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) EmeraldNeon else BorderSubtle)
                        ) {
                            Text(lvl, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Reset Progress Section
            SciFiCard(borderColor = RoseNeon) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = "Warning", tint = RoseNeon)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (settings.language == "bn") "অগ্রগতি রিসেট করুন" else "Reset Application Progress",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoseNeon
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (settings.language == "bn")
                        "আপনার সমস্ত অর্জিত পয়েন্ট, আনলককৃত বিশ্ব এবং সংরক্ষিত উত্তর মুছে ফেলে নতুন করে শুরু করুন।"
                    else
                        "Clear all earned points, unlocked worlds, and memory thread history to start fresh.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { showResetDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseNeon, contentColor = SpaceBlack),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reset_progress_trigger_btn")
                ) {
                    Icon(Icons.Default.DeleteForever, contentDescription = "Reset")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (settings.language == "bn") "সমস্ত ডেটা রিসেট করুন" else "Reset All Data",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Confirmation Dialog
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = {
                    Text(
                        text = if (settings.language == "bn") "আপনি কি নিশ্চিত?" else "Confirm Full Reset?",
                        color = RoseNeon,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = if (settings.language == "bn")
                            "এই কাজটি আর পূর্বাবস্থায় ফিরিয়ে আনা যাবে না। আপনার স্থানীয় স্কোর ও বিশ্ব অগ্রগতি সম্পূর্ণ মুছে যাবে।"
                        else
                            "This action cannot be undone. All local progress, memory threads, and stars will be permanently erased.",
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showResetDialog = false
                            onResetProgress()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RoseNeon, contentColor = SpaceBlack),
                        modifier = Modifier.testTag("confirm_reset_btn")
                    ) {
                        Text(if (settings.language == "bn") "হ্যাঁ, রিসেট করুন" else "Yes, Reset Everything", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showResetDialog = false },
                        modifier = Modifier.testTag("cancel_reset_btn")
                    ) {
                        Text(if (settings.language == "bn") "বাতিল" else "Cancel", color = TextSecondary)
                    }
                },
                containerColor = SurfaceNavy,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 13.sp, color = TextPrimary)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(testTag),
            colors = SwitchDefaults.colors(
                checkedThumbColor = SpaceBlack,
                checkedTrackColor = CyanNeon,
                uncheckedThumbColor = TextTertiary,
                uncheckedTrackColor = SurfaceCardHover
            )
        )
    }
}
