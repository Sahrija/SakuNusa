package com.example.sakunusa.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sakunusa.data.local.entity.AccountEntity
import com.example.sakunusa.data.local.entity.AnomalyEntity
import com.example.sakunusa.data.local.entity.RecordEntity
import com.example.sakunusa.utils.DateConverter

@Database(entities = [RecordEntity::class, AccountEntity::class, AnomalyEntity::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
    abstract fun accountDao(): AccountDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context, AppDatabase::class.java, "event_db"
                ).build()
            }
    }
}
