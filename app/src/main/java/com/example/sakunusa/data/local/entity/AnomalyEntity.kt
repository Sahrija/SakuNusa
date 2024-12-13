package com.example.sakunusa.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anomalies")
data class AnomalyEntity(
    @PrimaryKey
    val id: Int,
    val date: Long,
    val loss: Double,
    val recordId: Int,
    val anomalyDetected: Boolean,
)