package com.example.data

object Strings {

    // App identity
    const val APP_NAME = "NEUROVAULT"
    const val APP_FULL_TITLE = "NEUROVAULT: CHEMISTRY MEMORY UNIVERSE"
    const val TAGLINE_EN = "Train your memory. Master chemistry. Explore the unknown."
    const val TAGLINE_BN = "স্মৃতিকে শাণিত করুন। রসায়নকে জয় করুন। মহাবিশ্ব অন্বেষণ করুন।"

    // Screen Titles
    fun homeTitle(lang: String): String = if (lang == "bn") "নিউরো-ভল্ট নিয়ন্ত্রণ কেন্দ্র" else "NeuroVault Command Hub"
    fun expeditionTitle(lang: String): String = if (lang == "bn") "রসায়ন গ্যালাক্সি মানচিত্র" else "Chemistry Universe Map"
    fun memoryHubTitle(lang: String): String = if (lang == "bn") "স্মৃতি অভিযানের ৫টি মোড" else "Memory Universe Modes"
    fun dailyChallengeTitle(lang: String): String = if (lang == "bn") "দৈনিক মহাজাগতিক চ্যালেঞ্জ" else "Daily Cosmic Challenge"
    fun practiceLabTitle(lang: String): String = if (lang == "bn") "ভার্চুয়াল রসায়নাগার" else "Virtual Chemistry Lab"
    fun notebookTitle(lang: String): String = if (lang == "bn") "রসায়ন নোটবুক ও সূত্র ভাণ্ডার" else "Chemistry Notebook"
    fun formulaForgeTitle(lang: String): String = if (lang == "bn") "আয়ন ও সংকেত কর্মশালা" else "Formula Forge"
    fun progressTitle(lang: String): String = if (lang == "bn") "শিক্ষা ও স্মৃতির অগ্রগতি" else "Learning & Memory Analytics"
    fun settingsTitle(lang: String): String = if (lang == "bn") "সিস্টেম সেটিংস" else "System Settings"
    fun syllabusTitle(lang: String): String = if (lang == "bn") "বি.ফার্ম সিলেবাস ম্যাপিং" else "B.Pharm Syllabus Mapping"

    // Action buttons
    fun startExpedition(lang: String): String = if (lang == "bn") "অভিযান শুরু করুন" else "Start Expedition"
    fun chemistryUniverse(lang: String): String = if (lang == "bn") "রসায়ন জগৎ (১০টি বিশ্ব)" else "Chemistry Universe (10 Worlds)"
    fun memoryUniverse(lang: String): String = if (lang == "bn") "স্মৃতি প্রশিক্ষণ (৫টি মোড)" else "Memory Universe (5 Modes)"
    fun dailyChallenge(lang: String): String = if (lang == "bn") "দৈনিক চ্যালেঞ্জ" else "Daily Challenge"
    fun practiceLab(lang: String): String = if (lang == "bn") "অনুশীলন ল্যাব" else "Practice Lab"
    fun chemistryNotebook(lang: String): String = if (lang == "bn") "রসায়ন নোটবুক" else "Chemistry Notebook"
    fun formulaForge(lang: String): String = if (lang == "bn") "সংকেত ফোর্জ" else "Formula Forge"
    fun myProgress(lang: String): String = if (lang == "bn") "আমার অগ্রগতি" else "My Progress"
    fun settings(lang: String): String = if (lang == "bn") "সেটিংস" else "Settings"
    fun bpharmSyllabus(lang: String): String = if (lang == "bn") "বি.ফার্ম পাঠ্যক্রম" else "B.Pharm Curriculum"

    // Buttons
    fun submit(lang: String): String = if (lang == "bn") "যাচাই করুন" else "Submit"
    fun next(lang: String): String = if (lang == "bn") "পরবর্তী ধাপ" else "Next Step"
    fun retry(lang: String): String = if (lang == "bn") "পুনরায় চেষ্টা" else "Try Again"
    fun hint(lang: String): String = if (lang == "bn") "ইঙ্গিত / সাহায্য" else "Hint"
    fun explainWhy(lang: String): String = if (lang == "bn") "ব্যাখ্যা ও বৈজ্ঞানিক যুক্তি" else "Scientific Explanation"
    fun back(lang: String): String = if (lang == "bn") "ফিরে যান" else "Back"
    fun bookmark(lang: String): String = if (lang == "bn") "সংরক্ষণ" else "Bookmark"
    fun bookmarked(lang: String): String = if (lang == "bn") "সংরক্ষিত" else "Bookmarked"
    fun synthesize(lang: String): String = if (lang == "bn") "যৌগ তৈরি করুন" else "Synthesize Compound"
    fun clear(lang: String): String = if (lang == "bn") "মুছে ফেলুন" else "Clear"

    // Feedback
    fun correct(lang: String): String = if (lang == "bn") "অসাধারণ! সঠিক উত্তর।" else "Correct! Outstanding precision."
    fun incorrect(lang: String): String = if (lang == "bn") "সঠিক হয়নি। যুক্তিটি পর্যালোচনা করুন।" else "Incorrect. Review the scientific reasoning."
    fun streakLabel(lang: String, days: Int): String = if (lang == "bn") "ধারাবাহিকতা: $days দিন" else "Streak: $days Days"
    fun scoreLabel(lang: String, pts: Int): String = if (lang == "bn") "মোট পয়েন্ট: $pts" else "Total Points: $pts"
    fun unlocked(lang: String): String = if (lang == "bn") "উন্মুক্ত" else "Unlocked"
    fun locked(lang: String): String = if (lang == "bn") "তালাবদ্ধ" else "Locked"

    // Key bilingual concepts
    val BILINGUAL_TERMS = listOf(
        "Atomic Number" to "পারমাণবিক সংখ্যা (Z)",
        "Mass Number" to "ভর সংখ্যা (A)",
        "Valency" to "যোজনী",
        "Oxidation State" to "জারণ অবস্থা",
        "Mole" to "মোল (n)",
        "Molarity" to "মোলারিটি (M)",
        "Acid & Base" to "অম্ল ও ক্ষার",
        "Functional Group" to "কার্যকরী মূলক",
        "Chemical Equation" to "রাসায়নিক সমীকরণ",
        "Polyatomic Ion" to "বহু-পারমাণবিক মূলক",
        "Buffer Solution" to "বাফার দ্রবণ",
        "Titration" to "টাইট্রেশন",
        "Stoichiometry" to "স্টয়কিওমিতি"
    )
}
