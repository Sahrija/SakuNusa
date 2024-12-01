package com.example.sakunusa.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.sakunusa.data.local.entity.RecordEntity
import java.util.Date

@Dao
interface RecordDao {
    @Query("SELECT * FROM records ORDER BY dateTime DESC")
    fun getAllRecords(): LiveData<List<RecordEntity>>

    @Query("SELECT * FROM records WHERE accountId = :accountId ORDER BY dateTime DESC")
    fun getAllRecords(accountId: Int): LiveData<List<RecordEntity>>

    @Query("SELECT * FROM records WHERE accountId = :accountId ORDER BY dateTime DESC")
    fun getAllRecordsByDateDesc(accountId: Int): LiveData<List<RecordEntity>>

    @Query("SELECT * FROM records WHERE accountId = :accountId ORDER BY dateTime ASC")
    fun getAllRecordsByDateAsc(accountId: Int): LiveData<List<RecordEntity>>

    @Query("SELECT * FROM records WHERE accountId = :accountId AND dateTime BETWEEN :startDate AND :endDate ORDER BY dateTime DESC")
    fun getAllRecords(accountId: Int, startDate: Date, endDate: Date): LiveData<List<RecordEntity>>

    @Insert
    fun insertRecord(record: RecordEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertRecords(records: List<RecordEntity>)

    @Update
    fun updateRecord(record: RecordEntity)

    @Delete
    fun deleteRecord(record: RecordEntity)
}