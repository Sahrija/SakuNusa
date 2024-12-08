package com.example.sakunusa.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecordsResponse(
    val data: List<RecordItem?>? = null,
    val error: Boolean? = null,
    val message: String? = null
) : Parcelable

@Parcelize
data class RecordItem(
    val id: String,
    val accountId: Int,
    val type: Int,
    val amount: Float? = null,
    val category: String? = null,
    val dateTime: Long? = null,
    val description: String? = null,
) : Parcelable

data class DeleteResponse(
    val error: Boolean? = null,
    val message: String? = null
)