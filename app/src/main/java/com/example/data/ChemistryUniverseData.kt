package com.example.data

import com.example.model.*

object ChemistryUniverseData {

    val WORLDS: List<ChemistryWorld> = listOf(
        ChemistryWorld(
            id = 1,
            key = "atoms",
            titleEn = "World 1 — Atom Observatory",
            titleBn = "বিশ্ব ১ — পরমাণু মানমন্দির",
            subtitleEn = "Subatomic particles, atomic number, electron shells, cations & anions",
            subtitleBn = "উপ-পারমাণবিক কণা, পারমাণবিক সংখ্যা, ইলেকট্রন শেল, ক্যাটায়ন ও অ্যানায়ন",
            gameTitleEn = "ATOM ASSEMBLER",
            gameTitleBn = "পরমাণু সংযোজক",
            iconName = "atom",
            learningObjectivesEn = listOf(
                "Differentiate protons, neutrons, and electrons",
                "Calculate Atomic Number (Z) and Mass Number (A)",
                "Determine net ionic charge and shell filling (2, 8, 8)"
            ),
            learningObjectivesBn = listOf(
                "প্রোটন, নিউট্রন এবং ইলেকট্রনের ভূমিকা বুঝুন",
                "পারমাণবিক সংখ্যা (Z) এবং ভর সংখ্যা (A) হিসাব করুন",
                "আয়ন চার্জ ও শক্তিস্তরে ইলেকট্রন বিন্যাস জানুন"
            )
        ),
        ChemistryWorld(
            id = 2,
            key = "periodic",
            titleEn = "World 2 — Periodic Galaxy",
            titleBn = "বিশ্ব ২ — পর্যায় গ্যালাক্সি",
            subtitleEn = "Element symbols, groups, periods, metals, valency vs oxidation state",
            subtitleBn = "মৌলের প্রতীক, গ্রুপ, পর্যায়, ধাতু-অধাতু, যোজনী বনাম জারণ অবস্থা",
            gameTitleEn = "ELEMENT HUNT",
            gameTitleBn = "মৌল সন্ধান",
            iconName = "table",
            learningObjectivesEn = listOf(
                "Locate elements by atomic number, group, and period",
                "Distinguish metals, nonmetals, and metalloids",
                "Understand valency vs variable oxidation states"
            ),
            learningObjectivesBn = listOf(
                "পারমাণবিক সংখ্যা, গ্রুপ ও পর্যায় অনুযায়ী মৌল শনাক্ত করুন",
                "ধাতু, অধাতু ও উপধাতুর বৈশিষ্ট্য চিহ্নিত করুন",
                "যোজনী ও পরিবর্তনশীল জারণ অবস্থার পার্থক্য বুঝুন"
            )
        ),
        ChemistryWorld(
            id = 3,
            key = "bonding",
            titleEn = "World 3 — Bonding Dimension",
            titleBn = "বিশ্ব ৩ — রাসায়নিক বন্ধন মাত্রা",
            subtitleEn = "Ionic & covalent bonds, Lewis octet rule, electronegativity, polar bonds",
            subtitleBn = "আয়নিক ও সমযোজী বন্ধন, লুইস অষ্টক নিয়ম, তড়িৎ ঋণাত্মকতা, পোলার বন্ধন",
            gameTitleEn = "BOND BUILDER",
            gameTitleBn = "বন্ধন নির্মাতা",
            iconName = "link",
            learningObjectivesEn = listOf(
                "Identify ionic electron transfer vs covalent sharing",
                "Apply the octet rule to simple molecules",
                "Differentiate polar and nonpolar covalent bonds"
            ),
            learningObjectivesBn = listOf(
                "আয়নিক ইলেকট্রন স্থানান্তর বনাম সমযোজী শেয়ারিং বুঝুন",
                "সাধারণ অণুতে অষ্টক নিয়ম প্রয়োগ করুন",
                "পোলার এবং অপোলার সমযোজী বন্ধনের পার্থক্য চিনুন"
            )
        ),
        ChemistryWorld(
            id = 4,
            key = "formula",
            titleEn = "World 4 — Formula Forge",
            titleBn = "বিশ্ব ৪ — সংকেত ফোর্জ",
            subtitleEn = "Ionic charges, polyatomic ions, subscripts, electrical neutrality",
            subtitleBn = "আয়নিক চার্জ, বহু-পারমাণবিক মূলক, সাবস্ক্রিপ্ট, তড়িৎ নিরপেক্ষতা",
            gameTitleEn = "FORMULA CRAFTER",
            gameTitleBn = "সংকেত কারিগর",
            iconName = "flask",
            learningObjectivesEn = listOf(
                "Memorize common monoatomic and polyatomic ions",
                "Calculate simplest whole-number ratio for neutral compounds",
                "Correctly apply parentheses for polyatomic ions (e.g., Al₂(SO₄)₃)"
            ),
            learningObjectivesBn = listOf(
                "গুরুত্বপূর্ণ ক্যাটায়ন, অ্যানায়ন ও মূলকের চার্জ মনে রাখুন",
                "তড়িৎ নিরপেক্ষ যৌগের ক্ষুদ্রতম পূর্ণসংখ্যা অনুপাত বের করুন",
                "বহু-পারমাণবিক মূলকে বন্ধনীর সঠিক ব্যবহার শিখুন"
            )
        ),
        ChemistryWorld(
            id = 5,
            key = "reactions",
            titleEn = "World 5 — Reaction Reactor",
            titleBn = "বিশ্ব ৫ — বিক্রিয়া চুল্লি",
            subtitleEn = "Balancing equations, conservation of mass, reaction types, redox",
            subtitleBn = "সমীকরণ সমতাকরণ, ভরের নিত্যতা, বিক্রিয়ার প্রকারভেদ, জারণ-বিজারণ",
            gameTitleEn = "REACTION STABILIZER",
            gameTitleBn = "বিক্রিয়া সমন্বয়ক",
            iconName = "flame",
            learningObjectivesEn = listOf(
                "Conserve atom count on both reactant and product sides",
                "Balance stoichiometric coefficients without altering chemical formulas",
                "Identify neutralization, precipitation, and synthesis"
            ),
            learningObjectivesBn = listOf(
                "বিক্রিয়ক ও উৎপাদে মৌলের পরমাণুর সংখ্যার ভারসাম্য বজায় রাখুন",
                "সংকেত অপরিবর্তিত রেখে সহগ (coefficient) দিয়ে সমতা করুন",
                "প্রশমন, অধঃক্ষেপণ ও সংযোজন বিক্রিয়া চিহ্নিত করুন"
            )
        ),
        ChemistryWorld(
            id = 6,
            key = "moles",
            titleEn = "World 6 — Mole Matrix",
            titleBn = "বিশ্ব ৬ — মোল ম্যাট্রিক্স",
            subtitleEn = "Molar mass, Avogadro constant, mass-to-mole and particle conversions",
            subtitleBn = "মোলার ভর, অ্যাভোগাড্রো ধ্রুবক, ভর থেকে মোল ও কণা সংখ্যার রূপান্তর",
            gameTitleEn = "MOLE NAVIGATOR",
            gameTitleBn = "মোল দিকনির্ণয়",
            iconName = "calculator",
            learningObjectivesEn = listOf(
                "Master n = m / M and N = n × Nₐ calculations",
                "Calculate accurate molar masses from atomic weights",
                "Understand stoichiometric mole ratios in reactions"
            ),
            learningObjectivesBn = listOf(
                "n = m / M এবং N = n × Nₐ সূত্রের সমাধান শিখুন",
                "পারমাণবিক ভর যোগ করে মোলার ভর নিখুঁতভাবে বের করুন",
                "রাসায়নিক বিক্রিয়ার স্টয়কিওমিতিক অনুপাত বুঝুন"
            )
        ),
        ChemistryWorld(
            id = 7,
            key = "ph",
            titleEn = "World 7 — pH Dimension",
            titleBn = "বিশ্ব ৭ — pH মাত্রা",
            subtitleEn = "Acids, bases, pH = -log[H⁺], pOH, indicators, buffer solutions",
            subtitleBn = "অম্ল, ক্ষার, pH = -log[H⁺], pOH, নির্দেশক, বাফার দ্রবণ",
            gameTitleEn = "pH ALCHEMIST",
            gameTitleBn = "pH রূপান্তরবিদ",
            iconName = "droplet",
            learningObjectivesEn = listOf(
                "Understand pH scale (0–14) and logarithmic H⁺ concentration",
                "Apply pH + pOH = 14 at 25°C in dilute solutions",
                "Recognize buffer systems vital for pharmaceutical formulations"
            ),
            learningObjectivesBn = listOf(
                "০ থেকে ১৪ পর্যন্ত pH স্কেল ও লগারিদমিক অনুপাত বুঝুন",
                "২৫° সে. তাপমাত্রায় pH + pOH = ১৪ সম্পর্কটি প্রয়োগ করুন",
                "ঔষধ শিল্পে অপরিহার্য বাফার দ্রবণের গুরুত্ব উপলব্ধি করুন"
            )
        ),
        ChemistryWorld(
            id = 8,
            key = "solutions",
            titleEn = "World 8 — Solution Laboratory",
            titleBn = "বিশ্ব ৮ — দ্রবণ রসায়নাগার",
            subtitleEn = "Molarity (M = mol/L), dilution C₁V₁ = C₂V₂, mass/volume %",
            subtitleBn = "মোলারিটি (M = mol/L), লঘুকরণ C₁V₁ = C₂V₂, শতকরা মাত্রা (% w/v)",
            gameTitleEn = "SOLUTION ENGINEER",
            gameTitleBn = "দ্রবণ প্রকৌশলী",
            iconName = "beaker",
            learningObjectivesEn = listOf(
                "Calculate solution molarity M = n / V(L)",
                "Execute serial and target dilutions using C₁V₁ = C₂V₂",
                "Distinguish solvent added from final solution volume"
            ),
            learningObjectivesBn = listOf(
                "মোলারিটি M = n / V(L) সূত্রের সমাধান করুন",
                "C₁V₁ = C₂V₂ সূত্র দ্বারা সঠিক লঘুকরণ হিসাব করুন",
                "যোগকৃত দ্রাবক ও চূড়ান্ত মোট দ্রবণের আয়তনের পার্থক্য বুঝুন"
            )
        ),
        ChemistryWorld(
            id = 9,
            key = "organic",
            titleEn = "World 9 — Organic Molecule Forest",
            titleBn = "বিশ্ব ৯ — জৈব অণু অরণ্য",
            subtitleEn = "Carbon tetravalency, functional groups (alcohol, aldehyde, carboxylic acid), isomers",
            subtitleBn = "কার্বনের চতুর্যোজ্যতা, কার্যকরী মূলক (অ্যালকোহল, অ্যালডিহাইড, এসিড), সমাণু",
            gameTitleEn = "MOLECULE REBUILDER",
            gameTitleBn = "জৈব অণু নির্মাতা",
            iconName = "leaf",
            learningObjectivesEn = listOf(
                "Identify primary functional groups (-OH, -CHO, -COOH, -NH₂)",
                "Understand alkanes, alkenes, alkynes, and aromatics",
                "Differentiate structural isomers and IUPAC prefixes"
            ),
            learningObjectivesBn = listOf(
                "প্রধান কার্যকরী মূলক (-OH, -CHO, -COOH, -NH₂) চিনুন",
                "অ্যালকেন, অ্যালকিন, অ্যালকাইন ও অ্যারোমেটিক যৌগ বুঝুন",
                "গাঠনিক সমাণুতা ও IUPAC নামকরণের ভিত্তি আয়ত্ত করুন"
            )
        ),
        ChemistryWorld(
            id = 10,
            key = "pharma",
            titleEn = "World 10 — Pharmaceutical Crystal Vault",
            titleBn = "বিশ্ব ১০ — ফার্মাসিউটিক্যাল ক্রিস্টাল ভল্ট",
            subtitleEn = "Antacids, saline electrolytes, acid-base titration, buffers in drugs",
            subtitleBn = "অ্যান্টাসিড, স্যালাইন ইলেকট্রোলাইট, এসিড-ক্ষার টাইট্রেশন, ঔষধে বাফার",
            gameTitleEn = "PHARMA CHEMISTRY MISSION",
            gameTitleBn = "ফার্মা রসায়ন অভিযান",
            iconName = "shield",
            learningObjectivesEn = listOf(
                "Connect basic chemistry to B.Pharm inorganic pharmaceuticals",
                "Solve volumetric titration calculations (N₁V₁ = N₂V₂)",
                "Understand physiological buffers (blood pH 7.4) and antacids (Al(OH)₃, Mg(OH)₂)"
            ),
            learningObjectivesBn = listOf(
                "বি.ফার্ম সিলেবাসের ফার্মাসিউটিক্যাল ইনঅর্গানিক রসায়ন বুঝুন",
                "টাইট্রেশন ও আয়তনিক বিশ্লেষণের অংক নিখুঁতভাবে সমাধান করুন",
                "দেহের রক্তের স্বাভাবিক pH (৭.৪) এবং অ্যান্টাসিড যৌগের রাসায়নিক ভিত্তি জানুন"
            )
        )
    )

    val COMMON_IONS: List<IonData> = listOf(
        IonData("Na⁺", "Na", "Sodium ion", "সোডিয়াম আয়ন", +1, false),
        IonData("K⁺", "K", "Potassium ion", "পটাশিয়াম আয়ন", +1, false),
        IonData("NH₄⁺", "NH4", "Ammonium ion", "অ্যামোনিয়াম মূলক", +1, true),
        IonData("Ca²⁺", "Ca", "Calcium ion", "ক্যালসিয়াম আয়ন", +2, false),
        IonData("Mg²⁺", "Mg", "Magnesium ion", "ম্যাগনেসিয়াম আয়ন", +2, false),
        IonData("Al³⁺", "Al", "Aluminium ion", "অ্যালুমিনিয়াম আয়ন", +3, false),
        IonData("Cl⁻", "Cl", "Chloride ion", "ক্লোরাইড আয়ন", -1, false),
        IonData("OH⁻", "OH", "Hydroxide ion", "হাইড্রক্সাইড মূলক", -1, true),
        IonData("NO₃⁻", "NO3", "Nitrate ion", "নাইট্রেট মূলক", -1, true),
        IonData("SO₄²⁻", "SO4", "Sulfate ion", "সালফেট মূলক", -2, true),
        IonData("CO₃²⁻", "CO3", "Carbonate ion", "কার্বনেট মূলক", -2, true),
        IonData("PO₄³⁻", "PO4", "Phosphate ion", "ফসফেট মূলক", -3, true)
    )

    val CURATED_COMPOUNDS: List<CompoundData> = listOf(
        CompoundData("NaCl", "Sodium Chloride", "সোডিয়াম ক্লোরাইড (লবণ)", "Na⁺", "Cl⁻", 1, 1, 58.44, "Table salt, essential 0.9% normal saline IV fluid.", "সাধারণ খাবার লবণ ও ০.৯% স্যালাইন ফ্লুইড।", "Used in dehydration and electrolyte replenishment.", "শরীরের পানি ও ইলেক্ট্রোলাইট ঘাটতি পূরণে ব্যবহৃত।"),
        CompoundData("MgCl₂", "Magnesium Chloride", "ম্যাগনেসিয়াম ক্লোরাইড", "Mg²⁺", "Cl⁻", 1, 2, 95.21, "Soluble magnesium salt.", "ম্যাগনেসিয়াম লবণ।", "Magnesium deficiency therapeutic agent.", "ম্যাগনেসিয়াম ঘাটতি নিরাময়ে ব্যবহৃত।"),
        CompoundData("CaCO₃", "Calcium Carbonate", "ক্যালসিয়াম কার্বনেট", "Ca²⁺", "CO₃²⁻", 1, 1, 100.09, "Insoluble antacid and calcium supplement.", "অদ্রবণীয় অ্যান্টাসিড ও ক্যালসিয়াম সম্পূরক।", "Antacid for hyperacidity and dietary calcium source.", "অতিরিক্ত এসিডিটি নিরাময়ে অ্যান্টাসিড।"),
        CompoundData("Al₂(SO₄)₃", "Aluminium Sulfate", "অ্যালুমিনিয়াম সালফেট", "Al³⁺", "SO₄²⁻", 2, 3, 342.15, "Polyatomic salt with Al³⁺ and SO₄²⁻.", "Al³⁺ এবং SO₄²⁻ এর নিরপেক্ষ লবণ।", "Used in water purification and astringents.", "পানি বিশুদ্ধকরণ ও অ্যাস্ট্রিঞ্জেন্ট হিসেবে ব্যবহৃত।"),
        CompoundData("NH₄Cl", "Ammonium Chloride", "অ্যামোনিয়াম ক্লোরাইড", "NH₄⁺", "Cl⁻", 1, 1, 53.49, "Sublimable systemic acidifier and expectorant.", "সিস্টেমিক এসিডিফায়ার ও কফের ওষুধ।", "Expectorant in cough syrups.", "কাশি ও কফ দূরীকরণের মিশ্রণে ব্যবহৃত।"),
        CompoundData("Na₂SO₄", "Sodium Sulfate", "সোডিয়াম সালফেট", "Na⁺", "SO₄²⁻", 2, 1, 142.04, "Neutral sodium salt of sulfuric acid.", "সালফিউরিক এসিডের নিরপেক্ষ সোডিয়াম লবণ।", "Saline cathartic / laxative in pharmacy.", "ফার্মাসিউটিক্যালে রেচক হিসেবে পরিচিত।")
    )

    val CURATED_REACTIONS: List<ReactionEquation> = listOf(
        ReactionEquation(
            id = "water_synthesis",
            unbalancedEquation = "H₂ + O₂ → H₂O",
            balancedEquation = "2H₂ + O₂ → 2H₂O",
            reactants = listOf("H2", "O2"),
            products = listOf("H2O"),
            reactantCoefficients = listOf(2, 1),
            productCoefficients = listOf(2),
            reactionType = "Synthesis / Combination",
            reactionTypeBn = "সংযোজন বিক্রিয়া",
            explanationEn = "4 Hydrogen atoms and 2 Oxygen atoms are conserved on both sides.",
            explanationBn = "উভয় পাশে ৪টি হাইড্রোজেন এবং ২টি অক্সিজেন পরমাণুর সমতা রক্ষা হয়েছে।"
        ),
        ReactionEquation(
            id = "neutralization_hcl_naoh",
            unbalancedEquation = "HCl + NaOH → NaCl + H₂O",
            balancedEquation = "HCl + NaOH → NaCl + H₂O",
            reactants = listOf("HCl", "NaOH"),
            products = listOf("NaCl", "H2O"),
            reactantCoefficients = listOf(1, 1),
            productCoefficients = listOf(1, 1),
            reactionType = "Neutralization / Double Displacement",
            reactionTypeBn = "প্রশমন বিক্রিয়া",
            explanationEn = "Hydrochloric acid reacts with sodium hydroxide to form neutral salt and water.",
            explanationBn = "হাইড্রোক্লোরিক এসিড ও সোডিয়াম হাইড্রোক্সাইড প্রশমিত হয়ে লবণ ও পানি উৎপন্ন করে।"
        ),
        ReactionEquation(
            id = "sodium_chloride_synth",
            unbalancedEquation = "Na + Cl₂ → NaCl",
            balancedEquation = "2Na + Cl₂ → 2NaCl",
            reactants = listOf("Na", "Cl2"),
            products = listOf("NaCl"),
            reactantCoefficients = listOf(2, 1),
            productCoefficients = listOf(2),
            reactionType = "Redox Synthesis",
            reactionTypeBn = "জারণ-বিজারণ সংযোজন",
            explanationEn = "Sodium is oxidized (Na → Na⁺ + e⁻) and chlorine is reduced (Cl₂ + 2e⁻ → 2Cl⁻).",
            explanationBn = "সোডিয়াম ইলেকট্রন ত্যাগ করে জারিত হয় এবং ক্লোরিন ইলেকট্রন গ্রহণ করে বিজারিত হয়।"
        ),
        ReactionEquation(
            id = "methane_combustion",
            unbalancedEquation = "CH₄ + O₂ → CO₂ + H₂O",
            balancedEquation = "CH₄ + 2O₂ → CO₂ + 2H₂O",
            reactants = listOf("CH4", "O2"),
            products = listOf("CO2", "H2O"),
            reactantCoefficients = listOf(1, 2),
            productCoefficients = listOf(1, 2),
            reactionType = "Combustion",
            reactionTypeBn = "দহন বিক্রিয়া",
            explanationEn = "1 Carbon, 4 Hydrogens, 4 Oxygens on each side.",
            explanationBn = "প্রতি পাশে ১টি কার্বন, ৪টি হাইড্রোজেন ও ৪টি অক্সিজেন পরমাণু রয়েছে।"
        ),
        ReactionEquation(
            id = "calcium_carbonate_decomp",
            unbalancedEquation = "CaCO₃ → CaO + CO₂",
            balancedEquation = "CaCO₃ → CaO + CO₂",
            reactants = listOf("CaCO3"),
            products = listOf("CaO", "CO2"),
            reactantCoefficients = listOf(1),
            productCoefficients = listOf(1, 1),
            reactionType = "Thermal Decomposition",
            reactionTypeBn = "তাপীয় বিয়োজন বিক্রিয়া",
            explanationEn = "Limestone decomposes into quicklime (calcium oxide) and carbon dioxide gas.",
            explanationBn = "চুনাপাথর তাপে ভেঙে ক্যালসিয়াম অক্সাইড (চুন) ও কার্বন ডাই-অক্সাইড গ্যাসে পরিণত হয়।"
        )
    )

    val LESSONS: Map<Int, ChemistryLesson> = mapOf(
        1 to ChemistryLesson(
            id = "w1_lesson",
            worldId = 1,
            titleEn = "Atom Observatory: Subatomic Particles & Electron Shells",
            titleBn = "পরমাণু মানমন্দির: উপ-পারমাণবিক কণা ও ইলেকট্রন বিন্যাস",
            bilingualHeader = "Atomic Number — পারমাণবিক সংখ্যা (Z)",
            conceptOverviewEn = "Atoms consist of positively charged protons and neutral neutrons in the dense central nucleus, surrounded by negatively charged electrons occupying quantized energy levels (shells K=2, L=8, M=8/18). In a neutral atom, protons equal electrons.",
            conceptOverviewBn = "পরমাণুর কেন্দ্রে থাকে ধনাত্মক প্রোটন ও আধানহীন নিউট্রন বিশিষ্ট নিউক্লিয়াস, যাকে ঘিরে নির্দিষ্ট শক্তিস্তরে (K=২, L=৮, M=৮/১৮) ঘূর্ণায়মান ঋণাত্মক ইলেকট্রন। চার্জহীন পরমাণুতে প্রোটন সংখ্যা ও ইলেকট্রন সংখ্যা সর্বদা সমান হয়।",
            formulaDisplay = "Z = p⁺  |  A = p⁺ + n⁰  |  Net Charge = p⁺ - e⁻",
            visualType = "ATOM_SHELLS",
            workedExample = WorkedExample(
                titleEn = "Sodium Atom (Na, Z=11)",
                titleBn = "সোডিয়াম পরমাণুর গঠন (Na, Z=১১)",
                problemEn = "Sodium has 11 protons and 12 neutrons. Find its atomic number, mass number, and electron distribution.",
                problemBn = "সোডিয়ামের ১১টি প্রোটন ও ১২টি নিউট্রন আছে। এর পারমাণবিক সংখ্যা, ভর সংখ্যা ও শক্তিস্তরে ইলেকট্রন সংখ্যা নির্ণয় করুন।",
                stepsEn = listOf(
                    "Step 1: Atomic Number Z = Protons = 11.",
                    "Step 2: Mass Number A = Protons + Neutrons = 11 + 12 = 23.",
                    "Step 3: Shell filling rule: K shell holds 2, L shell holds 8, remaining 1 goes to M shell.",
                    "Step 4: Electron Configuration: 2, 8, 1 (Valence Electron = 1)."
                ),
                stepsBn = listOf(
                    "ধাপ ১: পারমাণবিক সংখ্যা Z = প্রোটন সংখ্যা = ১১।",
                    "ধাপ ২: ভর সংখ্যা A = প্রোটন + নিউট্রন = ১১ + ১২ = ২৩।",
                    "ধাপ ৩: শক্তিস্তরের নিয়ম: K স্তরে ২টি, L স্তরে ৮টি এবং অবশিষ্ট ১টি M স্তরে প্রবেশ করে।",
                    "ধাপ ৪: ইলেকট্রন বিন্যাস: ২, ৮, ১ (যোজ্যতা ইলেকট্রন = ১)।"
                ),
                finalAnswer = "Z = 11, A = 23, Config = 2, 8, 1"
            ),
            keyRuleEn = "Valency is combining capacity. Sodium loses its 1 valence electron to become stable Na⁺ (cation).",
            keyRuleBn = "যোজনী হলো পরমাণুর সংযোগ ক্ষমতা। সোডিয়াম তার ১টি যোজ্যতা ইলেকট্রন ত্যাগ করে স্থিতিশীল Na⁺ আয়নে পরিণত হয়।"
        ),
        2 to ChemistryLesson(
            id = "w2_lesson",
            worldId = 2,
            titleEn = "Periodic Galaxy: Groups, Periods & Trends",
            titleBn = "পর্যায় গ্যালাক্সি: গ্রুপ, পর্যায় ও পর্যায়বৃত্ত ধর্ম",
            bilingualHeader = "Valency vs Oxidation State — যোজনী বনাম জারণ অবস্থা",
            conceptOverviewEn = "The periodic table organizes elements by increasing atomic number Z. Vertical columns are Groups (similar chemical properties and valence electrons); horizontal rows are Periods (number of electron shells).",
            conceptOverviewBn = "পর্যায় সারণিতে মৌলসমূহকে পারমাণবিক সংখ্যা বৃদ্ধির ক্রমানুসারে সাজানো হয়েছে। উল্লম্ব স্তম্ভগুলোকে গ্রুপ এবং অনুভূমিক সারিগুলোকে পর্যায় বলা হয়। একই গ্রুপের মৌলসমূহের যোজ্যতা ইলেকট্রন সংখ্যা ও রাসায়নিক ধর্ম অভিন্ন হয়।",
            formulaDisplay = "Valency (unsigned: 1, 2, 3...) vs Oxidation State (+1, -2, +3...)",
            visualType = "PERIODIC_CARD",
            workedExample = WorkedExample(
                titleEn = "Magnesium vs Chlorine",
                titleBn = "ম্যাগনেসিয়াম বনাম ক্লোরিন",
                problemEn = "Magnesium is in Group 2, Chlorine is in Group 17. Compare their valency and common ionic charges.",
                problemBn = "ম্যাগনেসিয়াম গ্রুপ ২-এ এবং ক্লোরিন গ্রুপ ১৭-এ অবস্থিত। তাদের যোজনী ও সাধারণ আয়নিক চার্জ তুলনা করুন।",
                stepsEn = listOf(
                    "Mg has 2 valence electrons -> loses 2 -> valency 2, ionic charge +2 (Mg²⁺).",
                    "Cl has 7 valence electrons -> gains 1 to complete octet -> valency 1, ionic charge -1 (Cl⁻).",
                    "Valency is an integer combining power without sign (2 and 1); ionic charge carries sign (+2 and -1)."
                ),
                stepsBn = listOf(
                    "ম্যাগনেসিয়ামের শেষ স্তরে ২টি ইলেকট্রন আছে -> ২টি ত্যাগ করে -> যোজনী ২, আয়ন চার্জ +২ (Mg²⁺)।",
                    "ক্লোরিনের শেষ স্তরে ৭টি ইলেকট্রন আছে -> অষ্টক পূরণে ১টি গ্রহণ করে -> যোজনী ১, আয়ন চার্জ -১ (Cl⁻)।",
                    "যোজনী হলো চিহ্নহীন পূর্ণসংখ্যা (২ ও ১); আয়নিক চার্জ চিহ্নযুক্ত (+২ ও -১)।"
                ),
                finalAnswer = "Mg: Valency 2, Charge +2 | Cl: Valency 1, Charge -1"
            ),
            keyRuleEn = "Valency is unsigned; oxidation state has sign and can vary across different compounds.",
            keyRuleBn = "যোজনী সর্বদা ধনাত্মক বা চিহ্নহীন পূর্ণসংখ্যা; জারণ অবস্থা চিহ্নযুক্ত এবং যৌগের ভিন্নতায় পরিবর্তিত হতে পারে।"
        ),
        3 to ChemistryLesson(
            id = "w3_lesson",
            worldId = 3,
            titleEn = "Bonding Dimension: Ionic & Covalent Links",
            titleBn = "রাসায়নিক বন্ধন: আয়নিক ও সমযোজী বন্ধন",
            bilingualHeader = "Chemical Bond — রাসায়নিক বন্ধন ও অষ্টক নিয়ম",
            conceptOverviewEn = "Atoms bond to achieve stable noble-gas electron configurations (octet rule: 8 valence electrons). Ionic bonds form via complete electron transfer between metals and nonmetals. Covalent bonds form by mutual electron sharing between nonmetals.",
            conceptOverviewBn = "নিষ্ক্রিয় গ্যাসের মতো স্থিতিশীল ইলেকট্রন কাঠামো (অষ্টক নিয়ম: শেষ স্তরে ৮টি ইলেকট্রন) অর্জনের জন্য পরমাণুগুলো বন্ধনে যুক্ত হয়। ধাতু ও অধাতুর মধ্যে ইলেকট্রন আদান-প্রদানের মাধ্যমে আয়নিক বন্ধন এবং অধাতুসমূহের মধ্যে ইলেকট্রন শেয়ারিংয়ের মাধ্যমে সমযোজী বন্ধন তৈরি হয়।",
            formulaDisplay = "Ionic: M⁺ + X⁻ (Electrostatic) | Covalent: A:B (Electron Sharing)",
            visualType = "LEWIS_STRUCTURE",
            workedExample = WorkedExample(
                titleEn = "Water (H₂O) vs Table Salt (NaCl)",
                titleBn = "পানি (H₂O) বনাম খাবার লবণ (NaCl)",
                problemEn = "Classify the bonding in H₂O and NaCl using electron principles.",
                problemBn = "ইলেকট্রন আদান-প্রদান ও শেয়ারিং নীতির সাহায্যে H₂O এবং NaCl-এর বন্ধন ব্যাখ্যা করুন।",
                stepsEn = listOf(
                    "NaCl: Na (metal) donates 1e⁻ to Cl (nonmetal) -> Na⁺ and Cl⁻ held by strong electrostatic force -> Ionic Bond.",
                    "H₂O: Oxygen shares 1 pair of electrons with each H atom -> 2 single covalent bonds.",
                    "Because Oxygen has higher electronegativity (3.44) than Hydrogen (2.20), H₂O is a polar covalent molecule."
                ),
                stepsBn = listOf(
                    "NaCl: Na (ধাতু) তার ১টি ইলেকট্রন Cl (অধাতু)-কে দান করে -> Na⁺ ও Cl⁻ স্থির বৈদ্যুতিক আকর্ষণে যুক্ত -> আয়নিক বন্ধন।",
                    "H₂O: অক্সিজেন পরমাণু প্রতিটি হাইড্রোজেন পরমাণুর সাথে ১ জোড়া ইলেকট্রন শেয়ার করে -> ২টি একক সমযোজী বন্ধন।",
                    "অক্সিজেনের তড়িৎ ঋণাত্মকতা (৩.৪৪) হাইড্রোজেনের (২.২০) চেয়ে বেশি হওয়ায় পানি একটি পোলার সমযোজী অণু।"
                ),
                finalAnswer = "NaCl = Ionic Crystal Lattice, H₂O = Polar Covalent Molecule"
            ),
            keyRuleEn = "Electronegativity difference > 1.7 typically indicates predominantly ionic character.",
            keyRuleBn = "তড়িৎ ঋণাত্মকতার পার্থক্য ১.৭ এর বেশি হলে বন্ধনটি মূলত আয়নিক প্রকৃতির হয়।"
        ),
        4 to ChemistryLesson(
            id = "w4_lesson",
            worldId = 4,
            titleEn = "Formula Forge: Combining Ions into Neutral Compounds",
            titleBn = "সংকেত ফোর্জ: নিরপেক্ষ যৌগের সঠিক সংকেত তৈরি",
            bilingualHeader = "Chemical Formula — রাসায়নিক সংকেত ও মূলক",
            conceptOverviewEn = "Chemical formulas express the exact whole-number ratio of constituent atoms or ions. In ionic compounds, total positive charges must strictly equal total negative charges so the overall compound is electrically neutral (Net Charge = 0). Polyatomic ions repeated more than once require parentheses.",
            conceptOverviewBn = "রাসায়নিক সংকেত যৌগে উপস্থিত পরমাণু বা আয়নের ক্ষুদ্রতম পূর্ণসংখ্যা অনুপাত প্রকাশ করে। আয়নিক যৌগে মোট ধনাত্মক চার্জ ও মোট ঋণাত্মক চার্জ সমান হতে হয় যাতে সমগ্র যৌগটি তড়িৎ নিরপেক্ষ (মোট চার্জ = ০) থাকে। একাধিক বহু-পারমাণবিক মূলকের ক্ষেত্রে বন্ধনী ব্যবহার বাধ্যতামূলক।",
            formulaDisplay = "(Cation Charge × Count) + (Anion Charge × Count) = 0",
            visualType = "FORMULA_GRID",
            workedExample = WorkedExample(
                titleEn = "Aluminium Sulfate Synthesis",
                titleBn = "অ্যালুমিনিয়াম সালফেটের সংকেত নির্ণয়",
                problemEn = "Combine Aluminium ion (Al³⁺) and Sulfate ion (SO₄²⁻) into a valid neutral compound formula.",
                problemBn = "অ্যালুমিনিয়াম আয়ন (Al³⁺) এবং সালফেট মূলক (SO₄²⁻) দিয়ে একটি সঠিক তড়িৎ নিরপেক্ষ যৌগের সংকেত গঠন করুন।",
                stepsEn = listOf(
                    "Step 1: Cation is Al³⁺ (charge +3). Anion is SO₄²⁻ (charge -2, polyatomic).",
                    "Step 2: Find LCM of charges |3| and |-2| = 6.",
                    "Step 3: Number of Al³⁺ needed = 6 / 3 = 2. Total positive charge = +6.",
                    "Step 4: Number of SO₄²⁻ needed = 6 / 2 = 3. Total negative charge = -6.",
                    "Step 5: Write with parentheses because SO₄ is polyatomic and appears 3 times: Al₂(SO₄)₃."
                ),
                stepsBn = listOf(
                    "ধাপ ১: ক্যাটায়ন Al³⁺ (চার্জ +৩)। অ্যানায়ন SO₄²⁻ (চার্জ -২, বহু-পারমাণবিক)।",
                    "ধাপ ২: চার্জের পরম মানের ল.সা.গু = ৬।",
                    "ধাপ ৩: প্রয়োজনীয় Al³⁺ সংখ্যা = ৬ / ৩ = ২। মোট ধনাত্মক চার্জ = +৬।",
                    "ধাপ ৪: প্রয়োজনীয় SO₄²⁻ সংখ্যা = ৬ / ২ = ৩। মোট ঋণাত্মক চার্জ = -৬।",
                    "ধাপ ৫: সালফেট মূলক ৩টি থাকায় বন্ধনী দিয়ে লিখতে হবে: Al₂(SO₄)₃।"
                ),
                finalAnswer = "Al₂(SO₄)₃ (Aluminium Sulfate)"
            ),
            keyRuleEn = "Never alter subscripts inside a polyatomic ion: sulfate is always SO₄, never S₃O₁₂.",
            keyRuleBn = "বহু-পারমাণবিক মূলকের ভেতরের সাবস্ক্রিপ্ট কখনো বদলানো যাবে না: সালফেট সর্বদা SO₄, বন্ধনীর বাইরে ৩ বসবে।"
        ),
        5 to ChemistryLesson(
            id = "w5_lesson",
            worldId = 5,
            titleEn = "Reaction Reactor: Balancing Chemical Equations",
            titleBn = "বিক্রিয়া চুল্লি: রাসায়নিক সমীকরণ সমতাকরণ",
            bilingualHeader = "Chemical Equation — রাসায়নিক সমীকরণ ও ভরের নিত্যতা",
            conceptOverviewEn = "The Law of Conservation of Mass states that matter is neither created nor destroyed in a chemical reaction. Therefore, the total number of atoms of each element must remain exactly identical on the reactant side (left) and product side (right). We balance equations by adjusting stoichiometric coefficients in front of formulas, never changing subscripts.",
            conceptOverviewBn = "ভরের নিত্যতা সূত্র অনুসারে রাসায়নিক বিক্রিয়ায় কোনো পরমাণু ধ্বংস বা সৃষ্টি হয় না। সুতরাং বিক্রিয়কের (বাম পাশ) প্রতিটি মৌলের মোট পরমাণু সংখ্যা উৎপাদের (ডান পাশ) পরমাণু সংখ্যার হুবহু সমান হতে হবে। আমরা সংকেতের আগে পূর্ণসংখ্যার সহগ (coefficient) বসিয়ে সমতা করি, সংকেতের সাবস্ক্রিপ্ট কখনো পরিবর্তন করি না।",
            formulaDisplay = "Reactants → Products  |  Σ Atoms(Left) = Σ Atoms(Right)",
            visualType = "REACTION_BEAKER",
            workedExample = WorkedExample(
                titleEn = "Balancing Water Formation",
                titleBn = "পানির গঠন সমীকরণ সমতাকরণ",
                problemEn = "Balance the combustion reaction of hydrogen and oxygen: H₂ + O₂ → H₂O.",
                problemBn = "হাইড্রোজেন ও অক্সিজেনের পানির গঠন সমীকরণ সমতা করুন: H₂ + O₂ → H₂O।",
                stepsEn = listOf(
                    "Step 1: Count initial atoms: Left has 2 H, 2 O. Right has 2 H, 1 O.",
                    "Step 2: Oxygen is unbalanced. Place coefficient 2 in front of H₂O: H₂ + O₂ → 2H₂O.",
                    "Step 3: Recount: Right now has 4 H and 2 O. Left has 2 H and 2 O.",
                    "Step 4: Balance Hydrogen by placing coefficient 2 in front of H₂: 2H₂ + O₂ → 2H₂O.",
                    "Step 5: Verify: Left has 4 H, 2 O; Right has 4 H, 2 O. Perfectly balanced!"
                ),
                stepsBn = listOf(
                    "ধাপ ১: পরমাণু গণনা: বামে ২ H, ২ O। ডানে ২ H, ১ O।",
                    "ধাপ ২: অক্সিজেন অসমান। পানির সামনে সহগ ২ বসাই: H₂ + O₂ → 2H₂O।",
                    "ধাপ ৩: এবার ডানে ৪টি H এবং ২টি O হলো। বামে ২ H এবং ২ O আছে।",
                    "ধাপ ৪: বামের হাইড্রোজেনকে সমতা করতে H₂ এর সামনে ২ বসাই: 2H₂ + O₂ → 2H₂O।",
                    "ধাপ ৫: যাচাই: বামে ৪ H, ২ O; ডানে ৪ H, ২ O। সমীকরণটি সম্পূর্ণ সমতাকৃত!"
                ),
                finalAnswer = "2H₂ + O₂ → 2H₂O"
            ),
            keyRuleEn = "Neutralization: Acid + Base → Salt + Water (e.g., HCl + NaOH → NaCl + H₂O).",
            keyRuleBn = "প্রশমন বিক্রিয়া: এসিড + ক্ষার → লবণ + পানি (যেমন: HCl + NaOH → NaCl + H₂O)।"
        ),
        6 to ChemistryLesson(
            id = "w6_lesson",
            worldId = 6,
            titleEn = "Mole Matrix: Amount of Substance & Avogadro's Number",
            titleBn = "মোল ম্যাট্রিক্স: পদার্থের পরিমাণ ও অ্যাভোগাড্রো ধ্রুবক",
            bilingualHeader = "Mole — মোল (n) ও মোলার ভর (M)",
            conceptOverviewEn = "A mole is the SI unit for amount of substance. One mole contains exactly 6.02214076 × 10²³ elementary entities (atoms, molecules, or ions), known as Avogadro's constant (Nₐ). Molar mass (M) in g/mol is numerically equal to the relative atomic or molecular mass.",
            conceptOverviewBn = "মোল হলো পদার্থের পরিমাণের আন্তর্জাতিক (SI) একক। এক মোলে ঠিক ৬.০২২১৪০৭৬ × ১০²³ টি কণা (পরমাণু, অণু বা আয়ন) থাকে, যাকে অ্যাভোগাড্রো ধ্রুবক (Nₐ) বলে। গ্রাম এককে মোলার ভর (M) আপেক্ষিক আণবিক ভরের সমান।",
            formulaDisplay = "n = m / M  |  N = n × Nₐ  |  m = n × M",
            visualType = "MOLE_SCALE",
            workedExample = WorkedExample(
                titleEn = "Mass to Moles of Sodium Hydroxide (NaOH)",
                titleBn = "কস্টিক সোডার ভর থেকে মোল সংখ্যা নির্ণয়",
                problemEn = "Calculate the amount of substance in moles in 80 grams of sodium hydroxide (NaOH). (Atomic weights: Na=23, O=16, H=1)",
                problemBn = "৮০ গ্রাম সোডিয়াম হাইড্রোক্সাইডে (NaOH) কত মোল পদার্থ আছে তা নির্ণয় করুন। (পারমাণবিক ভর: Na=২৩, O=১৬, H=১)",
                stepsEn = listOf(
                    "Step 1: Calculate molar mass M of NaOH = 23 + 16 + 1 = 40.0 g/mol.",
                    "Step 2: Identify given mass m = 80.0 g.",
                    "Step 3: Apply relationship: n = m / M = 80.0 g / 40.0 g/mol = 2.0 moles.",
                    "Step 4: Number of molecules N = 2.0 × 6.022 × 10²³ = 1.2044 × 10²⁴ molecules."
                ),
                stepsBn = listOf(
                    "ধাপ ১: NaOH-এর মোলার ভর M = ২৩ + ১৬ + ১ = ৪০.০ গ্রাম/মোল।",
                    "ধাপ ২: প্রদত্ত ভর m = ৮০.০ গ্রাম।",
                    "ধাপ ৩: সূত্র প্রয়োগ: n = m / M = ৮০.০ গ্রাম / ৪০.০ গ্রাম/মোল = ২.০ মোল।",
                    "ধাপ ৪: মোট অণুর সংখ্যা N = ২.০ × ৬.০২২ × ১০²³ = ১.২০৪৪ × ১০²⁴ টি অণু।"
                ),
                finalAnswer = "n = 2.0 mol, N = 1.2044 × 10²⁴ molecules"
            ),
            keyRuleEn = "Avogadro's number connects the microscopic molecular world to measurable lab masses.",
            keyRuleBn = "অ্যাভোগাড্রো ধ্রুবক আণুবীক্ষণিক পরমাণু ও ল্যাবরেটরির পরিমাপযোগ্য ভরের মধ্যে মেলবন্ধন তৈরি করে।"
        ),
        7 to ChemistryLesson(
            id = "w7_lesson",
            worldId = 7,
            titleEn = "pH Dimension: Acids, Bases & Buffers",
            titleBn = "pH মাত্রা: অম্ল, ক্ষার ও বাফার দ্রবণ",
            bilingualHeader = "pH & pOH — অম্লতা ও ক্ষারত্ব স্কেল",
            conceptOverviewEn = "pH is defined as the negative base-10 logarithm of hydrogen ion activity, commonly approximated as [H⁺] in dilute aqueous solutions: pH = -log₁₀[H⁺]. At 25°C in neutral water, [H⁺] = 10⁻⁷ M, so pH = 7. Pure water self-ionizes: Kw = [H⁺][OH⁻] = 1.0 × 10⁻¹⁴, giving the relationship pH + pOH ≈ 14.0.",
            conceptOverviewBn = "লঘু জলীয় দ্রবণে হাইড্রোজেন আয়নের ঘনমাত্রার ঋণাত্মক ১০-ভিত্তিক লগারিদমকে pH বলা হয়: pH = -log₁₀[H⁺]। ২৫° সে. তাপমাত্রায় বিশুদ্ধ পানিতে [H⁺] = ১০⁻⁷ M, তাই pH = ৭। পানির স্বতঃ-আয়ননের কারণে [H⁺][OH⁻] = ১.০ × ১০⁻¹⁴, যেখান থেকে আসে pH + pOH ≈ ১৪.০ সম্পর্কটি।",
            formulaDisplay = "pH = -log₁₀[H⁺]  |  pOH = -log₁₀[OH⁻]  |  pH + pOH ≈ 14.0 (25°C)",
            visualType = "PH_STRIP",
            workedExample = WorkedExample(
                titleEn = "pH of 0.01 M Hydrochloric Acid (HCl)",
                titleBn = "০.০১ M হাইড্রোক্লোরিক এসিডের pH নির্ণয়",
                problemEn = "Calculate the pH of a 0.01 mol/L aqueous solution of strong acid HCl at 25°C.",
                problemBn = "২৫° সে. তাপমাত্রায় ০.০১ mol/L ঘনমাত্রার তীব্র এসিড HCl দ্রবণের pH নির্ণয় করুন।",
                stepsEn = listOf(
                    "Step 1: HCl is a strong monoprotic acid that dissociates completely: HCl → H⁺ + Cl⁻.",
                    "Step 2: Therefore, [H⁺] = 0.01 M = 1.0 × 10⁻² mol/L.",
                    "Step 3: Apply formula: pH = -log₁₀(10⁻²) = -(-2) = 2.0.",
                    "Step 4: Check pOH: pOH = 14.0 - 2.0 = 12.0. Solution is strongly acidic."
                ),
                stepsBn = listOf(
                    "ধাপ ১: HCl একটি তীব্র এক-ক্ষারকীয় এসিড যা পানিতে সম্পূর্ণরূপে বিয়োজিত হয়: HCl → H⁺ + Cl⁻।",
                    "ধাপ ২: সুতরাং, দ্রবণে [H⁺] = ০.০১ M = ১.০ × ১০⁻² mol/L।",
                    "ধাপ ৩: সূত্র প্রয়োগ: pH = -log₁₀(১০⁻²) = -(-২) = ২.০।",
                    "ধাপ ৪: pOH নির্ণয়: pOH = ১৪.০ - ২.০ = ১২.০। দ্রবণটি তীব্র অম্লীয়।"
                ),
                finalAnswer = "pH = 2.0 (Strongly Acidic)"
            ),
            keyRuleEn = "Buffers resist pH change when small amounts of acid or base are added. Blood pH is tightly regulated at 7.4.",
            keyRuleBn = "বাফার দ্রবণ সামান্য এসিড বা ক্ষার যোগ করলেও pH পরিবর্তন প্রতিরোধ করে। মানব রক্তের pH ৭.৪-এ নিয়ন্ত্রিত থাকে।"
        ),
        8 to ChemistryLesson(
            id = "w8_lesson",
            worldId = 8,
            titleEn = "Solution Laboratory: Molarity & Dilution",
            titleBn = "দ্রবণ ল্যাবরেটরি: মোলারিটি ও লঘুকরণ হিসাব",
            bilingualHeader = "Molarity — মোলারিটি (M = mol/L) ও C₁V₁ = C₂V₂",
            conceptOverviewEn = "Molarity (M) is the concentration of a solution expressed as the number of moles of solute dissolved per litre of total solution (mol/L). In dilution, only solvent is added, so moles of solute remain constant: n = C₁V₁ = C₂V₂.",
            conceptOverviewBn = "প্রতি লিটার দ্রবণে দ্রবীভূত দ্রবের মোল সংখ্যাকে ঐ দ্রবণের মোলারিটি (M) বলে (একক: mol/L)। লঘুকরণের সময় কেবল দ্রাবক (পানি) যোগ করা হয়, ফলে দ্রবের মোট মোল সংখ্যা অপরিবর্তিত থাকে: C₁V₁ = C₂V₂।",
            formulaDisplay = "M = n / V_L = m / (MolarMass × V_L)  |  C₁V₁ = C₂V₂",
            visualType = "SOLUTION_FLASK",
            workedExample = WorkedExample(
                titleEn = "Preparing 0.5 M NaCl Solution",
                titleBn = "০.৫ M সোডিয়াম ক্লোরাইড দ্রবণ প্রস্তুতকরণ",
                problemEn = "How many grams of NaCl are required to prepare 500 mL of 0.50 M solution? (Molar mass NaCl = 58.44 g/mol)",
                problemBn = "৫০০ মিলি ০.৫০ M দ্রবণ তৈরি করতে কত গ্রাম NaCl প্রয়োজন? (NaCl-এর মোলার ভর = ৫৮.৪৪ গ্রাম/মোল)",
                stepsEn = listOf(
                    "Step 1: Convert volume to litres: V = 500 mL / 1000 = 0.50 L.",
                    "Step 2: Calculate moles required: n = M × V = 0.50 mol/L × 0.50 L = 0.25 mol.",
                    "Step 3: Calculate mass: m = n × MolarMass = 0.25 mol × 58.44 g/mol = 14.61 g.",
                    "Step 4: Dissolve 14.61 g of NaCl in solvent and bring total final volume to exactly 500 mL in a volumetric flask."
                ),
                stepsBn = listOf(
                    "ধাপ ১: আয়তন লিটারে রূপান্তর: V = ৫০০ মিলি / ১০০০ = ০.৫০ লিটার।",
                    "ধাপ ২: প্রয়োজনীয় মোল সংখ্যা: n = M × V = ০.৫০ mol/L × ০.৫০ L = ০.২৫ মোল।",
                    "ধাপ ৩: প্রয়োজনীয় ভর: m = n × মোলার ভর = ০.২৫ মোল × ৫৮.৪৪ গ্রাম/মোল = ১৪.৬১ গ্রাম।",
                    "ধাপ ৪: ১৪.৬১ গ্রাম লবণ মেপে ভলিউমেট্রিক ফ্লাস্কে পানি যোগ করে মোট আয়তন ৫০০ মিলি করতে হবে।"
                ),
                finalAnswer = "Mass = 14.61 g NaCl dissolved up to 500 mL"
            ),
            keyRuleEn = "Never add solvent after the mark; solute plus solvent together equal final solution volume.",
            keyRuleBn = "দ্রাবক ও দ্রবের মোট সমন্বিত আয়তনই হলো চূড়ান্ত দ্রবণের আয়তন।"
        ),
        9 to ChemistryLesson(
            id = "w9_lesson",
            worldId = 9,
            titleEn = "Organic Molecule Forest: Functional Groups & Nomenclature",
            titleBn = "জৈব অণু অরণ্য: কার্যকরী মূলক ও নামকরণ",
            bilingualHeader = "Functional Group — কার্যকরী মূলক ও জৈব যৌগ",
            conceptOverviewEn = "Organic chemistry centers on Carbon's unique ability to form 4 strong covalent bonds (catenation). A Functional Group is a specific group of atoms that imparts characteristic chemical reactivity to organic molecules: Alcohol (-OH), Aldehyde (-CHO), Ketone (-CO-), Carboxylic Acid (-COOH), Amine (-NH₂).",
            conceptOverviewBn = "জৈব রসায়নের মূল ভিত্তি হলো কার্বনের চতুর্যোজ্যতা এবং শিকল গঠনের অসাধারণ ক্ষমতা। কার্যকরী মূলক হলো জৈব যৌগের অণুতে উপস্থিত বিশেষ পরমাণু বা পরমাণুগুচ্ছ যা ঐ যৌগের রাসায়নিক ধর্ম ও বৈশিষ্ট্য নির্ধারণ করে: যেমন অ্যালকোহল (-OH), অ্যালডিহাইড (-CHO), কিটোন (-CO-), কার্বক্সিলিক এসিড (-COOH), অ্যামিন (-NH₂)।",
            formulaDisplay = "-OH (Alcohol) | -CHO (Aldehyde) | -COOH (Carboxylic Acid) | -NH₂ (Amine)",
            visualType = "ORGANIC_CHAIN",
            workedExample = WorkedExample(
                titleEn = "Ethanol vs Acetic Acid",
                titleBn = "ইথানল বনাম অ্যাসিটিক এসিড",
                problemEn = "Compare ethanol (C₂H₅OH) and ethanoic/acetic acid (CH₃COOH) by identifying their functional groups and IUPAC classes.",
                problemBn = "ইথানল (C₂H₅OH) এবং অ্যাসিটিক এসিডের (CH₃COOH) কার্যকরী মূলক ও শ্রেণী চিহ্নিত করুন।",
                stepsEn = listOf(
                    "Ethanol: CH₃-CH₂-OH contains the hydroxyl group (-OH) attached to an sp³ carbon -> Class: Alcohol.",
                    "Acetic Acid: CH₃-COOH contains the carboxyl group (-COOH, carbonyl + hydroxyl on same carbon) -> Class: Carboxylic Acid.",
                    "Ethanol is a neutral organic solvent and antiseptic; Acetic acid is a weak organic acid."
                ),
                stepsBn = listOf(
                    "ইথানল: CH₃-CH₂-OH যৌগে হাইড্রক্সিল মূলক (-OH) বিদ্যমান -> শ্রেণী: অ্যালকোহল।",
                    "অ্যাসিটিক এসিড: CH₃-COOH যৌগে কার্বক্সিল মূলক (-COOH) বিদ্যমান -> শ্রেণী: কার্বক্সিলিক এসিড।",
                    "ইথানল জীবানুনাশক ও দ্রাবক; অ্যাসিটিক এসিড দুর্বল জৈব এসিড যা ভিনেগারে থাকে।"
                ),
                finalAnswer = "Ethanol = -OH (Alcohol) | Acetic Acid = -COOH (Carboxylic Acid)"
            ),
            keyRuleEn = "Isomers have the same molecular formula but different structural arrangements or spatial orientations.",
            keyRuleBn = "সমাণুসমূহের আণবিক সংকেত একই হলেও তাদের গঠনে বা পরমাণুর বিন্যাসে পার্থক্য থাকে।"
        ),
        10 to ChemistryLesson(
            id = "w10_lesson",
            worldId = 10,
            titleEn = "Pharmaceutical Crystal Vault: Chemistry in Medicine & B.Pharm",
            titleBn = "ফার্মাসিউটিক্যাল ক্রিস্টাল ভল্ট: চিকিৎসাবিজ্ঞান ও বি.ফার্মে রসায়ন",
            bilingualHeader = "Pharmaceutical Chemistry — ফার্মাসিউটিক্যাল ইনঅর্গানিক রসায়ন",
            conceptOverviewEn = "Pharmacy translates basic chemical laws into therapeutic tools. Antacids neutralize gastric hydrochloric acid (Al(OH)₃, Mg(OH)₂, CaCO₃). Physiological saline (0.9% w/v NaCl) matches blood tonicity (isotonic). Volumetric titration allows precise quantitative assay of active pharmaceutical ingredients (APIs).",
            conceptOverviewBn = "ফার্মেসি বিজ্ঞান মৌলিক রসায়নকে রোগীর নিরাময়ের কার্যকরী ওষুধে রূপান্তর করে। অ্যান্টাসিড পাকস্থলীর অতিরিক্ত এসিড প্রশমিত করে (যেমন Al(OH)₃, Mg(OH)₂, CaCO₃)। ০.৯% সোডিয়াম ক্লোরাইড স্যালাইন রক্তের সম-অভিস্রবণিক (আইসোটোনিক)। টাইট্রেশন পদ্ধতির মাধ্যমে ঔষধের বিশুদ্ধতা ও সক্রিয় উপাদানের সঠিক পরিমাণ নির্ধারণ করা হয়।",
            formulaDisplay = "N₁V₁ = N₂V₂  |  0.9% w/v NaCl = 9 g/L Isotonic Saline  |  Blood pH = 7.35–7.45",
            visualType = "PHARMA_VIAL",
            workedExample = WorkedExample(
                titleEn = "Volumetric Assay of Sodium Hydroxide with Standard HCl",
                titleBn = "স্ট্যান্ডার্ড HCl দ্বারা সোডিয়াম হাইড্রোক্সাইডের টাইট্রেশন বিশ্লেষণ",
                problemEn = "In a pharmaceutical quality control assay, 20.0 mL of NaOH sample required 25.0 mL of 0.100 M standard HCl for complete neutralization (phenolphthalein end-point). Find the molarity of the NaOH sample.",
                problemBn = "ঔষধের গুণগত মান যাচাইয়ে ২০.০ মিলি NaOH নমুনাকে সম্পূর্ণ প্রশমিত করতে ২৫.০ মিলি ০.১০০ M স্ট্যান্ডার্ড HCl প্রয়োজন হলো। NaOH নমুনার সঠিক মোলারিটি কত?",
                stepsEn = listOf(
                    "Step 1: Balanced reaction: HCl + NaOH → NaCl + H₂O (1:1 stoichiometric ratio).",
                    "Step 2: Moles of acid used: n_acid = M_acid × V_acid = 0.100 mol/L × 0.0250 L = 0.00250 mol.",
                    "Step 3: At equivalence point, moles of base = moles of acid = 0.00250 mol.",
                    "Step 4: Calculate base molarity: M_base = n_base / V_base = 0.00250 mol / 0.0200 L = 0.125 M."
                ),
                stepsBn = listOf(
                    "ধাপ ১: সমতাকৃত বিক্রিয়া: HCl + NaOH → NaCl + H₂O (১:১ মোলার অনুপাত)।",
                    "ধাপ ২: ব্যবহৃত এসিডের মোল সংখ্যা: n_acid = ০.১০০ mol/L × ০.০২৫০ L = ০.০০২৫০ মোল।",
                    "ধাপ ৩: তুল্যতা বিন্দুতে ক্ষারের মোল = এসিডের মোল = ০.০০২৫০ মোল।",
                    "ধাপ ৪: ক্ষারের মোলারিটি: M_base = ০.০০২৫০ মোল / ০.০২০০ L = ০.১২৫ M।"
                ),
                finalAnswer = "Molarity of NaOH = 0.125 M (mol/L)"
            ),
            keyRuleEn = "In pharmacy, precise concentration and purity are critical for drug safety and therapeutic efficacy.",
            keyRuleBn = "ঔষধ শিল্পে রোগীর সুরক্ষা ও নিরাময়ে উপাদানের সঠিক ঘনমাত্রা ও বিশুদ্ধতা অতীব গুরুত্বপূর্ণ।"
        )
    )

    val QUESTIONS_PER_WORLD: Map<Int, List<ChemistryQuestion>> = mapOf(
        1 to listOf(
            ChemistryQuestion(
                id = "w1_q1",
                worldId = 1,
                promptEn = "An atom has 6 protons, 6 neutrons, and 6 electrons. What is its Mass Number (A)?",
                promptBn = "একটি পরমাণুতে ৬টি প্রোটন, ৬টি নিউট্রন এবং ৬টি ইলেকট্রন আছে। এর ভর সংখ্যা (A) কত?",
                optionsEn = listOf("6", "12", "18", "0"),
                optionsBn = listOf("৬", "১২", "১৮", "০"),
                correctIndex = 1,
                hintEn = "Mass Number A = Protons + Neutrons.",
                hintBn = "ভর সংখ্যা A = প্রোটন সংখ্যা + নিউট্রন সংখ্যা।",
                explanationEn = "Protons (6) + Neutrons (6) = 12. Carbon-12 has mass number 12. Electrons have negligible mass.",
                explanationBn = "প্রোটন (৬) + নিউট্রন (৬) = ১২। কার্বন-১২ এর ভর সংখ্যা ১২। ইলেকট্রনের ভর অতি নগণ্য হওয়ায় ভর সংখ্যায় যোগ হয় না।"
            ),
            ChemistryQuestion(
                id = "w1_q2",
                worldId = 1,
                promptEn = "If a neutral Calcium atom (Z=20) loses 2 valence electrons, what is the charge on the resulting ion?",
                promptBn = "একটি নিরপেক্ষ ক্যালসিয়াম পরমাণু (Z=২০) যদি ২টি যোজ্যতা ইলেকট্রন ত্যাগ করে, তবে উৎপন্ন আয়নের চার্জ কত হবে?",
                optionsEn = listOf("+2 (Cation)", "-2 (Anion)", "+1", "0 (Neutral)"),
                optionsBn = listOf("+২ (ক্যাটায়ন)", "-২ (অ্যানায়ন)", "+১", "০ (নিরপেক্ষ)"),
                correctIndex = 0,
                hintEn = "Losing negative electrons leaves an excess of positive protons.",
                hintBn = "ঋণাত্মক ইলেকট্রন ত্যাগ করলে ধনাত্মক প্রোটনের সংখ্যা বেশি হয়ে যায়।",
                explanationEn = "20 protons minus 18 remaining electrons = +2 net charge. It forms Ca²⁺ cation.",
                explanationBn = "২০টি প্রোটন ও ১৮টি ইলেকট্রনের ফলে নেট চার্জ হয় +২। এটি Ca²⁺ ক্যাটায়ন তৈরি করে।"
            )
        ),
        2 to listOf(
            ChemistryQuestion(
                id = "w2_q1",
                worldId = 2,
                promptEn = "Which of the following correctly describes the difference between Valency and Oxidation State?",
                promptBn = "নিচের কোনটি যোজনী এবং জারণ অবস্থার মধ্যে সঠিক পার্থক্য নির্দেশ করে?",
                optionsEn = listOf(
                    "Valency is unsigned integer; oxidation state has an algebraic sign (+ or -)",
                    "Valency is always negative; oxidation state is always positive",
                    "There is no difference; they are always identical",
                    "Valency only applies to gases"
                ),
                optionsBn = listOf(
                    "যোজনী হলো চিহ্নহীন পূর্ণসংখ্যা; জারণ অবস্থা একটি বীজগাণিতিক চিহ্নযুক্ত সংখ্যা (+ বা -)",
                    "যোজনী সর্বদা ঋণাত্মক; জারণ অবস্থা সর্বদা ধনাত্মক",
                    "তাদের মধ্যে কোনো পার্থক্য নেই; তারা সর্বদা একই",
                    "যোজনী কেবল গ্যাসের ক্ষেত্রে প্রযোজ্য"
                ),
                correctIndex = 0,
                hintEn = "Combining capacity has no sign, while formal charge carries a positive or negative sign.",
                hintBn = "সংযোগ ক্ষমতার কোনো চিহ্ন থাকে না, কিন্তু অর্জিত চার্জের ধনাত্মক বা ঋণাত্মক চিহ্ন থাকে।",
                explanationEn = "Valency is the combining capacity of an element (always an unsigned integer like 1, 2, 3), whereas oxidation state is the hypothetical formal charge carrying a positive or negative sign.",
                explanationBn = "যোজনী হলো পরমাণুর সংযোগ ক্ষমতা (চিহ্নহীন ১, ২, ৩), অপরদিকে জারণ অবস্থা হলো যৌগে অর্জিত আপাত চার্জ যা ধনাত্মক বা ঋণাত্মক হতে পারে।"
            ),
            ChemistryQuestion(
                id = "w2_q2",
                worldId = 2,
                promptEn = "Elements in Group 18 of the Periodic Table (He, Ne, Ar, Kr) are called:",
                promptBn = "পর্যায় সারণির গ্রুপ ১৮-এর মৌলসমূহকে (He, Ne, Ar, Kr) কী বলা হয়?",
                optionsEn = listOf("Noble Gases (Inert)", "Alkali Metals", "Halogens", "Transition Metals"),
                optionsBn = listOf("নিষ্ক্রিয় গ্যাস", "ক্ষার ধাতু", "হ্যালোজেন", "অবস্থান্তর ধাতু"),
                correctIndex = 0,
                hintEn = "They have complete valence shells (duet/octet) and are chemically unreactive.",
                hintBn = "এদের বহিঃস্থ শক্তিস্তর ইলেকট্রন দ্বারা পূর্ণ (দ্বিত্ব/অষ্টক) এবং এরা রাসায়নিকভাবে নিষ্ক্রিয়।",
                explanationEn = "Group 18 elements have completely filled valence shells, making them extremely stable and chemically inert noble gases.",
                explanationBn = "গ্রুপ ১৮-এর মৌলসমূহের শেষ শক্তিস্তর সম্পূর্ণ থাকায় তারা অত্যন্ত স্থিতিশীল ও নিষ্ক্রিয় গ্যাস হিসেবে পরিচিত।"
            )
        ),
        3 to listOf(
            ChemistryQuestion(
                id = "w3_q1",
                worldId = 3,
                promptEn = "Which type of chemical bond is formed when a metal atom transfers electrons to a nonmetal atom?",
                promptBn = "একটি ধাতু পরমাণু যখন একটি অধাতু পরমাণুকে ইলেকট্রন দান করে, তখন কোন ধরনের রাসায়নিক বন্ধন গঠিত হয়?",
                optionsEn = listOf("Ionic Bond", "Covalent Bond", "Metallic Bond", "Hydrogen Bond"),
                optionsBn = listOf("আয়নিক বন্ধন", "সমযোজী বন্ধন", "ধাতব বন্ধন", "হাইড্রোজেন বন্ধন"),
                correctIndex = 0,
                hintEn = "The resulting cations and anions are held by electrostatic attraction.",
                hintBn = "উৎপন্ন ক্যাটায়ন ও অ্যানায়ন স্থির বৈদ্যুতিক আকর্ষণে পরস্পরের সাথে আবদ্ধ থাকে।",
                explanationEn = "Electron transfer between low-electronegativity metals and high-electronegativity nonmetals creates oppositely charged ions forming an Ionic Bond.",
                explanationBn = "ধাতু ও অধাতুর মধ্যে ইলেকট্রন স্থানান্তরের মাধ্যমে সৃষ্ট বিপরীত চার্জযুক্ত আয়নগুলো স্থির বৈদ্যুতিক বলে যুক্ত হয়ে আয়নিক বন্ধন গঠন করে।"
            ),
            ChemistryQuestion(
                id = "w3_q2",
                worldId = 3,
                promptEn = "Why is water (H₂O) considered a polar molecule?",
                promptBn = "পানিকে (H₂O) কেন একটি পোলার অণু বিবেচনা করা হয়?",
                optionsEn = listOf(
                    "Oxygen is significantly more electronegative than Hydrogen, creating dipole moments",
                    "Water contains ionic bonds between H⁺ and O²⁻",
                    "Water is linear with zero dipole moment",
                    "Water has no electrons"
                ),
                optionsBn = listOf(
                    "অক্সিজেনের তড়িৎ ঋণাত্মকতা হাইড্রোজেনের চেয়ে অনেক বেশি হওয়ায় ডাইপোল সৃষ্টি হয়",
                    "পানিতে H⁺ এবং O²⁻ এর মধ্যে আয়নিক বন্ধন থাকে",
                    "পানির অণু সরলরৈখিক হওয়ায় কোনো ডাইপোল থাকে না",
                    "পানিতে কোনো ইলেকট্রন থাকে না"
                ),
                correctIndex = 0,
                hintEn = "Electronegativity of Oxygen is 3.44 vs Hydrogen 2.20, with a bent 104.5° geometry.",
                hintBn = "অক্সিজেনের উচ্চ তড়িৎ ঋণাত্মকতা ও পানির কৌণিক গঠনের কারণে এতে ধনাত্মক ও ঋণাত্মক প্রান্তের সৃষ্টি হয়।",
                explanationEn = "The bent geometry (104.5°) and electronegativity difference between O (3.44) and H (2.20) prevent bond dipoles from canceling, making H₂O strongly polar.",
                explanationBn = "অক্সিজেনের উচ্চ তড়িৎ ঋণাত্মকতা ও কৌণিক কাঠামোর কারণে পানিতে স্থায়ী ডাইপোল সৃষ্টি হয় যা পানিকে উৎকৃষ্ট পোলার দ্রাবক করে তোলে।"
            )
        ),
        4 to listOf(
            ChemistryQuestion(
                id = "w4_q1",
                worldId = 4,
                promptEn = "What is the correct chemical formula for Magnesium Chloride formed by Mg²⁺ and Cl⁻?",
                promptBn = "Mg²⁺ এবং Cl⁻ আয়ন দ্বারা গঠিত ম্যাগনেসিয়াম ক্লোরাইডের সঠিক রাসায়নিক সংকেত কোনটি?",
                optionsEn = listOf("MgCl₂", "MgCl", "Mg₂Cl", "Mg₂Cl₂"),
                optionsBn = listOf("MgCl₂", "MgCl", "Mg₂Cl", "Mg₂Cl₂"),
                correctIndex = 0,
                hintEn = "One Mg²⁺ (+2) requires two Cl⁻ (-1) to balance net charge to 0.",
                hintBn = "একটি Mg²⁺ (+২) চার্জের সমতা করতে ২টি Cl⁻ (-১) অ্যানায়ন প্রয়োজন।",
                explanationEn = "Net charge must be zero: (+2) + 2(-1) = 0. The correct formula is MgCl₂.",
                explanationBn = "মোট চার্জ শূন্য হতে হবে: (+২) + ২(-১) = ০। তাই সঠিক সংকেত MgCl₂।"
            ),
            ChemistryQuestion(
                id = "w4_q2",
                worldId = 4,
                promptEn = "What is the correct chemical formula for Aluminium Sulfate formed by Al³⁺ and SO₄²⁻?",
                promptBn = "Al³⁺ এবং SO₄²⁻ মূলক দ্বারা গঠিত অ্যালুমিনিয়াম সালফেটের সঠিক রাসায়নিক সংকেত কোনটি?",
                optionsEn = listOf("Al₂(SO₄)₃", "AlSO₄", "Al₃(SO₄)₂", "Al₂SO₄₃"),
                optionsBn = listOf("Al₂(SO₄)₃", "AlSO₄", "Al₃(SO₄)₂", "Al₂SO₄₃"),
                correctIndex = 0,
                hintEn = "LCM of 3 and 2 is 6. Repeated polyatomic ion requires parentheses.",
                hintBn = "৩ ও ২ এর ল.সা.গু হলো ৬। বহু-পারমাণবিক মূলকে বন্ধনী ব্যবহার করতে হবে।",
                explanationEn = "2 Al³⁺ give +6 charge; 3 SO₄²⁻ give -6 charge. Since SO₄ is polyatomic, it is written as Al₂(SO₄)₃.",
                explanationBn = "২টি Al³⁺ দেয় +৬ এবং ৩টি SO₄²⁻ দেয় -৬ চার্জ। সালফেট মূলক বহু-পারমাণবিক হওয়ায় বন্ধনীসহ Al₂(SO₄)₃ লিখতে হয়।"
            )
        ),
        5 to listOf(
            ChemistryQuestion(
                id = "w5_q1",
                worldId = 5,
                promptEn = "What are the correct stoichiometric coefficients to balance: _ H₂ + _ O₂ → _ H₂O?",
                promptBn = "_ H₂ + _ O₂ → _ H₂O সমীকরণটির সমতাকৃত সঠিক সহগ কোনগুলো?",
                optionsEn = listOf("2, 1, 2", "1, 1, 1", "2, 2, 2", "1, 2, 1"),
                optionsBn = listOf("২, ১, ২", "১, ১, ১", "২, ২, ২", "১, ২, ১"),
                correctIndex = 0,
                hintEn = "Ensure 4 Hydrogen and 2 Oxygen atoms on both sides.",
                hintBn = "উভয় পাশে ৪টি হাইড্রোজেন ও ২টি অক্সিজেন পরমাণুর সমতা নিশ্চিত করুন।",
                explanationEn = "2H₂ + 1O₂ → 2H₂O gives exactly 4 H and 2 O atoms on each side.",
                explanationBn = "২H₂ + ১O₂ → ২H₂O সমীকরণে উভয় পাশে ঠিক ৪টি H এবং ২টি O পরমাণু সংরক্ষিত থাকে।"
            ),
            ChemistryQuestion(
                id = "w5_q2",
                worldId = 5,
                promptEn = "Which reaction type is represented by: HCl + NaOH → NaCl + H₂O?",
                promptBn = "HCl + NaOH → NaCl + H₂O বিক্রিয়াটি কোন ধরনের বিক্রিয়া?",
                optionsEn = listOf("Neutralization (Acid-Base)", "Decomposition", "Combustion", "Polymerization"),
                optionsBn = listOf("প্রশমন বিক্রিয়া (অম্ল-ক্ষার)", "বিয়োজন বিক্রিয়া", "দহন বিক্রিয়া", "পলিমারকরণ"),
                correctIndex = 0,
                hintEn = "Strong acid HCl reacts with strong base NaOH to yield neutral salt and water.",
                hintBn = "তীব্র এসিড HCl তীব্র ক্ষার NaOH এর সাথে বিক্রিয়া করে নিরপেক্ষ লবণ ও পানি তৈরি করে।",
                explanationEn = "An acid and base react stoichiometrically to produce a salt and water, which is a classic Neutralization reaction.",
                explanationBn = "এসিড ও ক্ষার বিক্রিয়া করে লবণ ও পানি উৎপন্ন করার প্রক্রিয়াকে প্রশমন বিক্রিয়া বলে।"
            )
        ),
        6 to listOf(
            ChemistryQuestion(
                id = "w6_q1",
                worldId = 6,
                promptEn = "How many moles are present in 36 grams of pure water (H₂O)? (Molar mass of H₂O = 18.0 g/mol)",
                promptBn = "৩৬ গ্রাম বিশুদ্ধ পানিতে (H₂O) কত মোল পানি বিদ্যমান? (পানির মোলার ভর = ১৮.০ গ্রাম/মোল)",
                optionsEn = listOf("2.0 moles", "1.0 mole", "18 moles", "0.5 moles"),
                optionsBn = listOf("২.০ মোল", "১.০ মোল", "১৮ মোল", "০.৫ মোল"),
                correctIndex = 0,
                hintEn = "Apply formula: n = mass (m) / molar mass (M).",
                hintBn = "সূত্র প্রয়োগ করুন: n = ভর (m) / মোলার ভর (M)।",
                explanationEn = "n = m / M = 36 g / 18.0 g/mol = 2.0 moles.",
                explanationBn = "n = m / M = ৩৬ গ্রাম / ১৮.০ গ্রাম/মোল = ২.০ মোল।"
            ),
            ChemistryQuestion(
                id = "w6_q2",
                worldId = 6,
                promptEn = "What is the defined value of Avogadro's constant (Nₐ)?",
                promptBn = "অ্যাভোগাড্রো ধ্রুবকের (Nₐ) আন্তর্জাতিক মান কোনটি?",
                optionsEn = listOf("6.022 × 10²³ mol⁻¹", "3.00 × 10⁸ m/s", "9.81 m/s²", "1.602 × 10⁻¹⁹ C"),
                optionsBn = listOf("৬.০২২ × ১০²³ mol⁻¹", "৩.০০ × ১০⁸ m/s", "৯.৮১ m/s²", "১.৬০২ × ১০⁻¹৯ C"),
                correctIndex = 0,
                hintEn = "It represents the number of specified particles in exactly one mole.",
                hintBn = "এক মোল পদার্থে উপস্থিত পরমাণু বা অণুর সংখ্যা।",
                explanationEn = "Avogadro's constant is defined as 6.02214076 × 10²³ mol⁻¹.",
                explanationBn = "এক মোল যেকোনো পদার্থে ঠিক ৬.০২২১৪০৭৬ × ১০²³ টি কণা থাকে।"
            )
        ),
        7 to listOf(
            ChemistryQuestion(
                id = "w7_q1",
                worldId = 7,
                promptEn = "What is the pH of a solution with a hydrogen ion concentration [H⁺] = 1.0 × 10⁻³ mol/L?",
                promptBn = "একটি দ্রবণে হাইড্রোজেন আয়নের ঘনমাত্রা [H⁺] = ১.০ × ১০⁻³ mol/L হলে এর pH কত?",
                optionsEn = listOf("3.0", "7.0", "11.0", "1.0"),
                optionsBn = listOf("৩.০", "৭.০", "১১.০", "১.০"),
                correctIndex = 0,
                hintEn = "pH = -log₁₀[H⁺].",
                hintBn = "pH = -log₁₀[H⁺] সূত্র প্রয়োগ করুন।",
                explanationEn = "pH = -log₁₀(10⁻³) = -(-3) = 3.0. This indicates an acidic solution.",
                explanationBn = "pH = -log₁₀(১০⁻³) = ৩.০। এটি একটি অম্লীয় দ্রবণ।"
            ),
            ChemistryQuestion(
                id = "w7_q2",
                worldId = 7,
                promptEn = "At 25°C in dilute aqueous solution, if pH = 4.0, what is the pOH?",
                promptBn = "২৫° সে. তাপমাত্রায় লঘু জলীয় দ্রবণে pH = ৪.০ হলে, pOH এর মান কত?",
                optionsEn = listOf("10.0", "14.0", "4.0", "7.0"),
                optionsBn = listOf("১০.০", "১৪.০", "৪.০", "৭.০"),
                correctIndex = 0,
                hintEn = "Use the relationship: pH + pOH = 14.0.",
                hintBn = "pH + pOH = ১৪.০ সূত্র ব্যবহার করুন।",
                explanationEn = "pOH = 14.0 - pH = 14.0 - 4.0 = 10.0.",
                explanationBn = "pOH = ১৪.০ - pH = ১৪.০ - ৪.০ = ১০.০।"
            )
        ),
        8 to listOf(
            ChemistryQuestion(
                id = "w8_q1",
                worldId = 8,
                promptEn = "If you dissolve 0.50 moles of solute in solvent to make a final solution volume of 2.0 Litres, what is the Molarity?",
                promptBn = "০.৫০ মোল দ্রব দ্রাবকে দ্রবীভূত করে দ্রবণের চূড়ান্ত আয়তন ২.০ লিটার করা হলে মোলারিটি (M) কত?",
                optionsEn = listOf("0.25 M", "1.0 M", "0.50 M", "4.0 M"),
                optionsBn = listOf("০.২৫ M", "১.০ M", "০.৫০ M", "৪.০ M"),
                correctIndex = 0,
                hintEn = "Molarity M = moles / volume in Litres.",
                hintBn = "মোলারিটি M = মোল সংখ্যা / লিটারে আয়তন।",
                explanationEn = "M = 0.50 mol / 2.0 L = 0.25 mol/L (M).",
                explanationBn = "M = ০.৫০ মোল / ২.০ লিটার = ০.২৫ M।"
            ),
            ChemistryQuestion(
                id = "w8_q2",
                worldId = 8,
                promptEn = "Using dilution formula C₁V₁ = C₂V₂, how much 2.0 M stock solution (V₁) is needed to make 100 mL of 0.50 M solution (C₂)?",
                promptBn = "C₁V₁ = C₂V₂ সূত্র অনুযায়ী, ১০০ মিলি ০.৫০ M দ্রবণ তৈরি করতে ২.০ M মূল দ্রবণের (V₁) কত আয়তন প্রয়োজন?",
                optionsEn = listOf("25 mL", "50 mL", "10 mL", "75 mL"),
                optionsBn = listOf("২৫ মিলি", "৫০ মিলি", "১০ মিলি", "৭৫ মিলি"),
                correctIndex = 0,
                hintEn = "V₁ = (C₂ × V₂) / C₁.",
                hintBn = "V₁ = (C₂ × V₂) / C₁ সূত্র প্রয়োগ করুন।",
                explanationEn = "V₁ = (0.50 M × 100 mL) / 2.0 M = 50 / 2 = 25 mL.",
                explanationBn = "V₁ = (০.৫০ M × ১০০ মিলি) / ২.০ M = ৫০ / ২ = ২৫ মিলি।"
            )
        ),
        9 to listOf(
            ChemistryQuestion(
                id = "w9_q1",
                worldId = 9,
                promptEn = "Which functional group characterizes carboxylic acids such as acetic acid (CH₃COOH)?",
                promptBn = "অ্যাসিটিক এসিডের (CH₃COOH) মতো কার্বক্সিলিক এসিডের বৈশিষ্ট্যমূলক কার্যকরী মূলক কোনটি?",
                optionsEn = listOf("-COOH (Carboxyl)", "-OH (Hydroxyl)", "-CHO (Aldehyde)", "-NH₂ (Amine)"),
                optionsBn = listOf("-COOH (কার্বক্সিল)", "-OH (হাইড্রক্সিল)", "-CHO (অ্যালডিহাইড)", "-NH₂ (অ্যামিন)"),
                correctIndex = 0,
                hintEn = "Consists of a carbonyl (C=O) and a hydroxyl (-OH) group on the same carbon atom.",
                hintBn = "একই কার্বনে কার্বনিল (C=O) ও হাইড্রক্সিল (-OH) মূলকের সমন্বয়।",
                explanationEn = "-COOH is the carboxyl group which gives acidic properties to organic acids.",
                explanationBn = "-COOH হলো কার্বক্সিল মূলক যা জৈব এসিডসমূহের অম্লীয় ধর্মের মূল কারণ।"
            ),
            ChemistryQuestion(
                id = "w9_q2",
                worldId = 9,
                promptEn = "What is the general molecular formula for saturated open-chain alkanes?",
                promptBn = "সম্পৃক্ত মুক্ত শিকল অ্যালকেনের সাধারণ আণবিক সংকেত কোনটি?",
                optionsEn = listOf("CnH2n+2", "CnH2n", "CnH2n-2", "CnHn"),
                optionsBn = listOf("CnH2n+2", "CnH2n", "CnH2n-2", "CnHn"),
                correctIndex = 0,
                hintEn = "Methane is CH₄ (n=1), Ethane is C₂H₆ (n=2).",
                hintBn = "মিথেন হলো CH₄ (n=১), ইথেন হলো C₂H₆ (n=২)।",
                explanationEn = "Alkanes contain single C-C bonds with maximum hydrogen saturation: CnH2n+2.",
                explanationBn = "অ্যালকেনে কার্বন-কার্বন একক বন্ধন থাকে এবং এর সাধারণ সংকেত CnH2n+2।"
            )
        ),
        10 to listOf(
            ChemistryQuestion(
                id = "w10_q1",
                worldId = 10,
                promptEn = "Which inorganic pharmaceutical compound is widely used as an insoluble antacid for gastric hyperacidity?",
                promptBn = "পাকস্থলীর অতিরিক্ত এসিডিটি নিরাময়ে বহুল ব্যবহৃত অদ্রবণীয় অ্যান্টাসিড যৌগ কোনটি?",
                optionsEn = listOf("Aluminium Hydroxide / Magnesium Hydroxide", "Sodium Cyanide", "Hydrochloric Acid", "Sulfuric Acid"),
                optionsBn = listOf("অ্যালুমিনিয়াম হাইড্রক্সাইড / ম্যাগনেসিয়াম হাইড্রক্সাইড", "সোডিয়াম সায়ানাইড", "হাইড্রোক্লোরিক এসিড", "সালফিউরিক এসিড"),
                correctIndex = 0,
                hintEn = "Insoluble bases neutralize excess stomach HCl without causing systemic alkalosis.",
                hintBn = "অদ্রবণীয় ক্ষারকীয় যৌগ যা সিস্টেমিক ক্ষারত্ব না বাড়িয়ে অতিরিক্ত এসিড প্রশমিত করে।",
                explanationEn = "Al(OH)₃ and Mg(OH)₂ neutralize gastric HCl: Al(OH)₃ + 3HCl → AlCl₃ + 3H₂O.",
                explanationBn = "Al(OH)₃ ও Mg(OH)₂ অ্যান্টাসিড হিসেবে পাকস্থলীর HCl প্রশমিত করে স্বস্তি প্রদান করে।"
            ),
            ChemistryQuestion(
                id = "w10_q2",
                worldId = 10,
                promptEn = "What is the standard concentration of isotonic normal saline IV fluid?",
                promptBn = "আইসোটোনিক নরমাল স্যালাইন আইভি ফ্লুইডের প্রমাণ ঘনমাত্রা কত?",
                optionsEn = listOf("0.9% w/v NaCl (9 g/L)", "5.0% w/v NaCl", "0.1% w/v NaCl", "20% w/v NaCl"),
                optionsBn = listOf("০.৯% w/v NaCl (৯ গ্রাম/লিটার)", "৫.০% w/v NaCl", "০.১% w/v NaCl", "২০% w/v NaCl"),
                correctIndex = 0,
                hintEn = "Matches the osmotic pressure and tonicity of human blood plasma.",
                hintBn = "মানব রক্তের প্লাজমার সম-অভিস্রবণিক চাপের সমান।",
                explanationEn = "0.9% w/v NaCl (9 g NaCl per 1000 mL water) is isotonic with blood plasma, preventing cell lysis or crenation.",
                explanationBn = "০.৯% সোডিয়াম ক্লোরাইড দ্রবণ রক্তের সাথে আইসোটোনিক হওয়ায় লোহিত রক্তকণিকার কোনো ক্ষতি করে না।"
            )
        )
    )

    val REVIEW_ITEMS: List<ReviewCard> = listOf(
        ReviewCard("rev_z", "atomic_number", 1, "Atomic Number (Z)", "পারমাণবিক সংখ্যা (Z)", "Z = p⁺", "Atomic number equals the number of protons in an atom's nucleus.", "পরমাণুর নিউক্লিয়াসে অবস্থিত প্রোটন সংখ্যাই হলো পারমাণবিক সংখ্যা।", 0L, 1, 0),
        ReviewCard("rev_a", "mass_number", 1, "Mass Number (A)", "ভর সংখ্যা (A)", "A = p⁺ + n⁰", "Total number of protons and neutrons in the nucleus.", "নিউক্লিয়াসে প্রোটন ও নিউট্রনের মোট সমষ্টি।", 0L, 1, 0),
        ReviewCard("rev_octet", "octet_rule", 3, "Octet Rule", "অষ্টক নিয়ম", "Valence e⁻ = 8", "Atoms transfer or share electrons to attain 8 valence electrons.", "স্থিতিশীলতা অর্জনের জন্য শেষ শক্তিস্তরে ৮টি ইলেকট্রন পূর্ণ করার প্রবণতা।", 0L, 1, 0),
        ReviewCard("rev_mole", "mole_formula", 6, "Mole Formula", "মোলের মূল সূত্র", "n = m / M", "Amount of substance in moles equals mass in grams divided by molar mass.", "মোল সংখ্যা = গ্রামে প্রকাশিত ভর / মোলার ভর।", 0L, 1, 0),
        ReviewCard("rev_ph", "ph_scale", 7, "pH Equation", "pH সমীকরণ", "pH = -log₁₀[H⁺]", "Logarithmic measure of hydrogen ion concentration in solution.", "দ্রবণে হাইড্রোজেন আয়ন ঘনমাত্রার ঋণাত্মক লগারিদম।", 0L, 1, 0),
        ReviewCard("rev_dilution", "dilution_rule", 8, "Dilution Law", "লঘুকরণ সূত্র", "C₁V₁ = C₂V₂", "Initial concentration × volume equals final concentration × volume.", "লঘুকরণের পূর্বে ও পরে দ্রবের মোট মোল সংখ্যা একই থাকে।", 0L, 1, 0),
        ReviewCard("rev_saline", "pharma_saline", 10, "Normal Saline", "নরমাল স্যালাইন", "0.9% w/v NaCl", "Isotonic sterile solution for fluid and electrolyte replacement.", "রক্তের সম-অভিস্রবণিক জীবাণুমুক্ত জরুরি স্যালাইন ফ্লুইড।", 0L, 1, 0)
    )
}
