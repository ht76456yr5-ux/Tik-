package com.example.tiksave.data.network

import android.content.Context
import android.os.Environment
import com.example.tiksave.data.local.DownloadDao
import com.example.tiksave.data.model.DownloadItem
import com.example.tiksave.data.model.DownloadStatus
import com.example.tiksave.data.model.MediaFormatType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.DecimalFormat
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class TikSaveDownloadManager(
    private val context: Context,
    private val downloadDao: DownloadDao,
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()
) {

    private val activeJobs = ConcurrentHashMap<Long, Job>()

    fun registerJob(downloadId: Long, job: Job) {
        activeJobs[downloadId] = job
    }

    fun cancelDownload(downloadId: Long) {
        activeJobs[downloadId]?.cancel()
        activeJobs.remove(downloadId)
    }

    suspend fun executeDownload(
        item: DownloadItem,
        isProSpeed: Boolean = false,
        onProgressUpdate: (progress: Int, speed: String, downloadedBytes: Long) -> Unit = { _, _, _ -> }
    ): Result<File> = withContext(Dispatchers.IO) {
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null
        var targetFile: File? = null

        try {
            // Determine storage directory
            val baseDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                ?: File(context.filesDir, "downloads")
            if (!baseDir.exists()) {
                baseDir.mkdirs()
            }

            // Create clean sanitized filename
            val extension = if (item.formatType == MediaFormatType.AUDIO_MP3) ".mp3" else ".mp4"
            val sanitizedTitle = item.title.replace(Regex("[^a-zA-Z0-9._-]"), "_").take(30)
            val fileName = "TikSave_${item.platform.name.lowercase()}_${sanitizedTitle}_${System.currentTimeMillis()}$extension"
            targetFile = File(baseDir, fileName)

            // Update status to DOWNLOADING
            downloadDao.updateProgress(
                id = item.id,
                status = DownloadStatus.DOWNLOADING,
                progress = 1,
                speed = if (isProSpeed) "Pro Ultra Speed" else "Connecting...",
                bytes = 0L
            )

            val request = Request.Builder()
                .url(item.downloadUrl)
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 TikSavePro/2.4")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                throw IllegalStateException("Server returned HTTP ${response.code}")
            }

            val body = response.body ?: throw IllegalStateException("Empty response body from server")
            val totalBytes = body.contentLength().takeIf { it > 0 } ?: (item.fileSize.takeIf { it > 0 } ?: 12_000_000L)

            inputStream = body.byteStream()
            outputStream = FileOutputStream(targetFile)

            val buffer = ByteArray(if (isProSpeed) 16384 else 8192)
            var bytesRead: Int
            var totalRead = 0L
            var lastUpdateTime = System.currentTimeMillis()
            var bytesSinceLastUpdate = 0L
            val df = DecimalFormat("#.1")

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
                totalRead += bytesRead
                bytesSinceLastUpdate += bytesRead

                val now = System.currentTimeMillis()
                val elapsed = now - lastUpdateTime
                if (elapsed >= 350 || totalRead == totalBytes) {
                    val progress = ((totalRead * 100) / totalBytes).toInt().coerceIn(0, 100)
                    val speedBytesPerSec = if (elapsed > 0) (bytesSinceLastUpdate * 1000) / elapsed else 0L
                    val speedText = if (speedBytesPerSec > 1024 * 1024) {
                        "${df.format(speedBytesPerSec / (1024.0 * 1024.0))} MB/s"
                    } else {
                        "${speedBytesPerSec / 1024} KB/s"
                    }

                    downloadDao.updateProgress(
                        id = item.id,
                        status = DownloadStatus.DOWNLOADING,
                        progress = progress,
                        speed = speedText,
                        bytes = totalRead
                    )
                    onProgressUpdate(progress, speedText, totalRead)

                    lastUpdateTime = now
                    bytesSinceLastUpdate = 0L
                }
            }

            outputStream.flush()

            // Mark completed in database
            val updated = item.copy(
                status = DownloadStatus.COMPLETED,
                progress = 100,
                speedText = "Completed",
                downloadedBytes = totalRead,
                fileSize = totalRead,
                localFilePath = targetFile.absolutePath,
                errorMessage = null
            )
            downloadDao.updateDownload(updated)

            activeJobs.remove(item.id)
            Result.success(targetFile)
        } catch (e: CancellationException) {
            targetFile?.delete()
            downloadDao.updateProgress(
                id = item.id,
                status = DownloadStatus.PAUSED,
                progress = 0,
                speed = "Cancelled",
                bytes = 0L
            )
            activeJobs.remove(item.id)
            Result.failure(e)
        } catch (e: Exception) {
            targetFile?.delete()
            val failed = item.copy(
                status = DownloadStatus.FAILED,
                speedText = "Failed",
                errorMessage = e.message ?: "Download connection error"
            )
            downloadDao.updateDownload(failed)
            activeJobs.remove(item.id)
            Result.failure(e)
        } finally {
            try { inputStream?.close() } catch (_: Exception) {}
            try { outputStream?.close() } catch (_: Exception) {}
        }
    }
}
