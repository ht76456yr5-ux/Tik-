package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TikSaveDarkColorScheme = darkColorScheme(
    primary = NeonPurple,
    onPrimary = Color.White,
    primaryContainer = NeonPurpleDark,
    onPrimaryContainer = Color(0xFFF3E8FF),
    secondary = VividMagenta,
    onSecondary = Color.White,
    secondaryContainer = VividMagentaDark,
    onSecondaryContainer = Color(0xFFFCE7F3),
    tertiary = ElectricCyan,
    onTertiary = Color.Black,
    background = TikSaveBackground,
    onBackground = TextPrimary,
    surface = TikSaveSurface,
    onSurface = TextPrimary,
    surfaceVariant = TikSaveSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = TikSaveCardBorder,
    outlineVariant = Color(0x33A855F7),
    error = ErrorRed,
    onError = Color.White
)

private val TikSaveLightColorScheme = lightColorScheme(
    primary = NeonPurpleDark,
    onPrimary = Color.White,
    secondary = VividMagentaDark,
    onSecondary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    error = ErrorRed
)

@Composable
fun TikSaveProTheme(
    darkTheme: Boolean = true, // Default to stunning premium dark theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) TikSaveDarkColorScheme else TikSaveLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias for template references
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    TikSaveProTheme(darkTheme = darkTheme, content = content)
}
