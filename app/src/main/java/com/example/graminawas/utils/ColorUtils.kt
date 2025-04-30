package com.example.graminawas.utils

import android.content.Context
import com.example.graminawas.R
import com.example.graminawas.models.TransactionStatus
import com.example.graminawas.models.TransactionType

object ColorUtils {
    fun getTransactionTypeColor(context: Context, type: TransactionType): Int {
        return when (type) {
            TransactionType.RELEASE -> context.getColor(R.color.green)
            TransactionType.EXPENSE -> context.getColor(R.color.red)
            TransactionType.REFUND -> context.getColor(R.color.blue)
        }
    }

    fun getTransactionStatusColor(context: Context, status: TransactionStatus): Int {
        return when (status) {
            TransactionStatus.COMPLETED -> context.getColor(R.color.green)
            TransactionStatus.PENDING -> context.getColor(R.color.orange)
            TransactionStatus.FAILED -> context.getColor(R.color.red)
        }
    }
} 