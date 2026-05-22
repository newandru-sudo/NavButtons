package com.example.navbuttons.ui.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.navbuttons.theme.CardBorder
import com.example.navbuttons.theme.CardSurface
import com.example.navbuttons.theme.DeepNavy
import com.example.navbuttons.theme.DimBlue
import com.example.navbuttons.theme.GlowBlue
import com.example.navbuttons.theme.GlowBlueSoft
import com.example.navbuttons.theme.IceWhite
import com.example.navbuttons.theme.NavButtonsTheme
import com.example.navbuttons.theme.NeonBlue
import com.example.navbuttons.theme.NeonBlueBright
import com.example.navbuttons.theme.NeonBlueDim
import com.example.navbuttons.theme.PressedSurface
import com.example.navbuttons.theme.SoftWhite
import kotlinx.coroutines.launch

// ---------------------------------------------------------------------------
// Neon glow modifier
// ---------------------------------------------------------------------------
fun Modifier.neonGlow(
    color: Color,
    blurRadius: Dp,
    alpha: Float,
): Modifier = this.drawBehind {
    drawIntoCanvas { canvas ->
        val paint = Paint().apply {
            asFrameworkPaint().apply {
                isAntiAlias = true
                this.color = android.graphics.Color.TRANSPARENT
                setShadowLayer(
                    blurRadius.toPx(),
                    0f, 0f,
                    color.copy(alpha = alpha).toArgb(),
                )
            }
        }
        canvas.drawRoundRect(
            left   = 0f,
            top    = 0f,
            right  = size.width,
            bottom = size.height,
            radiusX = 24.dp.toPx(),
            radiusY = 24.dp.toPx(),
            paint  = paint,
        )
    }
}

// ---------------------------------------------------------------------------
// Big card button (Home / Work)
// ---------------------------------------------------------------------------
@Composable
fun NeonCardButton(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val glowAlpha  = remember { Animatable(0f) }
    val glowRadius = remember { Animatable(0f) }
    val scope      = rememberCoroutineScope()
    var pressed    by remember { mutableStateOf(false) }

    val cardShape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .clip(cardShape)
            // radial glow overlay from centre
            .background(
                brush = if (pressed) {
                    Brush.radialGradient(
                        colors = listOf(
                            NeonBlue.copy(alpha = glowAlpha.value * 0.35f),
                            NeonBlueDim.copy(alpha = glowAlpha.value * 0.15f),
                            Color.Transparent,
                        ),
                    )
                } else {
                    Brush.radialGradient(colors = listOf(Color.Transparent, Color.Transparent))
                },
            )
            // card base
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(CardSurface, DeepNavy),
                ),
            )
            .border(
                width = if (pressed) 1.5.dp else 1.dp,
                brush = Brush.linearGradient(
                    colors = if (pressed)
                        listOf(NeonBlueBright, NeonBlue)
                    else
                        listOf(CardBorder, CardBorder.copy(alpha = 0.4f)),
                ),
                shape = cardShape,
            )
            // neon outer shadow
            .then(
                if (pressed) Modifier.neonGlow(NeonBlue, blurRadius = glowRadius.value.dp, alpha = glowAlpha.value)
                else Modifier
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        scope.launch {
                            launch { glowAlpha.animateTo(1f,  tween(120, easing = FastOutSlowInEasing)) }
                            launch { glowRadius.animateTo(32f, tween(120, easing = FastOutSlowInEasing)) }
                        }
                        tryAwaitRelease()
                        scope.launch {
                            launch { glowAlpha.animateTo(0f,  tween(300, easing = FastOutSlowInEasing)) }
                            launch { glowRadius.animateTo(0f,  tween(300, easing = FastOutSlowInEasing)) }
                        }
                        pressed = false
                        onClick()
                    },
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 36.dp, horizontal = 16.dp),
        ) {
            // Icon with neon tint
            Box(contentAlignment = Alignment.Center) {
                // soft ambient glow behind icon
                if (pressed) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(GlowBlue, Color.Transparent),
                                ),
                                shape = RoundedCornerShape(50),
                            ),
                    )
                }
                Icon(
                    imageVector        = icon,
                    contentDescription = label,
                    tint               = if (pressed) NeonBlueBright else SoftWhite,
                    modifier           = Modifier.size(52.dp),
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text       = label,
                fontSize   = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color      = if (pressed) NeonBlueBright else IceWhite,
                letterSpacing = 2.sp,
                textAlign  = TextAlign.Center,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Flat "Other" button
// ---------------------------------------------------------------------------
@Composable
fun NeonFlatButton(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val glowAlpha  = remember { Animatable(0f) }
    val scope      = rememberCoroutineScope()
    var pressed    by remember { mutableStateOf(false) }

    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .background(
                brush = if (pressed) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            NeonBlue.copy(alpha = glowAlpha.value * 0.22f),
                            NeonBlueDim.copy(alpha = glowAlpha.value * 0.1f),
                        ),
                    )
                } else {
                    Brush.horizontalGradient(colors = listOf(Color.Transparent, Color.Transparent))
                },
            )
            .background(Color(0xFF0B1530))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = if (pressed)
                        listOf(NeonBlue, NeonBlueBright)
                    else
                        listOf(CardBorder, CardBorder.copy(alpha = 0.3f)),
                ),
                shape = shape,
            )
            .then(
                if (pressed) Modifier.neonGlow(NeonBlue, blurRadius = 20.dp, alpha = glowAlpha.value * 0.8f)
                else Modifier
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        scope.launch { glowAlpha.animateTo(1f, tween(120)) }
                        tryAwaitRelease()
                        scope.launch { glowAlpha.animateTo(0f, tween(300)) }
                        pressed = false
                        onClick()
                    },
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment    = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 18.dp, horizontal = 24.dp),
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = label,
                tint               = if (pressed) NeonBlueBright else DimBlue,
                modifier           = Modifier.size(22.dp),
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text          = label,
                fontSize      = 15.sp,
                fontWeight    = FontWeight.Medium,
                color         = if (pressed) NeonBlueBright else DimBlue,
                letterSpacing = 2.sp,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Main Screen
// ---------------------------------------------------------------------------
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF070E1F), Color(0xFF040912)),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // ── Top label ────────────────────────────────────────────────
            Text(
                text          = "SELECT MODE",
                fontSize      = 11.sp,
                fontWeight    = FontWeight.Bold,
                color         = DimBlue,
                letterSpacing = 4.sp,
            )
            Spacer(modifier = Modifier.height(32.dp))

            // ── Home & Work side by side ──────────────────────────────────
            Row(
                modifier            = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                NeonCardButton(
                    label    = "HOME",
                    icon     = Icons.Rounded.Home,
                    modifier = Modifier
                        .weight(1f)
                        .height(200.dp),
                )
                NeonCardButton(
                    label    = "WORK",
                    icon     = Icons.Rounded.Work,
                    modifier = Modifier
                        .weight(1f)
                        .height(200.dp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Other flat button ─────────────────────────────────────────
            NeonFlatButton(
                label    = "OTHER",
                icon     = Icons.Rounded.MoreHoriz,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Preview
// ---------------------------------------------------------------------------
@Preview(showBackground = true, backgroundColor = 0xFF040912)
@Composable
fun MainScreenPreview() {
    NavButtonsTheme { MainScreen() }
}
