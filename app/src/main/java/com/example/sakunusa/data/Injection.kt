package com.example.sakunusa.data

import android.content.Context
import com.example.sakunusa.data.local.room.AppDatabase
import com.example.sakunusa.data.remote.retrofit.ApiConfig
import com.example.sakunusa.utils.AppExecutors

object Injection {
    fun provideRecordRepository(context: Context): RecordRepository {
        val apiService = ApiConfig.getApiService()
        val database: AppDatabase = AppDatabase.getInstance(context)
        val recordDao = database.recordDao()
        val accountDao = database.accountDao()
        val appExecutors = AppExecutors()
        return RecordRepository.getInstance(apiService, recordDao, accountDao, appExecutors)
    }

    fun provideAccountRepository(context: Context): AccountRepository {
        val apiService = ApiConfig.getApiService()
        val database: AppDatabase = AppDatabase.getInstance(context)
        val dao = database.accountDao()
        val appExecutors = AppExecutors()
        return AccountRepository.getInstance(apiService, dao, appExecutors)
    }
}