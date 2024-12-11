package com.example.sakunusa.ui.newaccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sakunusa.data.AccountRepository
import com.example.sakunusa.data.local.entity.AccountEntity
import kotlinx.coroutines.launch

class NewAccountViewModel(private var accountRepository: AccountRepository) : ViewModel() {
    fun newAccount(account: AccountEntity, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = accountRepository.newAccount(account)
            onResult(success)
        }
    }

    fun updateAccount(account: AccountEntity) {
        viewModelScope.launch {
            accountRepository.updateAccount(account)
        }
    }

    fun getAccountById(accountId: Int): LiveData<AccountEntity> {
        return accountRepository.getAccountById(accountId)
    }
}