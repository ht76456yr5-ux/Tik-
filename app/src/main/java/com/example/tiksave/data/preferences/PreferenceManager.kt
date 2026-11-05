package com.example.tiksave.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppAccentTheme(val title: String, val primaryHex: Long, val secondaryHex: Long) {
    NEON_PURPLE("Cyber Purple", 0xFFA855F7, 0xFFEC4899),
    VIVID_MAGENTA("Sunset Magenta", 0xFFEC4899, 0xFF8B5CF6),
    ELECTRIC_CYAN("Matrix Cyan", 0xFF06B6D4, 0xFFA855F7)
}

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("tiksave_pro_prefs", Context.MODE_PRIVATE)

    private val _isProUser = MutableStateFlow(prefs.getBoolean("is_pro_user", false))
    val isProUser: StateFlow<Boolean> = _isProUser.asStateFlow()

    private val _proTierPlan = MutableStateFlow(prefs.getString("pro_tier_plan", "None") ?: "None")
    val proTierPlan: StateFlow<String> = _proTierPlan.asStateFlow()

    private val _isDarkMode = MutableStateFlow(prefs.getBoolean("dark_mode", true))
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _accentTheme = MutableStateFlow(
        try {
            AppAccentTheme.valueOf(prefs.getString("accent_theme", AppAccentTheme.NEON_PURPLE.name) ?: AppAccentTheme.NEON_PURPLE.name)
        } catch (e: Exception) {
            AppAccentTheme.NEON_PURPLE
        }
    )
    val accentTheme: StateFlow<AppAccentTheme> = _accentTheme.asStateFlow()

    private val _selectedLanguage = MutableStateFlow(prefs.getString("app_language", "English") ?: "English")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    private val _autoClearHistory = MutableStateFlow(prefs.getBoolean("auto_clear_history", false))
    val autoClearHistory: StateFlow<Boolean> = _autoClearHistory.asStateFlow()

    private val _defaultQuality = MutableStateFlow(prefs.getString("default_quality", "HD (1080p)") ?: "HD (1080p)")
    val defaultQuality: StateFlow<String> = _defaultQuality.asStateFlow()

    fun setProUser(isPro: Boolean, plan: String = "Pro Yearly VIP") {
        prefs.edit()
            .putBoolean("is_pro_user", isPro)
            .putString("pro_tier_plan", if (isPro) plan else "None")
            .apply()
        _isProUser.value = isPro
        _proTierPlan.value = if (isPro) plan else "None"
    }

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean("dark_mode", enabled).apply()
        _isDarkMode.value = enabled
    }

    fun setAccentTheme(theme: AppAccentTheme) {
        prefs.edit().putString("accent_theme", theme.name).apply()
        _accentTheme.value = theme
    }

    fun setSelectedLanguage(lang: String) {
        prefs.edit().putString("app_language", lang).apply()
        _selectedLanguage.value = lang
    }

    fun setAutoClearHistory(enabled: Boolean) {
        prefs.edit().putBoolean("auto_clear_history", enabled).apply()
        _autoClearHistory.value = enabled
    }

    fun setDefaultQuality(quality: String) {
        prefs.edit().putString("default_quality", quality).apply()
        _defaultQuality.value = quality
    }
}
