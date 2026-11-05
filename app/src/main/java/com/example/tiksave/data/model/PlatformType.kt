package com.example.tiksave.data.model

import androidx.compose.ui.graphics.Color

enum class PlatformType(
    val displayName: String,
    val iconEmoji: String,
    val brandColor: Color,
    val secondaryColor: Color,
    val domains: List<String>
) {
    TIKTOK(
        displayName = "TikTok",
        iconEmoji = "🎵",
        brandColor = Color(0xFF00F2FE),
        secondaryColor = Color(0xFFFE0979),
        domains = listOf("tiktok.com", "vm.tiktok.com", "vt.tiktok.com")
    ),
    INSTAGRAM(
        displayName = "Instagram",
        iconEmoji = "📸",
        brandColor = Color(0xFFE1306C),
        secondaryColor = Color(0xFFF77737),
        domains = listOf("instagram.com", "instagr.am")
    ),
    FACEBOOK(
        displayName = "Facebook",
        iconEmoji = "👥",
        brandColor = Color(0xFF1877F2),
        secondaryColor = Color(0xFF4267B2),
        domains = listOf("facebook.com", "fb.watch", "fb.com", "m.facebook.com")
    ),
    YOUTUBE(
        displayName = "YouTube",
        iconEmoji = "▶️",
        brandColor = Color(0xFFFF0000),
        secondaryColor = Color(0xFF282828),
        domains = listOf("youtube.com", "youtu.be", "m.youtube.com")
    ),
    PINTEREST(
        displayName = "Pinterest",
        iconEmoji = "📌",
        brandColor = Color(0xFFE60023),
        secondaryColor = Color(0xFFBD081C),
        domains = listOf("pinterest.com", "pin.it")
    ),
    WHATSAPP(
        displayName = "WhatsApp Status",
        iconEmoji = "💬",
        brandColor = Color(0xFF25D366),
        secondaryColor = Color(0xFF128C7E),
        domains = listOf("whatsapp.com")
    ),
    UNKNOWN(
        displayName = "Web Media",
        iconEmoji = "🌐",
        brandColor = Color(0xFFA855F7),
        secondaryColor = Color(0xFFEC4899),
        domains = emptyList()
    );

    companion object {
        fun detectFromUrl(url: String): PlatformType {
            val lower = url.lowercase().trim()
            return when {
                lower.contains("tiktok.com") -> TIKTOK
                lower.contains("instagram.com") || lower.contains("instagr.am") -> INSTAGRAM
                lower.contains("facebook.com") || lower.contains("fb.watch") || lower.contains("fb.com") -> FACEBOOK
                lower.contains("youtube.com") || lower.contains("youtu.be") -> YOUTUBE
                lower.contains("pinterest.com") || lower.contains("pin.it") -> PINTEREST
                lower.contains("whatsapp") -> WHATSAPP
                else -> UNKNOWN
            }
        }
    }
}

enum class MediaFormatType {
    VIDEO_NO_WATERMARK,
    VIDEO_HD,
    VIDEO_SD,
    AUDIO_MP3,
    IMAGE_ORIGINAL
}

enum class DownloadStatus {
    PENDING,
    EXTRACTING,
    DOWNLOADING,
    PAUSED,
    COMPLETED,
    FAILED
}
