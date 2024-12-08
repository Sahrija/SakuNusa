package com.example.sakunusa.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.sakunusa.data.local.entity.AccountEntity


@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    fun getAccounts(): LiveData<List<AccountEntity>>

}