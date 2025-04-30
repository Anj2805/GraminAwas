package com.example.graminawas.models

data class ContractorReport(
    val totalProjects: Int,
    val completedProjects: Int,
    val inProgressProjects: Int,
    val delayedProjects: Int,
    val totalSanctionedAmount: Double,
    val totalAmountSpent: Double,
    val averageCompletionTime: String,
    val projectStatusDistribution: Map<ProjectStatus, Int>,
    val monthlyProgress: List<MonthlyProgress>,
    val recentActivities: List<ProjectActivity>
)

data class MonthlyProgress(
    val month: String,
    val projectsCompleted: Int,
    val projectsStarted: Int,
    val totalExpenditure: Double
)

data class ProjectActivity(
    val projectId: String,
    val beneficiaryName: String,
    val activityType: ActivityType,
    val timestamp: String,
    val description: String
)

enum class ActivityType {
    PROJECT_STARTED,
    PROGRESS_UPDATE,
    FUNDS_RECEIVED,
    COMPLETION_CERTIFICATE,
    INSPECTION
} 