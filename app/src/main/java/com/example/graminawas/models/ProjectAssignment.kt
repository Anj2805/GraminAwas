package com.example.graminawas.models

data class ProjectAssignment(
    val projectId: String,
    val beneficiaryName: String,
    val address: String,
    val sanctionedAmount: Double,
    val progressPercentage: Int,
    val startDate: String,
    val deadline: String,
    val status: ProjectStatus,
    val latitude: Double,
    val longitude: Double
)

enum class ProjectStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    DELAYED
} 