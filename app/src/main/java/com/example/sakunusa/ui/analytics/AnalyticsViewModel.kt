package com.example.sakunusa.ui.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sakunusa.data.AccountRepository
import com.example.sakunusa.data.RecordRepository
import com.example.sakunusa.data.Result
import com.example.sakunusa.data.local.entity.AnomalyEntity
import com.example.sakunusa.data.local.entity.AnomalyWithRecord
import com.example.sakunusa.data.local.entity.RecordEntity
import kotlinx.coroutines.launch

class AnalyticsViewModel(
    private val recordRepository: RecordRepository,
    private val accountRepository: AccountRepository,
) : ViewModel() {

    fun anomalies(): LiveData<Result<List<AnomalyWithRecord>>> = recordRepository.getAnomalies()
    fun deleteAnomaly(anomaly: AnomalyEntity) {
        viewModelScope.launch {
            recordRepository.deleteAnomaly(anomaly)
        }
    }

    fun toggleAnomaly(anomaly: AnomalyEntity) {
        viewModelScope.launch {
            recordRepository.toggleAnomaly(anomaly)
        }
    }

}
