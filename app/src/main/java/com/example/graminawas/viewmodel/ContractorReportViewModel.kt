package com.example.graminawas.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graminawas.models.*

class ContractorReportViewModel : ViewModel() {
    private val _report = MutableLiveData<ContractorReport>()
    val report: LiveData<ContractorReport> = _report

    init {
        loadDummyData()
    }

    private fun loadDummyData() {
        val dummyReport = ContractorReport(
            totalProjects = 15,
            completedProjects = 8,
            inProgressProjects = 5,
            delayedProjects = 2,
            totalSanctionedAmount = 1800000.0,
            totalAmountSpent = 1200000.0,
            averageCompletionTime = "4 months",
            projectStatusDistribution = mapOf(
                ProjectStatus.COMPLETED to 8,
                ProjectStatus.IN_PROGRESS to 5,
                ProjectStatus.DELAYED to 2
            ),
            monthlyProgress = listOf(
                MonthlyProgress("Jan 2024", 2, 3, 150000.0),
                MonthlyProgress("Feb 2024", 3, 2, 200000.0),
                MonthlyProgress("Mar 2024", 3, 1, 180000.0)
            ),
            recentActivities = listOf(
                ProjectActivity(
                    "P001",
                    "Ram Singh",
                    ActivityType.PROGRESS_UPDATE,
                    "2024-03-15",
                    "Construction progress reached 75% - Foundation and walls completed"
                ),
                ProjectActivity(
                    "P002",
                    "Shyam Kumar",
                    ActivityType.FUNDS_RECEIVED,
                    "2024-03-10",
                    "Received second installment of ₹50,000 for roofing materials"
                ),
                ProjectActivity(
                    "P003",
                    "Mohan Lal",
                    ActivityType.COMPLETION_CERTIFICATE,
                    "2024-03-05",
                    "Project completed and certificate issued - Final inspection passed"
                ),
                ProjectActivity(
                    "P001",
                    "Ram Singh",
                    ActivityType.INSPECTION,
                    "2024-02-28",
                    "Quality inspection completed - All parameters met"
                ),
                ProjectActivity(
                    "P002",
                    "Shyam Kumar",
                    ActivityType.PROJECT_STARTED,
                    "2024-02-15",
                    "Construction work started - Site preparation completed"
                )
            )
        )
        _report.value = dummyReport
    }

    fun refreshReport() {
        // TODO: Implement actual data fetching from repository/database
        loadDummyData()
    }
} 