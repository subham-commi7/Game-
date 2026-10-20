package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SciFiCard(
    modifier: Modifier = Modifier,
    borderColor: Color = BorderCyan,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceCard.copy(alpha = 0.95f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "neon_button",
    color: Color = CyanNeon,
    textColor: Color = SpaceBlack,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .heightIn(min = 48.dp)
            .testTag(testTag),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = textColor,
            disabledContainerColor = SurfaceCardHover,
            disabledContentColor = TextTertiary
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SciFiTopBar(
    title: String,
    score: Int,
    currentLang: String,
    onToggleLang: () -> Unit,
    onBack: (() -> Unit)? = null,
    testTag: String = "scifi_top_bar"
) {
    TopAppBar(
        modifier = Modifier
            .background(SpaceBlack)
            .testTag(testTag),
        title = {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1
            )
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("nav_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = CyanNeon
                    )
                }
            }
        },
        actions = {
            // Points pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceNavy)
                    .border(1.dp, BorderCyan, RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Score",
                    tint = AmberNeon,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$score",
                    color = AmberLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Language switcher pill button
            IconButton(
                onClick = onToggleLang,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("toggle_language_btn")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceCardHover)
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = "Switch Language",
                        tint = VioletNeon,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = if (currentLang == "bn") "BN" else "EN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SpaceBlack,
            titleContentColor = TextPrimary
        )
    )
}

@Composable
fun AtomVisualCanvas(
    protons: Int,
    neutrons: Int,
    electrons: Int,
    modifier: Modifier = Modifier,
    reducedMotion: Boolean = false,
    visualEffectsHigh: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "atom_rotation")
    val animatedAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "electron_orbit"
    )

    val angle = if (reducedMotion) 45f else animatedAngle

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceNavy)
            .border(1.dp, BorderCyan, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val baseRadius = size.height * 0.42f

            // Background glow when visual effects are high
            if (visualEffectsHigh) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(CyanNeon.copy(alpha = 0.25f), Color.Transparent),
                        center = center,
                        radius = baseRadius * 1.2f
                    ),
                    radius = baseRadius * 1.2f,
                    center = center
                )
            }

            // K Shell
            val rK = baseRadius * 0.35f
            drawCircle(
                color = BorderCyan,
                radius = rK,
                center = center,
                style = Stroke(width = 1.5f)
            )

            // L Shell
            val rL = baseRadius * 0.65f
            if (electrons > 2) {
                drawCircle(
                    color = BorderViolet,
                    radius = rL,
                    center = center,
                    style = Stroke(width = 1.5f)
                )
            }

            // M Shell
            val rM = baseRadius * 0.95f
            if (electrons > 10) {
                drawCircle(
                    color = CyanGlow.copy(alpha = 0.5f),
                    radius = rM,
                    center = center,
                    style = Stroke(width = 1.5f)
                )
            }

            // Central Nucleus: Protons (Cyan) & Neutrons (Violet)
            val pRadius = 7f
            val maxDrawP = protons.coerceAtMost(12)
            for (p in 0 until maxDrawP) {
                val pAngle = (p * (360f / maxDrawP)) * (Math.PI / 180f)
                val offset = Offset(
                    center.x + (12f * cos(pAngle)).toFloat(),
                    center.y + (12f * sin(pAngle)).toFloat()
                )
                drawCircle(color = CyanNeon, radius = pRadius, center = offset)
            }
            val maxDrawN = neutrons.coerceAtMost(12)
            for (n in 0 until maxDrawN) {
                val nAngle = ((n * (360f / maxDrawN)) + 15f) * (Math.PI / 180f)
                val offset = Offset(
                    center.x + (16f * cos(nAngle)).toFloat(),
                    center.y + (16f * sin(nAngle)).toFloat()
                )
                drawCircle(color = VioletNeon, radius = pRadius * 0.9f, center = offset)
            }

            // Orbiting Electrons (Yellow / Cyan sparks)
            val eCountK = electrons.coerceAtMost(2)
            for (e in 0 until eCountK) {
                val eAngle = ((e * 180f) + angle) * (Math.PI / 180f)
                val eOffset = Offset(
                    center.x + (rK * cos(eAngle)).toFloat(),
                    center.y + (rK * sin(eAngle)).toFloat()
                )
                drawCircle(color = AmberLight, radius = 6f, center = eOffset)
            }

            val eCountL = (electrons - 2).coerceIn(0, 8)
            for (e in 0 until eCountL) {
                val eAngle = ((e * (360f / 8)) - angle * 0.8f) * (Math.PI / 180f)
                val eOffset = Offset(
                    center.x + (rL * cos(eAngle)).toFloat(),
                    center.y + (rL * sin(eAngle)).toFloat()
                )
                drawCircle(color = CyanGlow, radius = 5.5f, center = eOffset)
            }

            val eCountM = (electrons - 10).coerceIn(0, 8)
            for (e in 0 until eCountM) {
                val eAngle = ((e * (360f / 8)) + angle * 0.6f) * (Math.PI / 180f)
                val eOffset = Offset(
                    center.x + (rM * cos(eAngle)).toFloat(),
                    center.y + (rM * sin(eAngle)).toFloat()
                )
                drawCircle(color = EmeraldLight, radius = 5f, center = eOffset)
            }
        }

        // Subatomic legend badge
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(SpaceBlack.copy(alpha = 0.85f))
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("p⁺: $protons", color = CyanNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("n⁰: $neutrons", color = VioletNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("e⁻: $electrons", color = AmberLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun BeakerCanvas(
    liquidLevel: Float, // 0.0f to 1.0f
    liquidColor: Color,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(160.dp, 180.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Beaker outline
            val left = w * 0.2f
            val right = w * 0.8f
            val top = h * 0.15f
            val bottom = h * 0.85f

            // Liquid level
            val liquidHeight = (bottom - top) * liquidLevel.coerceIn(0f, 1f)
            val liquidTop = bottom - liquidHeight

            // Fill liquid
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(liquidColor.copy(alpha = 0.5f), liquidColor)
                ),
                topLeft = Offset(left + 2f, liquidTop),
                size = Size(right - left - 4f, liquidHeight)
            )

            // Beaker glass lines
            drawLine(Color.White, Offset(left, top), Offset(left, bottom), strokeWidth = 3f)
            drawLine(Color.White, Offset(left, bottom), Offset(right, bottom), strokeWidth = 3f)
            drawLine(Color.White, Offset(right, bottom), Offset(right, top), strokeWidth = 3f)

            // Lip of beaker
            drawLine(Color.White, Offset(left - 8f, top), Offset(left, top), strokeWidth = 3f)
            drawLine(Color.White, Offset(right, top), Offset(right + 8f, top), strokeWidth = 3f)

            // Measurement marks
            for (step in 1..4) {
                val markY = bottom - ((bottom - top) * (step / 5f))
                drawLine(Color.White.copy(alpha = 0.6f), Offset(left, markY), Offset(left + 15f, markY), strokeWidth = 2f)
            }
        }

        Text(
            text = label,
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 6.dp)
        )
    }
}
