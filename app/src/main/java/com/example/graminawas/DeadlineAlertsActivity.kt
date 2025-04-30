package com.example.graminawas

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class DeadlineAlertsActivity : AppCompatActivity() {

    private lateinit var tvExpectedDate: TextView
    private lateinit var tvCountdown: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvProjectName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deadline_alerts)

        tvExpectedDate = findViewById(R.id.tvExpectedDate)
        tvCountdown = findViewById(R.id.tvCountdown)
        tvStatus = findViewById(R.id.tvStatus)
        tvProjectName = findViewById(R.id.tvProjectName)

        // Example data (replace with dynamic data from DB if needed)
        val expectedDateStr = "2025-05-01" // Format: yyyy-MM-dd
        val projectName = "Sample Housing Project"

        tvExpectedDate.text = "Expected Completion: $expectedDateStr"
        tvProjectName.text = "Project: $projectName"

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Calendar.getInstance().time
        val expectedDate = dateFormat.parse(expectedDateStr)

        expectedDate?.let {
            val diffInMillis = expectedDate.time - today.time
            val diffInDays = (diffInMillis / (1000 * 60 * 60 * 24)).toInt()

            if (diffInDays >= 0) {
                tvCountdown.text = "Days left: $diffInDays"
                tvStatus.text = "Status: On track"
                tvStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark))
            } else {
                tvCountdown.text = "Overdue by ${abs(diffInDays)} days"
                tvStatus.text = "Status: Overdue"
                tvStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            }
        }
    }
}
