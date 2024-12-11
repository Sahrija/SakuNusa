package com.example.sakunusa.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.sakunusa.data.local.entity.AccountEntity
import com.example.sakunusa.data.local.room.AccountDao
import com.example.sakunusa.data.remote.retrofit.ApiService
import com.example.sakunusa.utils.AppExecutors

class AccountRepository private constructor(
    private val apiService: ApiService,
    private val accountDao: AccountDao,
    private val appExecutors: AppExecutors
) {
    private val mediatorLiveDataResult = MediatorLiveData<Result<List<AccountEntity>>>()

    fun getAccounts(): LiveData<Result<List<AccountEntity>>> {
        val source = accountDao.getAccounts()

        mediatorLiveDataResult.addSource(source) {
            mediatorLiveDataResult.value = Result.Success(it)
        }

        return mediatorLiveDataResult
    }

    fun getAccountById(accountId: Int): LiveData<AccountEntity> {
        return accountDao.getAccountById(accountId)
    }

    suspend fun newAccount(account: AccountEntity): Boolean {
        accountDao.newAccount(account)
        return true
    }

    suspend fun updateAccount(account: AccountEntity) {
        accountDao.updateAccount(account)
    }

    suspend fun toggleAccountToSelected(account: AccountEntity) {
        val isSelected = !account.isSelected
        accountDao.setAccountSelected(account.id, isSelected)
    }

    companion object {
        @Volatile
        private var instance: AccountRepository? = null
        fun getInstance(
            apiService: ApiService, accountDao: AccountDao, appExecutors: AppExecutors
        ): AccountRepository = instance ?: synchronized(this) {
            instance ?: AccountRepository(apiService, accountDao, appExecutors)
        }.also { instance = it }
    }
}