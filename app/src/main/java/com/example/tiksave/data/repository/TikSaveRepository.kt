package com.example.tiksave.data.repository

import com.example.tiksave.data.local.DownloadDao
import com.example.tiksave.data.model.DownloadItem
import com.example.tiksave.data.model.MediaFormatType
import com.example.tiksave.data.model.MediaStreamInfo
import com.example.tiksave.data.model.StreamFormatOption
import com.example.tiksave.data.network.PlatformExtractor
import com.example.tiksave.data.network.TikSaveDownloadManager
import com.example.tiksave.data.preferences.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.File

class TikSaveRepository(
    private val downloadDao: DownloadDao,
    private val platformExtractor: PlatformExtractor,
    private val downloadManager: TikSaveDownloadManager,
    val preferenceManager: PreferenceManager
) {
    val allDownloads: Flow<List<DownloadItem>> = downloadDao.getAllDownloads()
    val recentDownloads: Flow<List<DownloadItem>> = downloadDao.getRecentDownloads(6)
    val favoriteDownloads: Flow<List<DownloadItem>> = downloadDao.getFavorites()
    val completedDownloads: Flow<List<DownloadItem>> = downloadDao.getCompletedDownloads()
    val activeDownloads: Flow<List<DownloadItem>> = downloadDao.getActiveDownloads()

    suspend fun resolveUrl(url: String): Result<MediaStreamInfo> {
        return platformExtractor.resolveMedia(url)
    }

    suspend fun startDownload(
        streamInfo: MediaStreamInfo,
        formatOption: StreamFormatOption,
        coroutineScope: CoroutineScope
    ): Long {
        val isPro = preferenceManager.isProUser.value
        val initialItem = DownloadItem(
            title = streamInfo.title,
            originalUrl = streamInfo.originalUrl,
            downloadUrl = formatOption.downloadUrl,
            platform = streamInfo.platform,
            formatType = formatOption.formatType,
            thumbnailUrl = streamInfo.thumbnailUrl,
            quality = formatOption.resolution,
            author = streamInfo.author,
            isPro = isPro
        )

        val id = downloadDao.insertDownload(initialItem)
        val createdItem = initialItem.copy(id = id)

        val job = coroutineScope.launch(Dispatchers.IO) {
            downloadManager.executeDownload(
                item = createdItem,
                isProSpeed = isPro
            )
        }
        downloadManager.registerJob(id, job)
        return id
    }

    fun cancelDownload(downloadId: Long) {
        downloadManager.cancelDownload(downloadId)
    }

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        downloadDao.setFavorite(id, isFavorite)
    }

    suspend fun deleteDownload(id: Long, filePath: String?) {
        if (!filePath.isNullOrBlank()) {
            try {
                val file = File(filePath)
                if (file.exists()) file.delete()
            } catch (_: Exception) {}
        }
        downloadDao.deleteById(id)
    }

    suspend fun clearHistory() {
        downloadDao.clearAll()
    }
}
