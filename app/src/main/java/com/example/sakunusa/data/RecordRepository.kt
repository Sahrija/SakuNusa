package com.example.sakunusa.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.sakunusa.data.local.entity.AccountEntity
import com.example.sakunusa.data.local.room.RecordDao
import com.example.sakunusa.data.remote.response.RecordItem
import com.example.sakunusa.data.remote.response.RecordsResponse
import com.example.sakunusa.data.remote.retrofit.ApiService
import com.example.sakunusa.utils.AppExecutors
import com.example.sakunusa.data.local.entity.RecordEntity
import com.example.sakunusa.data.local.room.AccountDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecordRepository private constructor(
    private val apiService: ApiService,
    private val recordDao: RecordDao,
    private val accountDao: AccountDao,
    private val appExecutors: AppExecutors
) {
    private val mediatorLiveDataResult = MediatorLiveData<Result<List<RecordEntity>>>()

    fun fetchAllRecords(): LiveData<Result<List<RecordEntity>>> {
        mediatorLiveDataResult.value = Result.Loading
        val client = apiService.getRecords()

        client.enqueue(object : Callback<RecordsResponse> {
            override fun onResponse(
                call: Call<RecordsResponse>,
                response: Response<RecordsResponse>
            ) {
                if (response.isSuccessful) {
                    val recordsResponse: List<RecordItem?>? = response.body()?.data


                    val records: List<RecordEntity> =
                        recordsResponse?.mapNotNull { recordItem ->
                            recordItem?.let { RecordEntity(it) }
                        } ?: emptyList()

                    mediatorLiveDataResult.postValue(Result.Success(records))
                }
            }

            override fun onFailure(call: Call<RecordsResponse>, t: Throwable) {
                mediatorLiveDataResult.value = Result.Error(t.message.toString())
            }
        })

        return mediatorLiveDataResult
    }

    fun getRecords(
        orderBy: String? = "desc"
    ): LiveData<Result<List<RecordEntity>>> {
        val source = if (orderBy == "asc") {
            recordDao.getRecordsForSelectedAccountsAsc()
        } else {
            recordDao.getRecordsForSelectedAccountsDesc()
        }

        mediatorLiveDataResult.addSource(source) {
            mediatorLiveDataResult.value = Result.Success(it)
        }

        return mediatorLiveDataResult
    }

    suspend fun getRecordById(recordId: Int): RecordEntity? {
        return recordDao.getRecordById(recordId)
    }

    suspend fun storeRecord(record: RecordEntity) {
        recordDao.insertRecord(record)

        val account = accountDao.getAccountByIdDirect(record.accountId)
        val newAccount: AccountEntity =
            account.copy(startingAmount = account.startingAmount + record.amount)
        accountDao.updateAccount(newAccount)
    }

    suspend fun updateRecord(record: RecordEntity) {
        recordDao.updateRecord(record)
    }

    suspend fun deleteRecord(recordId: Int): Boolean {
        return recordDao.deleteRecordById(recordId) > 0
    }

    companion object {
        @Volatile
        private var instance: RecordRepository? = null
        fun getInstance(
            apiService: ApiService,
            recordDao: RecordDao,
            accountDao: AccountDao,
            appExecutors: AppExecutors
        ): RecordRepository =
            instance ?: synchronized(this) {
                instance ?: RecordRepository(apiService, recordDao, accountDao, appExecutors)
            }.also { instance = it }
    }
}