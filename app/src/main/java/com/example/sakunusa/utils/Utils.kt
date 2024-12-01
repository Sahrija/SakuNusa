package com.example.sakunusa.utils

import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun formatDate(timestamp: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            .format(timestamp)
    }
}