package com.example.graminawas.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graminawas.models.ProjectAssignment
import com.example.graminawas.models.ProjectStatus

class ProjectAssignmentViewModel : ViewModel() {
    private val _assignedProjects = MutableLiveData<List<ProjectAssignment>>()
    val assignedProjects: LiveData<List<ProjectAssignment>> = _assignedProjects

    private val _selectedProject = MutableLiveData<ProjectAssignment?>()
    val selectedProject: LiveData<ProjectAssignment?> = _selectedProject

    init {
        // Initialize with test data
        loadTestData()
    }

    private fun loadTestData() {
        val testProjects = listOf(
            ProjectAssignment(
                projectId = "P001",
                beneficiaryName = "Ram Singh",
                address = "Village: Ramgarh, Block: Dhanbad",
                sanctionedAmount = 120000.0,
                progressPercentage = 0,
                startDate = "2024-03-01",
                deadline = "2024-09-01",
                status = ProjectStatus.PENDING,
                latitude = 23.7957,
                longitude = 86.4304
            ),
            ProjectAssignment(
                projectId = "P002",
                beneficiaryName = "Shyam Kumar",
                address = "Village: Bokaro, Block: Bokaro",
                sanctionedAmount = 120000.0,
                progressPercentage = 0,
                startDate = "2024-03-01",
                deadline = "2024-09-01",
                status = ProjectStatus.PENDING,
                latitude = 23.6693,
                longitude = 86.1511
            ),
            ProjectAssignment(
                projectId = "P003",
                beneficiaryName = "Mohan Lal",
                address = "Village: Hazaribagh, Block: Hazaribagh",
                sanctionedAmount = 120000.0,
                progressPercentage = 0,
                startDate = "2024-03-01",
                deadline = "2024-09-01",
                status = ProjectStatus.PENDING,
                latitude = 24.0000,
                longitude = 85.3500
            )
        )
        _assignedProjects.value = testProjects
    }

    fun selectProject(project: ProjectAssignment) {
        _selectedProject.value = project
    }

    fun updateProjectStatus(projectId: String, newStatus: ProjectStatus) {
        val updatedProjects = _assignedProjects.value?.map { project ->
            if (project.projectId == projectId) {
                project.copy(status = newStatus)
            } else {
                project
            }
        }
        _assignedProjects.value = updatedProjects
    }

    // TODO: Add methods to fetch data from repository/database
    fun loadProjects() {
        // This will be implemented to fetch actual project data
    }
} 