package com.lia.dialer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light Colors
private val LightPrimary = Color(0xFF0B7E3D)
private val LightSecondary = Color(0xFF00E676)
private val LightTertiary = Color(0xFF00BCD4)
private val LightBackground = Color(0xFFFAFAFA)
private val LightSurface = Color(0xFFFFFFFF)
private val LightError = Color(0xFFFF5252)

// Dark Colors
private val DarkPrimary = Color(0xFF00E676)
private val DarkSecondary = Color(0xFF1DE9B6)
private val DarkTertiary = Color(0xFF00E5FF)
private val DarkBackground = Color(0xFF121212)
private val DarkSurface = Color(0xFF1E1E1E)
private val DarkError = Color(0xFFFF5252)

// AMOLED Colors
private val AmoledBackground = Color(0xFF000000)
private val AmoledSurface = Color(0xFF0F0F0F)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = LightSecondary,
    tertiary = LightTertiary,
    background = LightBackground,
    surface = LightSurface,
    error = LightError
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    error = DarkError
)

private val AmoledColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkTertiary,
    background = AmoledBackground,
    surface = AmoledSurface,
    error = DarkError
)

@Composable
fun LiaDialerTheme(
    darkTheme: Boolean = false,
    amoledMode: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        amoledMode -> AmoledColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
