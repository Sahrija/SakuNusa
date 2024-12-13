package com.example.sakunusa.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class AnomalyWithRecord(
    @Embedded val anomaly: AnomalyEntity,
    @Relation(
        parentColumn = "recordId",
        entityColumn = "id",
    )
    val record: RecordEntity?
)
