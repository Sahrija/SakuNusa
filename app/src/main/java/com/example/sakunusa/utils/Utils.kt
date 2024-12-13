package com.example.sakunusa.utils

import android.content.Context
import androidx.core.content.ContextCompat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun formatDate(timestamp: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            .format(timestamp)
    }

//    fun formatAmount(amount: Number): String {
//        val symbols = DecimalFormatSymbols(Locale("id", "ID"))
//        symbols.groupingSeparator = '.'
//        val formatter = DecimalFormat("#,###", symbols)
//        return formatter.format(amount)
//    }

    fun formatAmount(amount: Number): String {
        // Create a DecimalFormatSymbols instance to set the grouping separator
        val symbols = DecimalFormatSymbols(Locale("id", "ID"))
        symbols.groupingSeparator = '.'  // Set the grouping separator to a dot

        // Create a DecimalFormat instance with the custom symbols
        val formatter = DecimalFormat("#,###", symbols)

        // Return the formatted amount as a string
        return formatter.format(amount)
    }

    fun getColorAsString(context: Context, colorRes: Int): String {
        val color = ContextCompat.getColor(context, colorRes)
        return String.format("#%06X", (0xFFFFFF and color))
    }
}