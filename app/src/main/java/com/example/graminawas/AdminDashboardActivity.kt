package com.example.graminawas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.graminawas.databinding.ActivityAdminDashboardBinding
import com.example.graminawas.viewmodel.ProjectViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.graminawas.viewmodel.ViewModelFactory

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var viewModel: ProjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory(application)).get(ProjectViewModel::class.java)

        // Navigate to AddBeneficiaryActivity
        binding.btnAddBeneficiary.setOnClickListener {
            val intent = Intent(this, AddBeneficiaryActivity::class.java)
            startActivity(intent)
        }

        // Navigate to ViewBeneficiaryActivity (normal view)
        binding.btnBeneficiaries.setOnClickListener {
            val intent = Intent(this, ViewBeneficiaryActivity::class.java)
            startActivity(intent)
        }

        // Navigate to AddProjectActivity
        binding.btnAddProject.setOnClickListener {
            val intent = Intent(this, AddProjectActivity::class.java)
            startActivity(intent)
        }

        // Navigate to TrackProgressActivity
        binding.btnTrackProgress.setOnClickListener {
            // Get the first project ID from the database
            viewModel.getFirstProjectId { projectId ->
                if (projectId != null) {
                    val intent = Intent(this, TrackProgressActivity::class.java).apply {
                        putExtra("projectId", projectId)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No projects found", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Navigate to AllocateFundsActivity
        binding.btnAllocateFund.setOnClickListener {
            val intent = Intent(this, AllocateFundsActivity::class.java)
            startActivity(intent)
        }

        // Navigate to FundAllocationActivity
        binding.checkFundsButton.setOnClickListener {
            val intent = Intent(this, FundAllocationActivity::class.java)
            startActivity(intent)
        }

        // ProjectMapActivity
        binding.btnMapProjects.setOnClickListener {
            val intent = Intent(this, ProjectMapActivity::class.java)
            startActivity(intent)
        }
    }
}
