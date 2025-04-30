package com.example.graminawas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.graminawas.data.entities.Project
import com.example.graminawas.databinding.ActivityAddProjectBinding
import com.example.graminawas.viewmodel.ProjectViewModel
import com.example.graminawas.viewmodel.ViewModelFactory
import java.util.Date
import java.util.UUID

class AddProjectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProjectBinding
    private lateinit var viewModel: ProjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory(application)).get(ProjectViewModel::class.java)

        setupToolbar()
        setupSubmitButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add New Project"
    }

    private fun setupSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            if (validateInputs()) {
                val project = createProject()
                viewModel.addProject(project)
                Toast.makeText(this, "Project added successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val name = binding.etProjectName.text.toString()
        val description = binding.etProjectDescription.text.toString()
        val location = binding.etLocation.text.toString()
        val beneficiaryId = binding.etBeneficiaryId.text.toString()
        val budget = binding.etBudget.text.toString()

        if (name.isEmpty()) {
            binding.etProjectName.error = "Project name is required"
            return false
        }
        if (description.isEmpty()) {
            binding.etProjectDescription.error = "Description is required"
            return false
        }
        if (location.isEmpty()) {
            binding.etLocation.error = "Location is required"
            return false
        }
        if (beneficiaryId.isEmpty()) {
            binding.etBeneficiaryId.error = "Beneficiary ID is required"
            return false
        }
        if (budget.isEmpty()) {
            binding.etBudget.error = "Budget is required"
            return false
        }

        return true
    }

    private fun createProject(): Project {
        return Project(
            id = UUID.randomUUID().toString(),
            name = binding.etProjectName.text.toString(),
            description = binding.etProjectDescription.text.toString(),
            location = binding.etLocation.text.toString(),
            startDate = Date(),
            endDate = Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000), // 1 year from now
            status = "Not Started",
            beneficiaryId = binding.etBeneficiaryId.text.toString(),
            budget = binding.etBudget.text.toString().toDouble(),
            progress = 0
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 