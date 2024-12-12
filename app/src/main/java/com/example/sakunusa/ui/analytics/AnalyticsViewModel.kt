package com.example.sakunusa.ui.analytics

import androidx.lifecycle.ViewModel
import com.example.sakunusa.data.AccountRepository
import com.example.sakunusa.data.RecordRepository

class AnalyticsViewModel(
    private val recordRepository: RecordRepository,
    private val accountRepository: AccountRepository,
) : ViewModel() {

}
