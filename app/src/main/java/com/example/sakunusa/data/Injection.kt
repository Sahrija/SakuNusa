package com.example.sakunusa.data

import android.content.Context
import com.example.sakunusa.data.local.room.AppDatabase
import com.example.sakunusa.data.remote.retrofit.ApiConfig
import com.example.sakunusa.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): RecordRepository {
        val apiService = ApiConfig.getApiService()
        val database: AppDatabase = AppDatabase.getInstance(context)
        val dao = database.recordDao()
        val appExecutors = AppExecutors()
        return RecordRepository.getInstance(apiService, dao, appExecutors)
    }
}