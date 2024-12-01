package com.example.sakunusa.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class AccountWithRecords(
    @Embedded val account: AccountEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val records: List<RecordEntity>
)