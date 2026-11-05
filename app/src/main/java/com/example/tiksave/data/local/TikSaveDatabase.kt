package com.example.tiksave.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tiksave.data.model.DownloadItem

@Database(entities = [DownloadItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TikSaveDatabase : RoomDatabase() {
    abstract fun downloadDao(): DownloadDao

    companion object {
        @Volatile
        private var INSTANCE: TikSaveDatabase? = null

        fun getDatabase(context: Context): TikSaveDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TikSaveDatabase::class.java,
                    "tiksave_pro_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
