package com.example.sakunusa.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sakunusa.data.AccountRepository
import com.example.sakunusa.data.RecordRepository
import com.example.sakunusa.data.Result
import com.example.sakunusa.data.local.entity.AccountEntity
import com.example.sakunusa.data.local.entity.RecordEntity
import kotlinx.coroutines.launch

class HomeViewModel(
    private val recordRepository: RecordRepository,
    private val accountRepository: AccountRepository,
) : ViewModel() {

    private val _records = MediatorLiveData<Result<List<RecordEntity>>>().apply {
        value = Result.Loading
        addSource(recordRepository.getRecords("desc")) { result ->
            value = result
        }
    }
    val records: LiveData<Result<List<RecordEntity>>> get() = _records

    fun fetchRecords() {
        _records.postValue(recordRepository.getRecords("desc").value)
    }

    override fun onCleared() {
        super.onCleared()
        recordRepository.getRecords("desc").removeObserver { }
    }

    fun getAccounts(): LiveData<Result<List<AccountEntity>>> = accountRepository.getAccounts()

    fun toggleAccountToSelected(account: AccountEntity) {
        viewModelScope.launch {
            accountRepository.toggleAccountToSelected(account)
        }
    }
}
