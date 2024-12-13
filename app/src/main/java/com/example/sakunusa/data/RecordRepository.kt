package com.example.sakunusa.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.sakunusa.data.local.entity.AccountEntity
import com.example.sakunusa.data.local.entity.AnomalyEntity
import com.example.sakunusa.data.local.entity.AnomalyWithRecord
import com.example.sakunusa.data.local.room.RecordDao
import com.example.sakunusa.data.remote.retrofit.ApiService
import com.example.sakunusa.utils.AppExecutors
import com.example.sakunusa.data.local.entity.RecordEntity
import com.example.sakunusa.data.local.room.AccountDao
import com.example.sakunusa.data.remote.retrofit.AnomalyRequestBody

class RecordRepository private constructor(
    private val apiService: ApiService,
    private val recordDao: RecordDao,
    private val accountDao: AccountDao,
    private val appExecutors: AppExecutors
) {
    private val mediatorLiveDataResult = MediatorLiveData<Result<List<RecordEntity>>>()

    private suspend fun requestAnomaly(
        record: RecordEntity,
        recordId: Int
    ): Result<AnomalyEntity> {
        val input_amount = record.amount

        try {
            val response = apiService.detectAnomaly(AnomalyRequestBody(input_amount = input_amount))

            if (response.isSuccessful) {
                val anomalyResponse = response.body()
                Log.d("anomalyResponse.loss", anomalyResponse?.loss.toString())
                return Result.Success(
                    AnomalyEntity(
                        id = recordId,
                        date = record.dateTime,
                        recordId = recordId,
                        loss = anomalyResponse?.loss.toString().toDouble(),
                        anomalyDetected = anomalyResponse?.isAnomalyDetected ?: false
                    )
                )
            } else {
                return Result.Error("Anomaly detection failed")
            }
        } catch (e: Exception) {
            return Result.Error("Error: ${e.localizedMessage}")
        }
    }

    private suspend fun detectAnomaly(record: RecordEntity, recordId: Int) {
        val anomalyResult = requestAnomaly(record, recordId)

        when (anomalyResult) {
            is Result.Success -> {
                if (anomalyResult.data.anomalyDetected) {
                    recordDao.insertAnomaly(anomalyResult.data)
                } else {
                    recordDao.deleteAnomalyByRecordId(recordId)
                }
            }

            is Result.Error -> {}
            Result.Loading -> {}
        }
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
        val recordId = recordDao.insertRecord(record)

        val account = accountDao.getAccountByIdDirect(record.accountId)
        val updatedAccount: AccountEntity =
            account.copy(startingAmount = account.startingAmount + record.amount)
        accountDao.updateAccount(updatedAccount)

        detectAnomaly(record, recordId.toInt())

        Log.d("Account change", updatedAccount.toString())
    }

    suspend fun updateRecord(record: RecordEntity) {
        val oldRecord = recordDao.getRecordById(record.id)
        val updatedAmountInterval: Float = record.amount - (oldRecord?.amount ?: 0F)
        val account = accountDao.getAccountByIdDirect(record.accountId)
        val updatedAccount: AccountEntity =
            account.copy(startingAmount = account.startingAmount + updatedAmountInterval)

        recordDao.updateRecord(record)
        accountDao.updateAccount(updatedAccount)
        detectAnomaly(record, record.id)

    }

    suspend fun deleteRecord(recordId: Int): Boolean {
        val record = recordDao.getRecordById(recordId)
        record?.let {
            val account = accountDao.getAccountByIdDirect(it.accountId)
            val newAccount: AccountEntity =
                account.copy(startingAmount = account.startingAmount - record.amount)
            Log.d(
                "Account change",
                account.startingAmount.toString() + record.amount.toString()
            )
            accountDao.updateAccount(newAccount)
        }

        recordDao.deleteAnomalyByRecordId(recordId)

        return recordDao.deleteRecordById(recordId) > 0
    }


    private val anomalyMediatorLiveDataResult = MediatorLiveData<Result<List<AnomalyWithRecord>>>()

    fun getAnomalies(): LiveData<Result<List<AnomalyWithRecord>>> {
        val source = recordDao.getAllAnomaliesWithRecord()

        anomalyMediatorLiveDataResult.addSource(source) {
            anomalyMediatorLiveDataResult.value = Result.Success(it)
        }

        return anomalyMediatorLiveDataResult
    }

    suspend fun deleteAnomaly(anomaly: AnomalyEntity) {
        recordDao.deleteAnomaly(anomaly)
    }

    suspend fun toggleAnomaly(anomaly: AnomalyEntity) {
        val anomalyId = anomaly.id
        val isAnomaly = !anomaly.anomalyDetected
        recordDao.setAnomalyStatus(anomalyId, isAnomaly)
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
                instance ?: RecordRepository(
                    apiService,
                    recordDao,
                    accountDao,
                    appExecutors
                )
            }.also { instance = it }
    }
}