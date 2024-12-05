package com.example.sakunusa.ui.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sakunusa.data.RecordRepository
import com.example.sakunusa.data.Result
import com.example.sakunusa.data.local.entity.RecordEntity

class StatisticsViewModel(private val repository: RecordRepository) : ViewModel() {
    private val _records = MediatorLiveData<Result<List<RecordEntity>>>().apply {
        value = Result.Loading
        addSource(repository.getRecords("desc")) { result ->
            value = result
        }
    }
    val records: LiveData<Result<List<RecordEntity>>> get() = _records

    override fun onCleared() {
        super.onCleared()
        repository.getRecords("desc").removeObserver { }
    }
}