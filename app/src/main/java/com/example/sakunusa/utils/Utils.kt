package com.example.sakunusa.utils

import android.content.Context
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun formatDate(timestamp: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            .format(timestamp)
    }

    fun getColorAsString(context: Context, colorRes: Int): String {
        val color = ContextCompat.getColor(context, colorRes)
        return String.format("#%06X", (0xFFFFFF and color))
    }
}