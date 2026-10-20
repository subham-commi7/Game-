package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Strings
import com.example.ui.components.AtomVisualCanvas
import com.example.ui.components.NeonButton
import com.example.ui.theme.*

@Composable
fun LaunchScreen(
    currentLang: String,
    onToggleLang: () -> Unit,
    onStart: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_scale")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "core_pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(SpaceBlack, SpaceNavy, SurfaceNavy)
                )
            )
            .padding(24.dp)
            .testTag("launch_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Language selector top badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceCard)
                    .border(1.dp, BorderCyan, RoundedCornerShape(20.dp))
                    .clickable { onToggleLang() }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .testTag("launch_lang_selector"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Translate,
                    contentDescription = "Language",
                    tint = CyanNeon,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (currentLang == "bn") "বাংলা (Default) / English" else "English / বাংলা",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Animated Quantum Atom Core
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .scale(pulse)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(CyanNeon.copy(alpha = 0.2f), VioletNeon.copy(alpha = 0.1f), SpaceNavy)
                        )
                    )
                    .border(2.dp, CyanGlow, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AtomVisualCanvas(
                    protons = 6,
                    neutrons = 6,
                    electrons = 6,
                    modifier = Modifier.size(180.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Game Title
            Text(
                text = Strings.APP_NAME,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CyanNeon,
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = if (currentLang == "bn") "রসায়ন মেমোরি ইউনিভার্স" else "CHEMISTRY MEMORY UNIVERSE",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = VioletNeon,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tagline
            Text(
                text = if (currentLang == "bn") Strings.TAGLINE_BN else Strings.TAGLINE_EN,
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // B.Pharm target indicator
            Text(
                text = if (currentLang == "bn") "বি.ফার্ম প্রিপারেশন ও মৌলিক রসায়ন শিক্ষা" else "B.Pharm Foundation & Memory Training",
                fontSize = 12.sp,
                color = EmeraldLight,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Primary Launch Action
            NeonButton(
                text = Strings.startExpedition(currentLang),
                onClick = onStart,
                color = CyanNeon,
                textColor = SpaceBlack,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(52.dp),
                testTag = "launch_start_expedition_btn"
            )
        }
    }
}
