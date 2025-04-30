package com.example.graminawas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graminawas.adapter.TransactionAdapter
import com.example.graminawas.databinding.ActivityFundTrackingBinding
import com.example.graminawas.models.Transaction
import com.example.graminawas.models.TransactionStatus
import com.example.graminawas.models.TransactionType
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

class FundTrackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFundTrackingBinding
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFundTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from intent
        val beneficiaryName = intent.getStringExtra("BENEFICIARY_NAME") ?: "Unknown"
        val totalSanctioned = intent.getDoubleExtra("TOTAL_SANCTIONED", 0.0)
        val amountReleased = intent.getDoubleExtra("AMOUNT_RELEASED", 0.0)
        val amountSpent = intent.getDoubleExtra("AMOUNT_SPENT", 0.0)

        setupUI(beneficiaryName, totalSanctioned, amountReleased, amountSpent)
        setupTransactionList()
        loadSampleTransactions()
    }

    private fun setupUI(
        beneficiaryName: String,
        totalSanctioned: Double,
        amountReleased: Double,
        amountSpent: Double
    ) {
        // Format currency
        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        formatter.maximumFractionDigits = 0

        // Set beneficiary info
        binding.tvBeneficiaryName.text = beneficiaryName
        binding.tvProjectId.text = "Project ID: P001" // This would come from the backend

        // Set fund amounts
        binding.tvTotalSanctioned.text = formatter.format(totalSanctioned)
        binding.tvAmountReleased.text = formatter.format(amountReleased)
        binding.tvAmountSpent.text = formatter.format(amountSpent)
        
        // Calculate and set balance
        val balance = amountReleased - amountSpent
        binding.tvBalance.text = formatter.format(balance)

        // Update progress bar
        val progress = ((amountSpent / totalSanctioned) * 100).toInt()
        binding.progressBarFunds.progress = progress

        // Set amount spent and remaining
        binding.tvAmountSpentLabel.text = "Spent: ${formatter.format(amountSpent)}"
        binding.tvAmountRemaining.text = "Remaining: ${formatter.format(totalSanctioned - amountSpent)}"
    }

    private fun setupTransactionList() {
        transactionAdapter = TransactionAdapter()
        binding.recyclerViewTransactions.apply {
            layoutManager = LinearLayoutManager(this@FundTrackingActivity)
            adapter = transactionAdapter
        }
    }

    private fun loadSampleTransactions() {
        // This would be replaced with actual data from a database or API
        val sampleTransactions = listOf(
            Transaction(
                id = "T001",
                date = Date(),
                amount = 50000.0,
                type = TransactionType.RELEASE,
                description = "First installment release",
                status = TransactionStatus.COMPLETED
            ),
            Transaction(
                id = "T002",
                date = Date(),
                amount = 25000.0,
                type = TransactionType.EXPENSE,
                description = "Construction materials purchase",
                status = TransactionStatus.COMPLETED
            ),
            Transaction(
                id = "T003",
                date = Date(),
                amount = 15000.0,
                type = TransactionType.EXPENSE,
                description = "Labor payment",
                status = TransactionStatus.PENDING
            )
        )
        transactionAdapter.submitList(sampleTransactions)
    }
}
