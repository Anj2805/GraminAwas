package com.example.graminawas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.graminawas.databinding.ActivityContractorDashboardBinding
import com.example.graminawas.models.FundData
import com.example.graminawas.viewmodel.ProjectAssignmentViewModel

class ContractorDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContractorDashboardBinding
    private val viewModel: ProjectAssignmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContractorDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnAssignProject.setOnClickListener {
            startActivity(Intent(this, AssignProjectActivity::class.java))
        }

        binding.btnUploadProgress.setOnClickListener {
            startActivity(Intent(this, ConstructionUpdateActivity::class.java))
        }

        binding.btnFundTracking.setOnClickListener {
            val fundData = FundData(
                beneficiaryName = "John Doe",
                totalSanctioned = 120000,
                amountReleased = 100000,
                amountSpent = 75000
            )

            val intent = Intent(this, FundTrackingActivity::class.java).apply {
                putExtra("BENEFICIARY_NAME", fundData.beneficiaryName)
                putExtra("TOTAL_SANCTIONED", fundData.totalSanctioned)
                putExtra("AMOUNT_RELEASED", fundData.amountReleased)
                putExtra("AMOUNT_SPENT", fundData.amountSpent)
            }

            startActivity(intent)
        }

        binding.btnDeadlines.setOnClickListener {
            startActivity(Intent(this, ProjectDeadlinesActivity::class.java))
        }

        binding.btnMyProjects.setOnClickListener {
            // TODO: Implement My Projects Activity
            Toast.makeText(this, "My Projects feature coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.btnReports.setOnClickListener {
            startActivity(Intent(this, ContractorReportsActivity::class.java))
        }
    }
}
