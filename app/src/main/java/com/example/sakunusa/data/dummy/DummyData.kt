package com.example.sakunusa.data.dummy

import com.example.sakunusa.data.local.entity.RecordEntity

class DummyData {
    fun getRecords(): List<RecordEntity> {
        return listOf(
            RecordEntity(1,1, 2000F, "expense", 10000L, description = null),
            RecordEntity(2,1, 4000F, "expense", 10000L, description = null),
            RecordEntity(3,1, 6000F, "expense", 10000L, description = null),
            RecordEntity(4,1, 7000F, "expense", 10000L, description = null),
            RecordEntity(5,1, 3000F, "expense", 10000L, description = null),
        )
    }
}