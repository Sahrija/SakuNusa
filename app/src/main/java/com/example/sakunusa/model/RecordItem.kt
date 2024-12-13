package com.example.sakunusa.model

import com.example.sakunusa.data.local.entity.RecordEntity
import java.lang.IllegalArgumentException
import java.util.Calendar
import java.util.Date

sealed class RecordItem {
    data class GroupHeader(val title: String, val totalAmount: Float) : RecordItem()

    data class Record(
        val id: Int,
        val accountId: Int,
        val type: Int,
        val amount: Float,
        val category: String,
        val dateTime: Long,
        val description: String?
    ) : RecordItem()

    companion object {

        const val GROUP_BY_DAY = 0
        const val GROUP_BY_MONTH = 1

        fun groupRecords(
            records: List<RecordEntity>,
            groupBy: Int = GROUP_BY_DAY
        ): List<RecordItem> {
            val groupedRecords = mutableListOf<RecordItem>()

            // Group by month/year
            val groupedByDate = records.groupBy {
                val date = Date(it.dateTime)
                val calendar = Calendar.getInstance().apply { time = date }
                when (groupBy) {
                    GROUP_BY_DAY -> {
                        groupByDay(calendar)
                    }

                    GROUP_BY_MONTH -> {
                        groupByMonth(calendar)
                    }

                    else -> {
                        throw IllegalArgumentException("Unknown Group By: $groupBy")
                    }
                }
            }

            groupedByDate.forEach { (dateKey, recordList) ->
                // Calculate the balance for this group
                val balance = recordList.sumOf { it.amount.toDouble() }.toFloat()

                // Add header
                groupedRecords.add(GroupHeader(title = dateKey, totalAmount = balance))

                // Add all records
                groupedRecords.addAll(recordList.map {
                    Record(
                        id = it.id,
                        accountId = it.accountId,
                        amount = it.amount,
                        type = it.type,
                        category = it.category,
                        dateTime = it.dateTime,
                        description = it.description
                    )
                })
            }

            return groupedRecords
        }

        private fun groupByDay(calendar: Calendar): String {
            return "${calendar.get(Calendar.DATE)} ${calendar.get(Calendar.MONTH) + 1} ${
                calendar.get(
                    Calendar.YEAR
                )
            }"
        }

        private fun groupByMonth(calendar: Calendar): String {
            return "${calendar.get(Calendar.MONTH) + 1} ${calendar.get(Calendar.YEAR)}"
        }
    }
}
