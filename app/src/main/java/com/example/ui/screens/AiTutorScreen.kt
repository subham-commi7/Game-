package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.FirebaseAiService
import com.example.engine.TutorContext
import com.example.engine.TutorResult
import com.example.ui.components.NeonButton
import com.example.ui.components.SciFiCard
import com.example.ui.components.SciFiTopBar
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AiTutorScreen(
    tutorContext: TutorContext,
    currentLang: String,
    score: Int,
    onToggleLang: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val aiService = remember { FirebaseAiService(context) }

    var userCustomQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var tutorAnswer by remember { mutableStateOf<String?>(null) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isCloudMode by remember { mutableStateOf(false) }

    val presetChips = remember(currentLang) {
        if (currentLang == "bn") {
            listOf(
                "সহজ বাংলায় ধারণাটি বুঝিয়ে দিন",
                "চিত্রটি ব্যাখ্যা করুন",
                "আমাকে একটি হিন্ট দিন",
                "আমার উত্তরটি কেন ভুল হলো?",
                "বি.ফার্ম এর সাথে সংযোগ কী?",
                "দ্রুত রিভিশন সারসংক্ষেপ",
                "অনুরূপ উদাহরণ সমাধান করুন",
                "Explain in English"
            )
        } else {
            listOf(
                "Explain this concept in simple English",
                "Explain this diagram",
                "Give me a hint",
                "Explain why my answer was wrong",
                "Connect this concept to B.Pharm",
                "Give me a quick revision summary",
                "Solve a similar example step by step",
                "সহজ বাংলায় ধারণাটি বুঝিয়ে দিন"
            )
        }
    }

    // Auto load initial explanation on screen entry
    LaunchedEffect(tutorContext.levelNumber) {
        isLoading = true
        val result = aiService.queryTutor(tutorContext, presetChips[0])
        isLoading = false
        when (result) {
            is TutorResult.Success -> {
                tutorAnswer = result.responseText
                isCloudMode = result.isOnlineCloud
                statusMessage = if (result.isOnlineCloud) "Cloud Gemini Active" else "Curriculum Tutor Mode"
            }
            is TutorResult.IncompleteSetup -> {
                tutorAnswer = result.offlineFallback
                isCloudMode = false
                statusMessage = result.message
            }
            is TutorResult.Error -> {
                tutorAnswer = result.offlineFallback
                isCloudMode = false
                statusMessage = "Offline Mode: ${result.errorMsg}"
            }
        }
    }

    fun submitQuery(queryText: String) {
        if (queryText.isBlank()) return
        scope.launch {
            isLoading = true
            val result = aiService.queryTutor(tutorContext.copy(userQueryPrompt = queryText), queryText)
            isLoading = false
            when (result) {
                is TutorResult.Success -> {
                    tutorAnswer = result.responseText
                    isCloudMode = result.isOnlineCloud
                    statusMessage = if (result.isOnlineCloud) "Cloud Gemini Active" else "Curriculum Tutor Mode"
                }
                is TutorResult.IncompleteSetup -> {
                    tutorAnswer = result.offlineFallback
                    isCloudMode = false
                    statusMessage = result.message
                }
                is TutorResult.Error -> {
                    tutorAnswer = result.offlineFallback
                    isCloudMode = false
                    statusMessage = "Offline Mode: ${result.errorMsg}"
                }
            }
        }
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            SciFiTopBar(
                title = if (currentLang == "bn") "AI রসায়ন টিউটর" else "Gemini Chemistry Tutor",
                score = score,
                currentLang = currentLang,
                onToggleLang = onToggleLang,
                onBack = onBack,
                testTag = "ai_tutor_top_bar"
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
            // Mode Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceNavy)
                    .border(1.dp, if (isCloudMode) EmeraldNeon else CyanNeon, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isCloudMode) Icons.Default.CloudDone else Icons.Default.Psychology,
                    contentDescription = "Mode",
                    tint = if (isCloudMode) EmeraldNeon else CyanNeon,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isCloudMode) "Gemini Cloud AI Connected" else "NeuroVault Offline Curriculum Tutor",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCloudMode) EmeraldLight else CyanGlow
                    )
                    if (statusMessage != null) {
                        Text(
                            text = statusMessage!!,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // Current Concept Card
            SciFiCard(borderColor = BorderSubtle) {
                Text(
                    text = "${if (currentLang == "bn") "বর্তমান পাঠ" else "Active Concept"}: ${tutorContext.levelTitle}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberNeon
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tutorContext.objective,
                    fontSize = 12.sp,
                    color = TextPrimary,
                    lineHeight = 16.sp
                )
                if (tutorContext.keyRule.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Rule: ${tutorContext.keyRule}",
                        fontSize = 11.sp,
                        color = CyanGlow,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Preset Chips Row
            Text(
                text = if (currentLang == "bn") "দ্রুত প্রশ্ন বা নির্দেশিকা নির্বাচন করুন:" else "Select Quick Tutor Inquiry:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(presetChips) { chip ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceCard)
                            .border(1.dp, BorderCyan, RoundedCornerShape(10.dp))
                            .clickable { submitQuery(chip) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .testTag("tutor_chip_${chip.hashCode()}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "AI", tint = CyanNeon, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = chip, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        }
                    }
                }
            }

            // Custom Question Input Field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, BorderCyan, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = userCustomQuery,
                    onValueChange = { userCustomQuery = it },
                    placeholder = {
                        Text(
                            text = if (currentLang == "bn") "আপনার রসায়ন প্রশ্নটি লিখুন..." else "Ask any chemistry question...",
                            fontSize = 12.sp,
                            color = TextTertiary
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                        focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("tutor_query_input")
                )

                IconButton(
                    onClick = {
                        val q = userCustomQuery.trim()
                        if (q.isNotEmpty()) {
                            submitQuery(q)
                            userCustomQuery = ""
                        }
                    },
                    modifier = Modifier.testTag("tutor_send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = CyanNeon
                    )
                }
            }

            // Tutor Response Area
            SciFiCard(borderColor = BorderCyan) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (currentLang == "bn") "টিউটরের নির্দেশনা ও বৈজ্ঞানিক বিশ্লেষণ" else "Tutor Guidance & Scientific Analysis",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanNeon
                    )
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = CyanNeon,
                            strokeWidth = 2.dp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (isLoading) {
                    Text(
                        text = if (currentLang == "bn") "টিউটর উত্তর তৈরি করছে..." else "Tutor is analyzing your request...",
                        fontSize = 12.sp,
                        color = TextTertiary
                    )
                } else if (tutorAnswer != null) {
                    Text(
                        text = tutorAnswer!!,
                        fontSize = 13.sp,
                        color = TextPrimary,
                        lineHeight = 19.sp,
                        modifier = Modifier.testTag("tutor_response_text")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
