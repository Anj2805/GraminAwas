package com.example.graminawas

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graminawas.adapter.ProjectAssignmentAdapter
import com.example.graminawas.databinding.ActivityAssignProjectBinding
import com.example.graminawas.models.ProjectStatus
import com.example.graminawas.viewmodel.ProjectAssignmentViewModel

class AssignProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssignProjectBinding
    private lateinit var adapter: ProjectAssignmentAdapter
    private val viewModel: ProjectAssignmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = ProjectAssignmentAdapter(
            onItemClick = { project ->
                // Handle project item click
                Toast.makeText(this, "Selected: ${project.beneficiaryName}", Toast.LENGTH_SHORT).show()
            },
            onAssignClick = { project ->
                // Handle project assignment
                viewModel.updateProjectStatus(project.projectId, ProjectStatus.IN_PROGRESS)
                Toast.makeText(this, "Project assigned successfully", Toast.LENGTH_SHORT).show()
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AssignProjectActivity)
            adapter = this@AssignProjectActivity.adapter
        }
    }

    private fun setupObservers() {
        viewModel.assignedProjects.observe(this) { projects ->
            adapter.submitList(projects)
        }
    }
} 