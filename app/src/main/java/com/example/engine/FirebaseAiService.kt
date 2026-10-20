package com.example.engine

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.ai.FirebaseAI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class TutorContext(
    val language: String = "bn",
    val worldId: Int = 1,
    val levelNumber: Int = 1,
    val levelTitle: String = "",
    val objective: String = "",
    val conceptExplanation: String = "",
    val keyRule: String = "",
    val pharmacyRelevance: String = "",
    val lastSubmittedAnswer: String = "",
    val isCorrect: Boolean = false,
    val expectedAnswer: String = "",
    val userQueryPrompt: String = ""
)

sealed class TutorResult {
    data class Success(val responseText: String, val isOnlineCloud: Boolean) : TutorResult()
    data class IncompleteSetup(val message: String, val offlineFallback: String) : TutorResult()
    data class Error(val errorMsg: String, val offlineFallback: String) : TutorResult()
}

class FirebaseAiService(private val context: Context) {

    /**
     * Checks if Firebase is initialized in the application environment.
     */
    fun isFirebaseInitialized(): Boolean {
        return try {
            FirebaseApp.getApps(context).isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Asks the Gemini AI Chemistry Tutor for conceptual reasoning, hints, or B.Pharm context.
     * Guaranteed never to throw or crash. Falls back gracefully to verified offline curriculum tutor.
     */
    suspend fun queryTutor(
        tutorContext: TutorContext,
        presetChip: String? = null
    ): TutorResult = withContext(Dispatchers.IO) {
        val query = presetChip ?: tutorContext.userQueryPrompt.ifBlank {
            if (tutorContext.language == "bn") "এই ধারণাটি সহজ বাংলায় বুঝিয়ে দিন" else "Explain this concept in simple English"
        }

        val offlineFallback = generateOfflineVerifiedResponse(tutorContext, query)

        if (!isFirebaseInitialized()) {
            return@withContext TutorResult.IncompleteSetup(
                message = if (tutorContext.language == "bn")
                    "AI টিউটর অনলাইন সেটআপ অসম্পূর্ণ। ফায়ারবেস কনসোলে Firebase AI Logic সংযুক্ত করুন। (ভেরিফায়েড অফলাইন টিউটর মোড সক্রিয়)"
                else
                    "AI Tutor online setup is incomplete in Firebase Console. (Running in verified offline curriculum mode)",
                offlineFallback = offlineFallback
            )
        }

        try {
            // Use official Firebase AI Logic SDK with supported model
            val generativeModel = FirebaseAI.getInstance().generativeModel("gemini-2.5-flash")

            val systemContextPrompt = buildString {
                appendLine("You are the NEUROVAULT AI Chemistry Tutor for B.Pharm students.")
                appendLine("Language requested: ${if (tutorContext.language == "bn") "Bengali (বাংলা) with English chemical terms" else "English"}.")
                appendLine("Current World: ${tutorContext.worldId}, Level: ${tutorContext.levelNumber} - ${tutorContext.levelTitle}")
                appendLine("Learning Objective: ${tutorContext.objective}")
                appendLine("Verified Scientific Concept: ${tutorContext.conceptExplanation}")
                appendLine("Golden Rule: ${tutorContext.keyRule}")
                if (tutorContext.pharmacyRelevance.isNotBlank()) {
                    appendLine("B.Pharm Curriculum Relevance: ${tutorContext.pharmacyRelevance}")
                }
                if (tutorContext.lastSubmittedAnswer.isNotBlank()) {
                    appendLine("Learner's Last Answer: ${tutorContext.lastSubmittedAnswer} (Correct: ${tutorContext.isCorrect})")
                    appendLine("Verified Expected Answer: ${tutorContext.expectedAnswer}")
                }
                appendLine()
                appendLine("Student Query: $query")
                appendLine()
                appendLine("Guidelines:")
                appendLine("1. Encourage understanding and scientific reasoning; do not just give away answers.")
                appendLine("2. Keep explanations clear, friendly, and accurate according to official PCI B.Pharm guidelines.")
                appendLine("3. Highlight relevant pharmaceutical and clinical significance.")
            }

            val response = generativeModel.generateContent(systemContextPrompt)
            val text = response.text?.trim()
            if (!text.isNullOrEmpty()) {
                TutorResult.Success(responseText = text, isOnlineCloud = true)
            } else {
                TutorResult.Success(responseText = offlineFallback, isOnlineCloud = false)
            }
        } catch (e: Exception) {
            // Handles missing API keys, network outages, quotas, or unconfigured Firebase AI backend
            TutorResult.Error(
                errorMsg = e.localizedMessage ?: "Network or Cloud AI Provider Error",
                offlineFallback = offlineFallback
            )
        }
    }

    /**
     * Scientifically verified offline fallback generating tailored explanations, hints,
     * worked steps, and B.Pharm relevance derived from local curriculum models.
     */
    fun generateOfflineVerifiedResponse(tc: TutorContext, query: String): String {
        val isBn = tc.language == "bn" || query.contains("বাংলা")

        return when {
            query.contains("বাংলা") || query.contains("Bengali") -> {
                buildString {
                    appendLine("⚛️ **নিউরোভল্ট টিউটর ব্যাখ্যা (বাংলা):**")
                    appendLine(tc.conceptExplanation)
                    appendLine()
                    appendLine("📌 **মূল সূত্র ও নিয়ম:** ${tc.keyRule}")
                    if (tc.pharmacyRelevance.isNotBlank()) {
                        appendLine("💊 **বি.ফার্ম প্রয়োগ:** ${tc.pharmacyRelevance}")
                    }
                }
            }
            query.contains("English") -> {
                buildString {
                    appendLine("⚛️ **NeuroVault Tutor Explanation (English):**")
                    appendLine(tc.conceptExplanation)
                    appendLine()
                    appendLine("📌 **Golden Rule:** ${tc.keyRule}")
                    if (tc.pharmacyRelevance.isNotBlank()) {
                        appendLine("💊 **B.Pharm Clinical Relevance:** ${tc.pharmacyRelevance}")
                    }
                }
            }
            query.contains("hint") || query.contains("হিন্ট") -> {
                if (isBn) {
                    "💡 **হিন্ট:** মনে রাখুন, ${tc.keyRule}। প্রশ্নটির সাথে মূল নিয়মটি মিলিয়ে দেখুন।"
                } else {
                    "💡 **Hint:** Remember: ${tc.keyRule}. Connect this fundamental rule directly to the question parameters."
                }
            }
            query.contains("wrong") || query.contains("ভুল") -> {
                if (isBn) {
                    buildString {
                        appendLine("🔍 **ভুল উত্তর বিশ্লেষণ:**")
                        if (tc.lastSubmittedAnswer.isNotBlank()) {
                            appendLine("আপনার দেওয়া উত্তর: '${tc.lastSubmittedAnswer}'।")
                        }
                        appendLine("সঠিক বৈজ্ঞানিক নীতি: ${tc.keyRule}।")
                        appendLine("মনে রাখুন, ${tc.conceptExplanation}")
                    }
                } else {
                    buildString {
                        appendLine("🔍 **Mistake Diagnosis:**")
                        if (tc.lastSubmittedAnswer.isNotBlank()) {
                            appendLine("Your submitted response: '${tc.lastSubmittedAnswer}'.")
                        }
                        appendLine("Governing scientific rule: ${tc.keyRule}.")
                        appendLine("Key insight: ${tc.conceptExplanation}")
                    }
                }
            }
            query.contains("B.Pharm") || query.contains("ফার্ম") -> {
                if (isBn) {
                    "💊 **বি.ফার্ম পাঠ্যক্রমীয় গুরুত্ব:**\n${tc.pharmacyRelevance.ifBlank { "এই মৌলিক রসায়ন ধারণাটি ফার্মাসিউটিক্যাল ইনঅর্গানিক ও ফিজিক্যাল ফার্মাসিউটিকসে সরাসরি ব্যবহৃত হয়।" }}"
                } else {
                    "💊 **B.Pharm Curriculum Connection:**\n${tc.pharmacyRelevance.ifBlank { "This foundational chemistry concept is central to Pharmaceutical Inorganic Chemistry (BP106T) and Pharmaceutics." }}"
                }
            }
            query.contains("summary") || query.contains("সারসংক্ষেপ") -> {
                if (isBn) {
                    "📝 **দ্রুত রিভিশন সারসংক্ষেপ:**\n• স্তর: ${tc.levelTitle}\n• মূল বিষয়: ${tc.objective}\n• নিয়ম: ${tc.keyRule}"
                } else {
                    "📝 **Quick Revision Summary:**\n• Level: ${tc.levelTitle}\n• Core Concept: ${tc.objective}\n• Golden Rule: ${tc.keyRule}"
                }
            }
            query.contains("diagram") || query.contains("চিত্র") -> {
                if (isBn) {
                    "🔬 **চিত্র ব্যাখ্যা:**\nএই ইন্টারেক্টিভ চিত্রে রাসায়নিক উপাদান ও তাদের কণা/চার্জ অনুপাত সরাসরি প্রদর্শিত হচ্ছে। স্লাইডার বা বোতাম পরিবর্তন করে মানের পরিবর্তন লক্ষ্য করুন।"
                } else {
                    "🔬 **Diagram Breakdown:**\nThis interactive scientific diagram illustrates the real-time equilibrium and particle relationships. Adjusting parameters visibly updates the charge and stoichiometric ledger."
                }
            }
            else -> {
                if (isBn) {
                    buildString {
                        appendLine("⚛️ **রসায়ন টিউটর পর্যালোচনা:**")
                        appendLine(tc.conceptExplanation)
                        appendLine("সূত্র: ${tc.keyRule}")
                        if (tc.pharmacyRelevance.isNotBlank()) {
                            appendLine("বি.ফার্ম সংযোগ: ${tc.pharmacyRelevance}")
                        }
                    }
                } else {
                    buildString {
                        appendLine("⚛️ **Chemistry Tutor Guidance:**")
                        appendLine(tc.conceptExplanation)
                        appendLine("Key Rule: ${tc.keyRule}")
                        if (tc.pharmacyRelevance.isNotBlank()) {
                            appendLine("Pharmacy Application: ${tc.pharmacyRelevance}")
                        }
                    }
                }
            }
        }
    }
}
