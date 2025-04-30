package com.example.graminawas.models

import java.util.Date

data class Transaction(
    val id: String,
    val date: Date,
    val amount: Double,
    val type: TransactionType,
    val description: String,
    val status: TransactionStatus
)

enum class TransactionType {
    RELEASE, EXPENSE, REFUND
}

enum class TransactionStatus {
    PENDING, COMPLETED, FAILED
} 