package com.example.sakunusa.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.sakunusa.data.remote.response.RecordItem
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "records")
data class RecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val accountId: Int,
    val amount: Float,
    val category: String,
    val dateTime: Long,
    val description: String?,
) : Parcelable {
    constructor(it: RecordItem) : this(
        id = it.id.toInt(),
        accountId = it.accountId,
        amount = it.amount ?: 0F,
        category = it.category ?: "",
        dateTime = it.dateTime ?: 0,
        description = it.description,
    )
}