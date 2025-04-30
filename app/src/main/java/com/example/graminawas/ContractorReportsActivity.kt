package com.example.graminawas

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graminawas.adapter.ActivityAdapter
import com.example.graminawas.adapter.MonthlyProgressAdapter
import com.example.graminawas.databinding.ActivityContractorReportsBinding
import com.example.graminawas.viewmodel.ContractorReportViewModel
import java.text.NumberFormat
import java.util.Locale

class ContractorReportsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContractorReportsBinding
    private val viewModel: ContractorReportViewModel by viewModels()
    private lateinit var activityAdapter: ActivityAdapter
    private lateinit var monthlyProgressAdapter: MonthlyProgressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContractorReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapters()
        setupObservers()
    }

    private fun setupAdapters() {
        activityAdapter = ActivityAdapter()
        monthlyProgressAdapter = MonthlyProgressAdapter()

        binding.rvRecentActivities.apply {
            layoutManager = LinearLayoutManager(this@ContractorReportsActivity)
            adapter = activityAdapter
        }

        binding.rvMonthlyProgress.apply {
            layoutManager = LinearLayoutManager(this@ContractorReportsActivity)
            adapter = monthlyProgressAdapter
        }
    }

    private fun setupObservers() {
        viewModel.report.observe(this) { report ->
            // Update summary section
            binding.tvTotalProjects.text = report.totalProjects.toString()
            binding.tvCompletedProjects.text = "${report.completedProjects}/${report.totalProjects}"
            
            val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
            binding.tvTotalAmount.text = formatter.format(report.totalSanctionedAmount)
            binding.tvSpentAmount.text = formatter.format(report.totalAmountSpent)

            // Update adapters
            activityAdapter.submitList(report.recentActivities)
            monthlyProgressAdapter.submitList(report.monthlyProgress)
        }
    }
} 