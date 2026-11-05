package com.example.tiksave.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloads")
data class DownloadItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val originalUrl: String,
    val downloadUrl: String,
    val platform: PlatformType,
    val formatType: MediaFormatType = MediaFormatType.VIDEO_HD,
    val thumbnailUrl: String = "",
    val localFilePath: String = "",
    val fileSize: Long = 0L,
    val quality: String = "1080p HD",
    val author: String = "",
    val status: DownloadStatus = DownloadStatus.PENDING,
    val progress: Int = 0,
    val speedText: String = "",
    val downloadedBytes: Long = 0L,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val isPro: Boolean = false,
    val errorMessage: String? = null
)

data class MediaStreamInfo(
    val title: String,
    val author: String,
    val platform: PlatformType,
    val originalUrl: String,
    val thumbnailUrl: String,
    val durationText: String = "0:30",
    val availableFormats: List<StreamFormatOption> = emptyList(),
    val isPrivateOrDrm: Boolean = false,
    val policyNotice: String? = null
)

data class StreamFormatOption(
    val formatType: MediaFormatType,
    val label: String,
    val resolution: String,
    val estimatedSize: String,
    val downloadUrl: String,
    val isProRequired: Boolean = false
)
