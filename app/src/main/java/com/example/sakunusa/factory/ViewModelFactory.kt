package com.example.sakunusa.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sakunusa.data.AccountRepository
import com.example.sakunusa.data.Injection
import com.example.sakunusa.data.RecordRepository
import com.example.sakunusa.ui.records.RecordsViewModel
import com.example.sakunusa.ui.analytics.AnalyticsViewModel
import com.example.sakunusa.ui.home.HomeViewModel
import com.example.sakunusa.ui.newaccount.NewAccountViewModel
import com.example.sakunusa.ui.newrecord.NewRecordViewModel
import com.example.sakunusa.ui.statistics.StatisticsViewModel


class ViewModelFactory private constructor(private val recordRepository: RecordRepository
,private val accountRepository: AccountRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(NewRecordViewModel::class.java)
            -> NewRecordViewModel(recordRepository, accountRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java)
            -> HomeViewModel(recordRepository, accountRepository) as T
            modelClass.isAssignableFrom(StatisticsViewModel::class.java)
            -> StatisticsViewModel(recordRepository) as T
            modelClass.isAssignableFrom(RecordsViewModel::class.java)
            -> RecordsViewModel(recordRepository) as T
            modelClass.isAssignableFrom(NewAccountViewModel::class.java)
            -> NewAccountViewModel(accountRepository) as T
            modelClass.isAssignableFrom(AnalyticsViewModel::class.java)
            -> AnalyticsViewModel(recordRepository, accountRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRecordRepository(context),
                    Injection.provideAccountRepository(context)
                )
            }.also { instance = it }
    }
}