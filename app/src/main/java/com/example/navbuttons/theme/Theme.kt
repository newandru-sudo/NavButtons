package com.example.navbuttons.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NavButtonsDarkColorScheme = darkColorScheme(
    primary          = NeonBlue,
    onPrimary        = IceWhite,
    primaryContainer = MediumNavy,
    background       = DeepNavy,
    onBackground     = IceWhite,
    surface          = CardSurface,
    onSurface        = IceWhite,
    outline          = CardBorder,
)

@Composable
fun NavButtonsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NavButtonsDarkColorScheme,
        typography  = Typography,
        content     = content,
    )
}
