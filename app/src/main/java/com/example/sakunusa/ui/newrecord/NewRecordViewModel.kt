package com.example.sakunusa.ui.newrecord

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sakunusa.data.RecordRepository
import com.example.sakunusa.data.local.entity.RecordEntity
import kotlinx.coroutines.launch
import java.util.Calendar

class NewRecordViewModel(private val repository: RecordRepository) : ViewModel() {
    private val _selectedDate = MutableLiveData<Long>().apply {
        val calendar = Calendar.getInstance()
        val todayStartAsLong = calendar.timeInMillis
        postValue(todayStartAsLong)
    }
    val selectedDate: LiveData<Long> get() = _selectedDate
    fun setSelectedDate(dateLong: Long) {
        _selectedDate.value = dateLong
    }

    private val _amount = MutableLiveData<Int>()
    val amount: LiveData<Int> get() = _amount

    init {

    }


    fun addRecord(recordEntity: RecordEntity) {
        repository.storeRecord(recordEntity)
    }

    private val _record = MutableLiveData<RecordEntity?>()
    val record: LiveData<RecordEntity?> get() = _record

    fun fetchRecordById(recordId: Int) {
        viewModelScope.launch {
            val result = repository.getRecordById(recordId)
            _record.postValue(result)
        }
    }

    fun updateRecord(record: RecordEntity) {
        viewModelScope.launch {
            repository.updateRecord(record)
        }
    }

    fun deleteRecord(recordId: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.deleteRecord(recordId)
            onResult(success)
        }
    }
}