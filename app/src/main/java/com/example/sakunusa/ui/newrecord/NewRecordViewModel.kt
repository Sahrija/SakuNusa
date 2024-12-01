package com.example.sakunusa.ui.newrecord

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sakunusa.data.RecordRepository
import com.example.sakunusa.data.local.entity.RecordEntity

class NewRecordViewModel(val repository: RecordRepository) : ViewModel() {
    private val _selectedDate = MutableLiveData<Long>()
    val selectedDate: LiveData<Long> get() = _selectedDate
    fun setSelectedDate(dateLong: Long){_selectedDate.value = dateLong}



    private val _amount = MutableLiveData<Int>()
    val amount: LiveData<Int> get() = _amount

    init {

    }


    fun addRecord(recordEntity: RecordEntity) {
        repository.storeRecord(recordEntity)
    }

    fun getRecord(recordId: Int) {

    }

    fun getCurrentTime(): Long {
        return System.currentTimeMillis()
    }
}