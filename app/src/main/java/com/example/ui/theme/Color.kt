package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// TikSave Pro Neon Theme Colors
val TikSaveBackground = Color(0xFF0A0713)
val TikSaveSurface = Color(0xFF140E24)
val TikSaveSurfaceVariant = Color(0xFF1E1636)
val TikSaveCardBorder = Color(0xFF322554)

val NeonPurple = Color(0xFFA855F7)
val NeonPurpleDark = Color(0xFF7E22CE)
val NeonPurpleGlow = Color(0xFFC084FC)

val VividMagenta = Color(0xFFEC4899)
val VividMagentaDark = Color(0xFFBE185D)
val VividMagentaGlow = Color(0xFFF472B6)

val ElectricCyan = Color(0xFF06B6D4)
val SuccessGreen = Color(0xFF10B981)
val WarningAmber = Color(0xFFF59E0B)
val ErrorRed = Color(0xFFEF4444)

val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

// Gradients
val PrimaryGradient = Brush.horizontalGradient(
    colors = listOf(NeonPurple, VividMagenta)
)

val GlowGradient = Brush.linearGradient(
    colors = listOf(Color(0x33A855F7), Color(0x33EC4899))
)

val CyberCardGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF1F1638), Color(0xFF130E24))
)

val ProGoldGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFF59E0B), Color(0xFFFCD34D))
)
