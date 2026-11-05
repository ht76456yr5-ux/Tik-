package com.example.tiksave.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tiksave.data.model.DownloadItem
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {
    @Query("SELECT * FROM downloads ORDER BY timestamp DESC")
    fun getAllDownloads(): Flow<List<DownloadItem>>

    @Query("SELECT * FROM downloads ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentDownloads(limit: Int = 5): Flow<List<DownloadItem>>

    @Query("SELECT * FROM downloads WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavorites(): Flow<List<DownloadItem>>

    @Query("SELECT * FROM downloads WHERE status = 'COMPLETED' ORDER BY timestamp DESC")
    fun getCompletedDownloads(): Flow<List<DownloadItem>>

    @Query("SELECT * FROM downloads WHERE status IN ('DOWNLOADING', 'PENDING', 'EXTRACTING') ORDER BY timestamp DESC")
    fun getActiveDownloads(): Flow<List<DownloadItem>>

    @Query("SELECT * FROM downloads WHERE id = :id")
    suspend fun getDownloadById(id: Long): DownloadItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(item: DownloadItem): Long

    @Update
    suspend fun updateDownload(item: DownloadItem)

    @Delete
    suspend fun deleteDownload(item: DownloadItem)

    @Query("DELETE FROM downloads WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM downloads")
    suspend fun clearAll()

    @Query("UPDATE downloads SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Query("UPDATE downloads SET status = :status, progress = :progress, speedText = :speed, downloadedBytes = :bytes WHERE id = :id")
    suspend fun updateProgress(id: Long, status: com.example.tiksave.data.model.DownloadStatus, progress: Int, speed: String, bytes: Long)
}
