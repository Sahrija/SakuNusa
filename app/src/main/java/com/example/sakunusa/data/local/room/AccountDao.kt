package com.example.sakunusa.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Transaction
import androidx.room.Update
import com.example.sakunusa.data.local.entity.AccountEntity
import com.example.sakunusa.data.local.entity.AccountWithRecords


@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    fun getAccounts(): LiveData<List<AccountEntity>>

    @Insert
    suspend fun newAccount(account: AccountEntity)

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Query("UPDATE account SET isSelected = :isSelected WHERE id = :accountId")
    suspend fun setAccountSelected(accountId: Int, isSelected: Boolean)

    @Query("SELECT * FROM account WHERE id = :accountId")
    fun getAccountById(accountId: Int): LiveData<AccountEntity>

    @Query("SELECT * FROM account WHERE id = :accountId")
    suspend fun getAccountByIdDirect(accountId: Int): AccountEntity
}