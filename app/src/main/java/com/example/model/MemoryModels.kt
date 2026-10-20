package com.example.model

enum class MemoryGameMode(
    val titleEn: String,
    val titleBn: String,
    val descEn: String,
    val descBn: String
) {
    ECHO_MAP(
        "Echo Map",
        "প্রতিধ্বনি মানচিত্র",
        "Observe the luminous orbital node sequence and reconstruct their precise pattern.",
        "উজ্জ্বল পরমাণু কক্ষপথের সংকেত ক্রমটি পর্যবেক্ষণ করুন এবং সঠিক ক্রমানুসারে পুনর্গঠন করুন।"
    ),
    TIMEFOLD(
        "Timefold",
        "টাইমফোল্ড রাসায়নিক পরিবর্তন",
        "Track chemical transformations across time and spot what shifted or reacted first.",
        "সময়ের সাথে রাসায়নিক দৃশ্যপট পর্যবেক্ষণ করে চিহ্নিত করুন কোন অণু স্থানান্তরিত বা পরিবর্তিত হয়েছে।"
    ),
    MEMORY_TRAP(
        "Memory Trap",
        "স্মৃতি ফাঁদ ও পার্থক্য নির্ণয়",
        "Distinguish genuine learned chemical structures from subtle altered distractors.",
        "পূর্বে দেখা রাসায়নিক সংকেত ও যৌগকে সূক্ষ্ম ভুল প্ররোচক থেকে আলাদা করুন।"
    ),
    LINKWEAVER(
        "Linkweaver",
        "সংযোগ বয়ন",
        "Weave mental threads connecting ions, charges, equations, and pharma applications.",
        "আয়ন, চার্জ, রাসায়নিক সমীকরণ এবং ফার্মাসিউটিক্যাল বৈশিষ্ট্যের মধ্যে সঠিক যোগসূত্র বাঁধুন।"
    ),
    DELAYED_ECHO(
        "Delayed Echo",
        "বিলম্বিত অনুরণন",
        "Recall previously encountered chemistry concepts from past expeditions.",
        "পূর্ববর্তী মিশন ও শিক্ষায় অর্জিত রসায়ন ধারণাগুলি দীর্ঘস্থায়ী স্মৃতি থেকে উদ্ধার করুন।"
    )
}

data class EchoNode(
    val id: Int,
    val label: String,
    val xRatio: Float,
    val yRatio: Float,
    val colorHex: Long
)

data class TimefoldEvent(
    val step: Int,
    val descriptionEn: String,
    val descriptionBn: String,
    val iconType: String,
    val isTarget: Boolean
)

data class LinkPairItem(
    val id: String,
    val leftText: String,
    val leftLabelBn: String,
    val rightText: String,
    val rightLabelBn: String,
    val category: String
)

data class MemoryRoundResult(
    val mode: MemoryGameMode,
    val score: Int,
    val accuracy: Float,
    val timeTakenSeconds: Int,
    val memoryThreadsCreated: Int
)
