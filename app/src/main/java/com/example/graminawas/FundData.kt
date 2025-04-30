package com.example.graminawas.models

data class FundData(
    val beneficiaryName: String,
    val totalSanctioned: Int,
    val amountReleased: Int,
    val amountSpent: Int
) {
    val remaining: Int
        get() = amountReleased - amountSpent
}
