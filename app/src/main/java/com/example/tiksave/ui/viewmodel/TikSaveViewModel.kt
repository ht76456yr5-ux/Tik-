package com.example.tiksave.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiksave.data.local.TikSaveDatabase
import com.example.tiksave.data.model.DownloadItem
import com.example.tiksave.data.model.MediaStreamInfo
import com.example.tiksave.data.model.PlatformType
import com.example.tiksave.data.model.StreamFormatOption
import com.example.tiksave.data.network.PlatformExtractor
import com.example.tiksave.data.network.TikSaveDownloadManager
import com.example.tiksave.data.preferences.AppAccentTheme
import com.example.tiksave.data.preferences.PreferenceManager
import com.example.tiksave.data.repository.TikSaveRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Downloads : Screen("downloads", "Downloads")
    object History : Screen("history", "History")
    object Favorites : Screen("favorites", "Favorites")
    object Pro : Screen("pro", "TikSave Pro")
    object Settings : Screen("settings", "Settings")
    object WhatsAppStatus : Screen("whatsapp", "Status Saver")
    object AboutUs : Screen("about", "About Us")
    object PrivacyPolicy : Screen("privacy", "Privacy Policy")
    object ContactUs : Screen("contact", "Contact Us")
}

class TikSaveViewModel(application: Application) : AndroidViewModel(application) {

    private val database = TikSaveDatabase.getDatabase(application)
    private val downloadManager = TikSaveDownloadManager(application, database.downloadDao())
    private val platformExtractor = PlatformExtractor()
    val preferenceManager = PreferenceManager(application)

    val repository = TikSaveRepository(
        downloadDao = database.downloadDao(),
        platformExtractor = platformExtractor,
        downloadManager = downloadManager,
        preferenceManager = preferenceManager
    )

    // Navigation state
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Home input & extraction states
    private val _urlInput = MutableStateFlow("")
    val urlInput: StateFlow<String> = _urlInput.asStateFlow()

    private val _detectedPlatform = MutableStateFlow(PlatformType.UNKNOWN)
    val detectedPlatform: StateFlow<PlatformType> = _detectedPlatform.asStateFlow()

    private val _isResolving = MutableStateFlow(false)
    val isResolving: StateFlow<Boolean> = _isResolving.asStateFlow()

    private val _resolvedMedia = MutableStateFlow<MediaStreamInfo?>(null)
    val resolvedMedia: StateFlow<MediaStreamInfo?> = _resolvedMedia.asStateFlow()

    private val _selectedFormat = MutableStateFlow<StreamFormatOption?>(null)
    val selectedFormat: StateFlow<StreamFormatOption?> = _selectedFormat.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // History filter & search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterPlatform = MutableStateFlow<PlatformType?>(null)
    val filterPlatform: StateFlow<PlatformType?> = _filterPlatform.asStateFlow()

    // Video preview dialog
    private val _previewVideoPath = MutableStateFlow<String?>(null)
    val previewVideoPath: StateFlow<String?> = _previewVideoPath.asStateFlow()

    private val _previewVideoTitle = MutableStateFlow<String?>(null)
    val previewVideoTitle: StateFlow<String?> = _previewVideoTitle.asStateFlow()

    // Batch download modal
    private val _showBatchDialog = MutableStateFlow(false)
    val showBatchDialog: StateFlow<Boolean> = _showBatchDialog.asStateFlow()

    // Database flows
    val allDownloads = repository.allDownloads.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val recentDownloads = repository.recentDownloads.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val activeDownloads = repository.activeDownloads.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val favoriteDownloads = repository.favoriteDownloads.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val completedDownloads = repository.completedDownloads.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Filtered history
    val filteredHistory = combine(allDownloads, _searchQuery, _filterPlatform) { list, query, filter ->
        list.filter { item ->
            val matchesQuery = query.isBlank() || item.title.contains(query, ignoreCase = true) || item.author.contains(query, ignoreCase = true)
            val matchesPlatform = filter == null || item.platform == filter
            matchesQuery && matchesPlatform
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isProUser = preferenceManager.isProUser
    val proTierPlan = preferenceManager.proTierPlan
    val isDarkMode = preferenceManager.isDarkMode
    val accentTheme = preferenceManager.accentTheme
    val selectedLanguage = preferenceManager.selectedLanguage
    val autoClearHistory = preferenceManager.autoClearHistory

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun onUrlChanged(newUrl: String) {
        _urlInput.value = newUrl
        _detectedPlatform.value = PlatformType.detectFromUrl(newUrl)
        _errorMessage.value = null
        if (newUrl.isBlank()) {
            _resolvedMedia.value = null
            _selectedFormat.value = null
        }
    }

    fun onClearUrl() {
        _urlInput.value = ""
        _detectedPlatform.value = PlatformType.UNKNOWN
        _resolvedMedia.value = null
        _selectedFormat.value = null
        _errorMessage.value = null
    }

    fun loadSampleUrl(platform: PlatformType) {
        val sample = when (platform) {
            PlatformType.TIKTOK -> PlatformExtractor.SAMPLE_TIKTOK_URL
            PlatformType.INSTAGRAM -> PlatformExtractor.SAMPLE_INSTAGRAM_URL
            PlatformType.FACEBOOK -> PlatformExtractor.SAMPLE_FACEBOOK_URL
            PlatformType.YOUTUBE -> PlatformExtractor.SAMPLE_YOUTUBE_URL
            PlatformType.PINTEREST -> PlatformExtractor.SAMPLE_PINTEREST_URL
            PlatformType.WHATSAPP -> "https://whatsapp.com/status/sample"
            PlatformType.UNKNOWN -> PlatformExtractor.SAMPLE_DIRECT_MP4_URL
        }
        onUrlChanged(sample)
        fetchMediaInfo()
    }

    fun fetchMediaInfo() {
        val url = _urlInput.value.trim()
        if (url.isBlank()) {
            _errorMessage.value = "Please paste or enter a video link first"
            return
        }

        viewModelScope.launch {
            _isResolving.value = true
            _errorMessage.value = null
            _resolvedMedia.value = null

            val result = repository.resolveUrl(url)
            _isResolving.value = false

            result.onSuccess { info ->
                _resolvedMedia.value = info
                _selectedFormat.value = info.availableFormats.firstOrNull()
            }.onFailure { err ->
                _errorMessage.value = err.message ?: "Failed to resolve link. Please check the URL."
            }
        }
    }

    fun selectFormat(format: StreamFormatOption) {
        _selectedFormat.value = format
    }

    fun startDownload(format: StreamFormatOption? = null) {
        val media = _resolvedMedia.value ?: return
        val chosenFormat = format ?: _selectedFormat.value ?: media.availableFormats.firstOrNull() ?: return

        if (chosenFormat.isProRequired && !isProUser.value) {
            _errorMessage.value = "This format requires TikSave Pro. Tap the Pro tab to unlock!"
            return
        }

        viewModelScope.launch {
            try {
                repository.startDownload(
                    streamInfo = media,
                    formatOption = chosenFormat,
                    coroutineScope = viewModelScope
                )
                showToast("Download started: ${media.title.take(20)}...")
                // Optionally navigate to downloads screen to monitor progress
                _currentScreen.value = Screen.Downloads
            } catch (e: Exception) {
                _errorMessage.value = "Could not start download: ${e.message}"
            }
        }
    }

    fun cancelDownload(id: Long) {
        repository.cancelDownload(id)
        showToast("Download cancelled")
    }

    fun toggleFavorite(item: DownloadItem) {
        viewModelScope.launch {
            repository.toggleFavorite(item.id, !item.isFavorite)
            showToast(if (!item.isFavorite) "Added to Favorites" else "Removed from Favorites")
        }
    }

    fun deleteDownload(item: DownloadItem) {
        viewModelScope.launch {
            repository.deleteDownload(item.id, item.localFilePath)
            showToast("Item deleted")
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            showToast("Download history cleared")
        }
    }

    fun setFilterPlatform(platform: PlatformType?) {
        _filterPlatform.value = platform
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openVideoPreview(filePath: String, title: String) {
        _previewVideoPath.value = filePath
        _previewVideoTitle.value = title
    }

    fun closeVideoPreview() {
        _previewVideoPath.value = null
        _previewVideoTitle.value = null
    }

    fun openBatchDialog() {
        if (!isProUser.value) {
            showToast("Batch downloads is a Pro feature! Upgrade to unlock.")
            _currentScreen.value = Screen.Pro
        } else {
            _showBatchDialog.value = true
        }
    }

    fun closeBatchDialog() {
        _showBatchDialog.value = false
    }

    fun executeBatchDownload(urls: List<String>) {
        _showBatchDialog.value = false
        viewModelScope.launch {
            var count = 0
            urls.filter { it.isNotBlank() }.forEach { url ->
                val res = repository.resolveUrl(url)
                res.onSuccess { info ->
                    info.availableFormats.firstOrNull()?.let { fmt ->
                        repository.startDownload(info, fmt, viewModelScope)
                        count++
                    }
                }
            }
            showToast("Queued $count downloads in batch mode")
            _currentScreen.value = Screen.Downloads
        }
    }

    fun upgradeToPro(plan: String = "Pro Yearly VIP ($29.99/yr)") {
        preferenceManager.setProUser(true, plan)
        showToast("🎉 Welcome to TikSave Pro! All features unlocked.")
    }

    fun cancelProSubscription() {
        preferenceManager.setProUser(false, "None")
        showToast("Pro subscription deactivated")
    }

    fun setDarkMode(enabled: Boolean) {
        preferenceManager.setDarkMode(enabled)
    }

    fun setAccentTheme(theme: AppAccentTheme) {
        preferenceManager.setAccentTheme(theme)
    }

    fun setLanguage(lang: String) {
        preferenceManager.setSelectedLanguage(lang)
        showToast("Language set to $lang")
    }

    fun setAutoClear(enabled: Boolean) {
        preferenceManager.setAutoClearHistory(enabled)
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
