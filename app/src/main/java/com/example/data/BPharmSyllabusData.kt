package com.example.data

import com.example.model.SyllabusTopic

object BPharmSyllabusData {

    /**
     * B.Pharm Curriculum Mapping aligned with the official Pharmacy Council of India (PCI)
     * revised B.Pharm curriculum as per NEP 2020 (Implementation session 2026–27).
     *
     * In the official 2026 PCI syllabus:
     * - BP106T: "Pharmaceutical Inorganic and Analytical Chemistry (Theory)" covers inorganic pharmaceuticals,
     *   volumetric titrations, antacids, limit tests, and electrolytes in Semester I (VERIFIED).
     * - Semester II and III courses retain foundational organic and physicochemical topics (PROVISIONAL).
     */
    val SYLLABUS_TOPICS: List<SyllabusTopic> = listOf(
        SyllabusTopic(
            code = "BP106T-U1",
            subjectEn = "Pharmaceutical Inorganic and Analytical Chemistry",
            subjectBn = "ফার্মাসিউটিক্যাল ইনঅর্গানিক ও অ্যানালিটিক্যাল রসায়ন",
            topicEn = "Impurities in Pharmaceutical Substances & Limit Tests",
            topicBn = "ঔষধ উপাদানে অপদ্রব্য ও লিমিট টেস্ট",
            semester = "Semester I",
            alignmentStatus = "VERIFIED",
            descriptionEn = "Sources of impurities in active pharmaceutical ingredients (APIs); principle and procedure of limit tests for chloride, sulfate, iron, and heavy metals.",
            descriptionBn = "সক্রিয় ঔষধ উপাদানে অপদ্রব্যের উৎস; ক্লোরাইড, সালফেট, আয়রন ও ভারী ধাতুর গুণগত লিমিট টেস্ট বিশ্লেষণ।"
        ),
        SyllabusTopic(
            code = "BP106T-U2",
            subjectEn = "Pharmaceutical Inorganic and Analytical Chemistry",
            subjectBn = "ফার্মাসিউটিক্যাল ইনঅর্গানিক ও অ্যানালিটিক্যাল রসায়ন",
            topicEn = "Volumetric Analysis: Acid-Base Titration & Neutralization Indicators",
            topicBn = "আয়তনিক বিশ্লেষণ: অম্ল-ক্ষার টাইট্রেশন ও প্রশমন নির্দেশক",
            semester = "Semester I",
            alignmentStatus = "VERIFIED",
            descriptionEn = "Theories of acid-base indicators, neutralization curves, primary/secondary standards, and volumetric assay of sodium hydroxide and hydrochloric acid.",
            descriptionBn = "অম্ল-ক্ষার নির্দেশকের তত্ত্ব, প্রশমন কার্ভ, প্রাইমারি ও সেকেন্ডারি স্ট্যান্ডার্ড দ্রবণ এবং সোডিয়াম হাইড্রোক্সাইডের টাইট্রেশন বিশ্লেষণ।"
        ),
        SyllabusTopic(
            code = "BP106T-U3",
            subjectEn = "Pharmaceutical Inorganic and Analytical Chemistry",
            subjectBn = "ফার্মাসিউটিক্যাল ইনঅর্গানিক ও অ্যানালিটিক্যাল রসায়ন",
            topicEn = "Gastrointestinal Agents: Antacids, Acidifiers & Protectives",
            topicBn = "গ্যাস্ট্রোইনটেস্টাইনাল এজেন্ট: অ্যান্টাসিড, এসিডিফায়ার ও প্রটেক্টিভ",
            semester = "Semester I",
            alignmentStatus = "VERIFIED",
            descriptionEn = "Mechanism, preparation, and assay of inorganic antacids: Aluminium hydroxide gel, Magnesium hydroxide, Calcium carbonate; and systemic acidifiers (Ammonium chloride).",
            descriptionBn = "অজৈব অ্যান্টাসিডের কার্যপদ্ধতি ও প্রস্তুতি: অ্যালুমিনিয়াম হাইড্রক্সাইড, ম্যাগনেসিয়াম হাইড্রক্সাইড, ক্যালসিয়াম কার্বনেট ও নিশাদল।"
        ),
        SyllabusTopic(
            code = "BP106T-U4",
            subjectEn = "Pharmaceutical Inorganic and Analytical Chemistry",
            subjectBn = "ফার্মাসিউটিক্যাল ইনঅর্গানিক ও অ্যানালিটিক্যাল রসায়ন",
            topicEn = "Major Intra- & Extracellular Electrolytes & Isotonic Solutions",
            topicBn = "প্রধান আন্তঃ ও বহিঃকোষীয় ইলেক্ট্রোলাইট ও আইসোটোনিক স্যালাইন",
            semester = "Semester I",
            alignmentStatus = "VERIFIED",
            descriptionEn = "Physiological functions of Na⁺, K⁺, Ca²⁺, Cl⁻, and bicarbonate ions; formulation of Oral Rehydration Salts (ORS) and 0.9% w/v isotonic sodium chloride saline.",
            descriptionBn = "দেহে Na⁺, K⁺, Ca²⁺, Cl⁻ আয়নের শারীরবৃত্তীয় গুরুত্ব; ওআরএস ও ০.৯% সোডিয়াম ক্লোরাইড আইসোটোনিক স্যালাইন প্রস্তুতি।"
        ),
        SyllabusTopic(
            code = "BP202T-U1",
            subjectEn = "Pharmaceutical Organic Chemistry I",
            subjectBn = "ফার্মাসিউটিক্যাল জৈব রসায়ন ১",
            topicEn = "IUPAC Nomenclature, Carbon Hybridization & Structural Isomerism",
            topicBn = "IUPAC নামকরণ, কার্বনের সংকরণ ও গাঠনিক সমাণুতা",
            semester = "Semester II",
            alignmentStatus = "PROVISIONAL",
            descriptionEn = "Classification of organic compounds, IUPAC nomenclature rules, functional group priority, and chain/position/functional isomerism.",
            descriptionBn = "জৈব যৌগের শ্রেণীবিভাগ, IUPAC নামকরণের নিয়মাবলী, কার্যকরী মূলকের অগ্রাধিকার ও বিভিন্ন প্রকার সমাণুতা।"
        ),
        SyllabusTopic(
            code = "BP302T-U1",
            subjectEn = "Physical Pharmaceutics I",
            subjectBn = "ফিজিক্যাল ফার্মাসিউটিকস ১",
            topicEn = "pH, Buffers, Tonicity & Physicochemical Principles",
            topicBn = "pH, বাফার দ্রবণ, সম-অভিস্রবণিকতা ও ভৌত-রাসায়নিক মূলনীতি",
            semester = "Semester III",
            alignmentStatus = "PROVISIONAL",
            descriptionEn = "Sørensen's pH scale, Henderson-Hasselbalch equation, buffer capacity, biological buffer systems, and isotonicity adjustments for parenterals.",
            descriptionBn = "pH স্কেল, হেন্ডারসন-হ্যাসেলবাক সমীকরণ, বাফার ধারণক্ষমতা এবং ইনজেকশনযোগ্য ঔষধের সম-অভিস্রবণিকতা নিয়ন্ত্রণ।"
        )
    )
}
