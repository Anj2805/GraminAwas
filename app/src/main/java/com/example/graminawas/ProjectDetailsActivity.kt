package com.example.graminawas

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.graminawas.databinding.ActivityProjectDetailsBinding
import com.example.graminawas.databinding.DialogEditProjectBinding
import com.example.graminawas.data.entities.Project
import com.example.graminawas.data.AppDatabase
import com.example.graminawas.repository.ProjectRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProjectDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectDetailsBinding
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private lateinit var projectRepository: ProjectRepository
    private var currentProject: Project? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize repository
        projectRepository = ProjectRepository(AppDatabase.getDatabase(this))

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get project ID from intent
        val projectId = intent.getStringExtra("project_id")
        if (projectId == null) {
            Toast.makeText(this, "Project ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Load project details
        loadProjectDetails(projectId)

        // Set up edit button click listener
        binding.fabEdit.setOnClickListener {
            currentProject?.let { project ->
                showEditDialog(
                    project.name,
                    project.location,
                    dateFormat.format(project.startDate),
                    dateFormat.format(project.endDate),
                    project.budget.toString(),
                    project.description
                )
            }
        }
    }

    private fun loadProjectDetails(projectId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val project = withContext(Dispatchers.IO) {
                    projectRepository.getProjectById(projectId)
                }
                project?.let {
                    currentProject = it
                    updateUI(it)
                } ?: run {
                    Toast.makeText(this@ProjectDetailsActivity, 
                        "Project not found", 
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProjectDetailsActivity, 
                    "Error: ${e.message}", 
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun updateUI(project: Project) {
        binding.tvProjectName.text = project.name
        binding.tvProjectStatus.text = project.status
        binding.tvLocation.text = project.location
        binding.tvStartDate.text = dateFormat.format(project.startDate)
        binding.tvEndDate.text = dateFormat.format(project.endDate)
        binding.tvBudget.text = project.budget.toString()
        binding.tvDescription.text = project.description
    }

    private fun showEditDialog(
        currentName: String,
        currentLocation: String,
        currentStartDate: String,
        currentEndDate: String,
        currentBudget: String,
        currentDescription: String
    ) {
        val dialog = Dialog(this)
        val dialogBinding = DialogEditProjectBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            (resources.displayMetrics.heightPixels * 0.8).toInt()
        )

        // Set current values
        dialogBinding.etProjectName.setText(currentName)
        dialogBinding.etLocation.setText(currentLocation)
        dialogBinding.etStartDate.setText(currentStartDate)
        dialogBinding.etEndDate.setText(currentEndDate)
        dialogBinding.etBudget.setText(currentBudget)
        dialogBinding.etDescription.setText(currentDescription)

        // Set up date pickers
        dialogBinding.etStartDate.setOnClickListener {
            showDatePicker { date ->
                dialogBinding.etStartDate.setText(dateFormat.format(date))
            }
        }

        dialogBinding.etEndDate.setOnClickListener {
            showDatePicker { date ->
                dialogBinding.etEndDate.setText(dateFormat.format(date))
            }
        }

        // Set up save button
        dialogBinding.btnSave.setOnClickListener {
            val newName = dialogBinding.etProjectName.text.toString()
            val newLocation = dialogBinding.etLocation.text.toString()
            val newStartDate = dialogBinding.etStartDate.text.toString()
            val newEndDate = dialogBinding.etEndDate.text.toString()
            val newBudget = dialogBinding.etBudget.text.toString()
            val newDescription = dialogBinding.etDescription.text.toString()

            if (newName.isBlank() || newLocation.isBlank() || newStartDate.isBlank() ||
                newEndDate.isBlank() || newBudget.isBlank() || newDescription.isBlank()
            ) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            currentProject?.let { project ->
                val updatedProject = project.copy(
                    name = newName,
                    location = newLocation,
                    startDate = dateFormat.parse(newStartDate)!!,
                    endDate = dateFormat.parse(newEndDate)!!,
                    budget = newBudget.toDouble(),
                    description = newDescription
                )

                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        withContext(Dispatchers.IO) {
                            projectRepository.update(updatedProject)
                        }
                        currentProject = updatedProject
                        updateUI(updatedProject)
                        Snackbar.make(binding.root, 
                            "Project updated successfully", 
                            Snackbar.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } catch (e: Exception) {
                        Toast.makeText(this@ProjectDetailsActivity, 
                            "Error: ${e.message}", 
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        dialog.show()
    }

    private fun showDatePicker(onDateSet: (Calendar) -> Unit) {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                onDateSet(calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
} 