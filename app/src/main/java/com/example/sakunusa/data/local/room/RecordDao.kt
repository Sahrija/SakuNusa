package com.example.sakunusa.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.sakunusa.data.local.entity.AnomalyEntity
import com.example.sakunusa.data.local.entity.AnomalyWithRecord
import com.example.sakunusa.data.local.entity.RecordEntity
import java.util.Date

@Dao
interface RecordDao {
    @Query("SELECT * FROM records ORDER BY dateTime DESC")
    fun getAllRecords(): LiveData<List<RecordEntity>>

    @Query("SELECT * FROM records WHERE accountId = :accountId ORDER BY dateTime DESC")
    fun getAllRecords(accountId: Int): LiveData<List<RecordEntity>>


    @Query("SELECT * FROM records WHERE accountId = :accountId AND dateTime BETWEEN :startDate AND :endDate ORDER BY dateTime DESC")
    fun getAllRecords(accountId: Int, startDate: Date, endDate: Date): LiveData<List<RecordEntity>>

    @Insert
    suspend fun insertRecord(record: RecordEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertRecords(records: List<RecordEntity>)

    @Update
    suspend fun updateRecord(record: RecordEntity)

    @Query("DELETE FROM records WHERE id = :recordId")
    suspend fun deleteRecordById(recordId: Int): Int

    @Query("SELECT * FROM records WHERE id = :recordId ")
    suspend fun getRecordById(recordId: Int): RecordEntity?

    @Query(
        """
        SELECT * FROM records 
        WHERE accountId IN (
            SELECT id FROM account WHERE isSelected = 1
        )
        ORDER BY dateTime ASC
    """
    )

    fun getRecordsForSelectedAccountsAsc(): LiveData<List<RecordEntity>>

    @Query(
        """
        SELECT * FROM records 
        WHERE accountId IN (
            SELECT id FROM account WHERE isSelected = 1
        )
        ORDER BY dateTime DESC
    """
    )
    fun getRecordsForSelectedAccountsDesc(): LiveData<List<RecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnomaly(anomalyEntity: AnomalyEntity)

    @Transaction
    @Query("SELECT * FROM anomalies WHERE id = :anomalyId")
    fun getAnomalyWithRecordById(anomalyId: Int): AnomalyWithRecord?

    @Transaction
    @Query("SELECT * FROM anomalies")
    fun getAllAnomaliesWithRecord(): LiveData<List<AnomalyWithRecord>>

    @Delete
    suspend fun deleteAnomaly(anomaly: AnomalyEntity)
    @Query("DELETE FROM anomalies WHERE recordId = :recordId")
    suspend fun deleteAnomalyByRecordId(recordId: Int)

    @Query("UPDATE anomalies SET anomalyDetected = :anomaly WHERE id = :anomalyId")
    suspend fun setAnomalyStatus(anomalyId: Int, anomaly: Boolean)
}