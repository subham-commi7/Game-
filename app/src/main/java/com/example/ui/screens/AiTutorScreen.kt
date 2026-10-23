package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.ConnectionTestResult
import com.example.engine.FirebaseAiService
import com.example.engine.TutorContext
import com.example.engine.TutorResult
import com.example.ui.components.NeonButton
import com.example.ui.components.SciFiCard
import com.example.ui.components.SciFiTopBar
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import java.util.UUID

enum class MessageAuthor { STUDENT, TUTOR }

data class TutorChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val author: MessageAuthor,
    val text: String,
    val isCloud: Boolean = false,
    val statusTag: String? = null,
    val isError: Boolean = false,
    val rawQueryToRetry: String? = null
)

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
    var isSubmitting by remember { mutableStateOf(false) }
    var showDiagnosticDialog by remember { mutableStateOf(false) }
    var diagnosticResult by remember { mutableStateOf<ConnectionTestResult?>(null) }
    var isTestingConnection by remember { mutableStateOf(false) }

    val messages = remember { mutableStateListOf<TutorChatMessage>() }
    val listState = rememberLazyListState()

    val presetChips = remember(currentLang, tutorContext.levelTitle) {
        if (currentLang == "bn") {
            listOf(
                "সহজ বাংলায় ধারণাটি বুঝিয়ে দিন",
                "চিত্র ও কণার সম্পর্ক ব্যাখ্যা করুন",
                "আমাকে একটি কার্যকর হিন্ট দিন",
                "আমার উত্তরটি কেন ভুল হলো?",
                "বি.ফার্ম ও ফার্মাসিউটিক্যাল সংযোগ কী?",
                "দ্রুত রিভিশন সারসংক্ষেপ দিন",
                "পরমাণুর গঠন ও চার্জ কীভাবে হিসাব করব?",
                "মোলারিটি ও দ্রবণ প্রস্তুত করার নিয়ম কী?"
            )
        } else {
            listOf(
                "Explain this concept simply",
                "Explain the diagram & particles",
                "Give me a helpful hint",
                "Why was my previous answer wrong?",
                "What is the pharmaceutical connection?",
                "Give me a quick revision summary",
                "How to calculate atomic charge & mass?",
                "How to calculate solution molarity?"
            )
        }
    }

    // Initialize conversation with level briefing
    LaunchedEffect(tutorContext.levelNumber) {
        if (messages.isEmpty()) {
            val initialExplanation = aiService.generateOfflineVerifiedResponse(
                tutorContext,
                if (currentLang == "bn") "সহজ বাংলায় ধারণাটি বুঝিয়ে দিন" else "Explain this concept in simple English"
            )
            messages.add(
                TutorChatMessage(
                    author = MessageAuthor.TUTOR,
                    text = initialExplanation,
                    isCloud = false,
                    statusTag = "Curriculum Engine"
                )
            )
        }
    }

    // Scroll to latest message
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    fun executeQuery(queryText: String) {
        val trimmed = queryText.trim()
        if (trimmed.isEmpty() || isSubmitting) return

        // Add student message immediately
        messages.add(
            TutorChatMessage(
                author = MessageAuthor.STUDENT,
                text = trimmed
            )
        )

        isSubmitting = true
        scope.launch {
            val result = aiService.queryTutor(
                tutorContext.copy(userQueryPrompt = trimmed),
                trimmed
            )
            isSubmitting = false

            when (result) {
                is TutorResult.Success -> {
                    messages.add(
                        TutorChatMessage(
                            author = MessageAuthor.TUTOR,
                            text = result.responseText,
                            isCloud = result.isOnlineCloud,
                            statusTag = result.statusNote
                        )
                    )
                }
                is TutorResult.IncompleteSetup -> {
                    messages.add(
                        TutorChatMessage(
                            author = MessageAuthor.TUTOR,
                            text = result.offlineFallback,
                            isCloud = false,
                            statusTag = "Offline Curriculum (Setup Incomplete)"
                        )
                    )
                }
                is TutorResult.Error -> {
                    messages.add(
                        TutorChatMessage(
                            author = MessageAuthor.TUTOR,
                            text = "${result.offlineFallback}\n\n⚠️ [Cloud Request Note: ${result.errorMsg}]",
                            isCloud = false,
                            statusTag = "Offline Fallback (Network/Cloud Error)",
                            isError = true,
                            rawQueryToRetry = trimmed
                        )
                    )
                }
            }
        }
    }

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
        ) {
            // Connection Status & Diagnostic Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceNavy)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    val hasCloud = messages.any { it.isCloud }
                    Icon(
                        imageVector = if (hasCloud) Icons.Default.CloudDone else Icons.Default.Psychology,
                        contentDescription = "Status",
                        tint = if (hasCloud) EmeraldNeon else CyanNeon,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (hasCloud) "Gemini Cloud AI Active" else "NeuroVault Offline Curriculum Engine Active",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (hasCloud) EmeraldLight else CyanGlow
                        )
                        Text(
                            text = if (currentLang == "bn") "যেকোনো রসায়ন প্রশ্ন বাংলায় বা ইংরেজিতে লিখুন" else "Ask any chemistry inquiry in English or Bengali",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }

                IconButton(
                    onClick = {
                        showDiagnosticDialog = true
                        scope.launch {
                            isTestingConnection = true
                            diagnosticResult = aiService.testCloudConnection()
                            isTestingConnection = false
                        }
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Diagnostic",
                        tint = AmberNeon,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Active Level Topic Pill
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceCard)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${if (currentLang == "bn") "বর্তমান পাঠ" else "Level"}: ${tutorContext.levelTitle}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberNeon,
                        modifier = Modifier.weight(1f)
                    )
                    if (tutorContext.keyRule.isNotBlank()) {
                        Text(
                            text = "Rule: ${tutorContext.keyRule.take(24)}...",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Quick Preset Suggestions Row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(presetChips) { chip ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceNavy)
                            .border(1.dp, BorderCyan, RoundedCornerShape(8.dp))
                            .clickable(enabled = !isSubmitting) {
                                executeQuery(chip)
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("tutor_chip_${chip.hashCode()}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = chip, fontSize = 11.sp, color = TextPrimary)
                        }
                    }
                }
            }

            HorizontalDivider(color = BorderSubtle, thickness = 1.dp)

            // Conversation Message Thread
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    ChatBubble(
                        message = msg,
                        currentLang = currentLang,
                        onRetry = { retryQuery ->
                            executeQuery(retryQuery)
                        }
                    )
                }

                if (isSubmitting) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceNavy)
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = CyanNeon,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (currentLang == "bn") "টিউটর বৈজ্ঞানিক যুক্তি বিশ্লেষণ করছে..." else "Tutor is analyzing scientific concepts...",
                                fontSize = 12.sp,
                                color = CyanGlow
                            )
                        }
                    }
                }
            }

            // Bottom Input Bar
            Surface(
                color = SurfaceNavy,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = userCustomQuery,
                        onValueChange = { userCustomQuery = it },
                        placeholder = {
                            Text(
                                text = if (currentLang == "bn") "যেকোনো রসায়ন প্রশ্ন বাংলায় বা ইংরেজিতে লিখুন..." else "Ask any chemistry inquiry in English/Bengali...",
                                fontSize = 12.sp,
                                color = TextTertiary
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = SpaceBlack,
                            unfocusedContainerColor = SpaceBlack,
                            focusedIndicatorColor = CyanNeon,
                            unfocusedIndicatorColor = BorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isSubmitting,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("tutor_query_input")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            val q = userCustomQuery.trim()
                            if (q.isNotEmpty()) {
                                executeQuery(q)
                                userCustomQuery = ""
                            }
                        },
                        enabled = !isSubmitting && userCustomQuery.isNotBlank(),
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (userCustomQuery.isNotBlank()) CyanNeon else SurfaceCard)
                            .testTag("tutor_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = if (userCustomQuery.isNotBlank()) SpaceBlack else TextTertiary
                        )
                    }
                }
            }
        }
    }

    // Connection Diagnostics Dialog
    if (showDiagnosticDialog) {
        AlertDialog(
            onDismissRequest = { showDiagnosticDialog = false },
            title = {
                Text(
                    text = if (currentLang == "bn") "AI সংযোগ স্ট্যাটাস ও ডায়াগনস্টিক" else "AI Connection Diagnostics",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanNeon
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (isTestingConnection) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = CyanNeon, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Testing connection...", fontSize = 12.sp, color = TextPrimary)
                        }
                    } else if (diagnosticResult != null) {
                        Text(
                            text = diagnosticResult!!.statusTitle,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (diagnosticResult!!.isOnline) EmeraldLight else AmberNeon
                        )
                        Text(
                            text = diagnosticResult!!.detailedReason,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    } else {
                        Text(
                            text = "Tap 'Test Connection' to verify live Google Gemini connectivity via Firebase AI Logic.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ℹ️ Offline Guarantee: When online Firebase credentials are not provisioned, NEUROVAULT seamlessly runs the built-in offline curriculum engine with 100% verified chemistry knowledge.",
                        fontSize = 10.sp,
                        color = CyanGlow
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            isTestingConnection = true
                            diagnosticResult = aiService.testCloudConnection()
                            isTestingConnection = false
                        }
                    }
                ) {
                    Text(text = "Test Connection", color = CyanNeon)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiagnosticDialog = false }) {
                    Text(text = "Close", color = TextSecondary)
                }
            },
            containerColor = SurfaceNavy
        )
    }
}

@Composable
fun ChatBubble(
    message: TutorChatMessage,
    currentLang: String,
    onRetry: (String) -> Unit
) {
    val isStudent = message.author == MessageAuthor.STUDENT

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isStudent) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 330.dp)
                .clip(RoundedCornerShape(
                    topStart = 14.dp,
                    topEnd = 14.dp,
                    bottomStart = if (isStudent) 14.dp else 2.dp,
                    bottomEnd = if (isStudent) 2.dp else 14.dp
                ))
                .background(if (isStudent) SurfaceNavy else SurfaceCard)
                .border(
                    1.dp,
                    if (isStudent) BorderCyan else if (message.isCloud) BorderEmerald else BorderSubtle,
                    RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (isStudent) 14.dp else 2.dp,
                        bottomEnd = if (isStudent) 2.dp else 14.dp
                    )
                )
                .padding(12.dp)
        ) {
            // Header for tutor messages
            if (!isStudent) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (message.isCloud) Icons.Default.CloudDone else Icons.Default.Psychology,
                            contentDescription = null,
                            tint = if (message.isCloud) EmeraldNeon else CyanNeon,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (message.isCloud) "Gemini 2.5 Flash" else "NeuroVault Tutor",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (message.isCloud) EmeraldLight else CyanGlow
                        )
                    }

                    if (message.statusTag != null) {
                        Text(
                            text = message.statusTag,
                            fontSize = 9.sp,
                            color = TextTertiary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Message Body
            Text(
                text = message.text,
                fontSize = 12.sp,
                color = TextPrimary,
                lineHeight = 17.sp,
                modifier = if (!isStudent) Modifier.testTag("tutor_response_text") else Modifier
            )

            // Retry action if failed
            if (message.isError && message.rawQueryToRetry != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SpaceBlack)
                        .clickable { onRetry(message.rawQueryToRetry) }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Retry", tint = AmberNeon, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (currentLang == "bn") "পুনরায় চেষ্টা করুন" else "Retry Query",
                        fontSize = 10.sp,
                        color = AmberNeon,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
