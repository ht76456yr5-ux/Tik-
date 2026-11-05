package com.example.tiksave.data.local

import androidx.room.TypeConverter
import com.example.tiksave.data.model.DownloadStatus
import com.example.tiksave.data.model.MediaFormatType
import com.example.tiksave.data.model.PlatformType

class Converters {
    @TypeConverter
    fun fromPlatform(platform: PlatformType): String = platform.name

    @TypeConverter
    fun toPlatform(value: String): PlatformType =
        try { PlatformType.valueOf(value) } catch (e: Exception) { PlatformType.UNKNOWN }

    @TypeConverter
    fun fromFormat(format: MediaFormatType): String = format.name

    @TypeConverter
    fun toFormat(value: String): MediaFormatType =
        try { MediaFormatType.valueOf(value) } catch (e: Exception) { MediaFormatType.VIDEO_HD }

    @TypeConverter
    fun fromStatus(status: DownloadStatus): String = status.name

    @TypeConverter
    fun toStatus(value: String): DownloadStatus =
        try { DownloadStatus.valueOf(value) } catch (e: Exception) { DownloadStatus.PENDING }
}
