package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BPharmSyllabusData
import com.example.data.ChemistryUniverseData
import com.example.data.Strings
import com.example.model.ReviewCard
import com.example.model.SyllabusTopic
import com.example.ui.components.SciFiCard
import com.example.ui.components.SciFiTopBar
import com.example.ui.theme.*

@Composable
fun NotebookScreen(
    currentLang: String,
    score: Int,
    bookmarkedIds: Set<String>,
    onToggleBookmark: (String) -> Unit,
    onToggleLang: () -> Unit,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf(
        if (currentLang == "bn") "সূত্র ও নিয়ম" else "Formulas",
        if (currentLang == "bn") "আয়ন ও মূলক" else "Ions Sheet",
        if (currentLang == "bn") "রিভিউ কিউ" else "Review Queue",
        if (currentLang == "bn") "বি.ফার্ম সিলেবাস" else "B.Pharm Syllabus"
    )

    Scaffold(
        topBar = {
            SciFiTopBar(
                title = Strings.notebookTitle(currentLang),
                score = score,
                currentLang = currentLang,
                onToggleLang = onToggleLang,
                onBack = onBack,
                testTag = "notebook_top_bar"
            )
        },
        containerColor = SpaceBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Scrollable Tab Row
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = SurfaceNavy,
                contentColor = CyanNeon,
                edgePadding = 16.dp,
                modifier = Modifier.testTag("notebook_tab_row")
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                when (selectedTab) {
                    0 -> FormulasTab(currentLang)
                    1 -> IonsTab(currentLang)
                    2 -> ReviewQueueTab(currentLang, bookmarkedIds, onToggleBookmark)
                    3 -> SyllabusTab(currentLang)
                }
            }
        }
    }
}

@Composable
private fun FormulasTab(currentLang: String) {
    val formulas = listOf(
        Triple("Amount of Substance", "n = m / M", if (currentLang == "bn") "মোল = গ্রামে ভর / মোলার ভর (গ্রাম/মোল)" else "Moles = Mass (g) / Molar Mass (g/mol)"),
        Triple("Particles & Avogadro", "N = n × Nₐ", if (currentLang == "bn") "কণা সংখ্যা = মোল × ৬.০২২ × ১০²³" else "Particles = Moles × 6.022 × 10²³"),
        Triple("Solution Molarity", "M = n / V_L", if (currentLang == "bn") "মোলারিটি = দ্রবের মোল / দ্রবণের আয়তন (লিটার)" else "Molarity = Moles Solute / Solution Volume (L)"),
        Triple("Dilution Law", "C₁V₁ = C₂V₂", if (currentLang == "bn") "লঘুকরণের পূর্বে ও পরে দ্রবের মোল অপরিবর্তিত থাকে" else "Conservation of solute mass during dilution"),
        Triple("pH Scale", "pH = -log₁₀[H⁺]", if (currentLang == "bn") "হাইড্রোজেন আয়ন ঘনমাত্রার ঋণাত্মক লগারিদম" else "Negative logarithm of hydrogen ion activity"),
        Triple("Water Self-Ionization", "pH + pOH = 14.0", if (currentLang == "bn") "২৫° সে. তাপমাত্রায় লঘু জলীয় দ্রবণে Kw = 10⁻¹⁴" else "At 25°C in dilute aqueous solution (Kw = 10⁻¹⁴)"),
        Triple("Charge Balance", "Σ q⁺ + Σ q⁻ = 0", if (currentLang == "bn") "আয়নিক যৌগের মোট ধনাত্মক ও ঋণাত্মক চার্জ শূন্য হতে হয়" else "Total positive charges must equal negative charges")
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(formulas) { (name, math, desc) ->
            SciFiCard(borderColor = BorderCyan) {
                Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CyanNeon)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SpaceBlack)
                        .padding(8.dp)
                ) {
                    Text(text = math, color = EmeraldLight, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = desc, fontSize = 11.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun IonsTab(currentLang: String) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(ChemistryUniverseData.COMMON_IONS) { ion ->
            SciFiCard(borderColor = if (ion.charge > 0) BorderCyan else BorderViolet) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = ion.formula,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (ion.charge > 0) CyanNeon else VioletNeon
                        )
                        Text(
                            text = if (currentLang == "bn") ion.nameBn else ion.nameEn,
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Charge: ${if (ion.charge > 0) "+${ion.charge}" else "${ion.charge}"}",
                            fontWeight = FontWeight.Bold,
                            color = if (ion.charge > 0) CyanGlow else VioletDeep,
                            fontSize = 13.sp
                        )
                        if (ion.isPolyatomic) {
                            Text(
                                text = if (currentLang == "bn") "বহু-পারমাণবিক মূলক" else "Polyatomic",
                                fontSize = 10.sp,
                                color = AmberLight
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewQueueTab(
    currentLang: String,
    bookmarkedIds: Set<String>,
    onToggleBookmark: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(ChemistryUniverseData.REVIEW_ITEMS) { card ->
            val isBookmarked = bookmarkedIds.contains(card.id)
            SciFiCard(borderColor = if (isBookmarked) AmberNeon else BorderSubtle) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (currentLang == "bn") card.titleBn else card.titleEn,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = card.formula,
                            fontFamily = FontFamily.Monospace,
                            color = CyanNeon,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (currentLang == "bn") card.summaryBn else card.summaryEn,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 15.sp
                        )
                    }

                    IconButton(
                        onClick = { onToggleBookmark(card.id) },
                        modifier = Modifier.testTag("bookmark_${card.id}")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) AmberNeon else TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SyllabusTab(currentLang: String) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = if (currentLang == "bn")
                    "ফার্মেসি কাউন্সিল (PCI) নির্ধারিত সিলেবাস ভিত্তি (2026–27)"
                else
                    "Pharmacy Council of India (PCI) Foundation Mapping (2026–27)",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = EmeraldLight
            )
        }

        items(BPharmSyllabusData.SYLLABUS_TOPICS) { topic ->
            SciFiCard(borderColor = BorderSubtle) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = topic.code,
                        color = CyanNeon,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = topic.alignmentStatus,
                        color = EmeraldNeon,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (currentLang == "bn") topic.subjectBn else topic.subjectEn,
                    fontSize = 11.sp,
                    color = VioletNeon,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = if (currentLang == "bn") topic.topicBn else topic.topicEn,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (currentLang == "bn") topic.descriptionBn else topic.descriptionEn,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
