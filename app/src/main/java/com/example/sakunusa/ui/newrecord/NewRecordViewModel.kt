package com.example.sakunusa.ui.newrecord

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sakunusa.data.AccountRepository
import com.example.sakunusa.data.RecordRepository
import com.example.sakunusa.data.Result
import com.example.sakunusa.data.local.entity.AccountEntity
import com.example.sakunusa.data.local.entity.RecordEntity
import kotlinx.coroutines.launch
import java.util.Calendar

class NewRecordViewModel(
    private val recordRepository: RecordRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {
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


    fun addRecord(recordEntity: RecordEntity) {
        viewModelScope.launch { recordRepository.storeRecord(recordEntity) }
    }

    private val _record = MutableLiveData<RecordEntity?>()
    val record: LiveData<RecordEntity?> get() = _record

    fun fetchRecordById(recordId: Int) {
        viewModelScope.launch {
            val result = recordRepository.getRecordById(recordId)
            _record.postValue(result)
        }
    }

    fun updateRecord(record: RecordEntity) {
        viewModelScope.launch {
            recordRepository.updateRecord(record)
        }
    }

    fun deleteRecord(recordId: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = recordRepository.deleteRecord(recordId)
            onResult(success)
        }
    }

    fun getAccounts(): LiveData<Result<List<AccountEntity>>> = accountRepository.getAccounts()
}