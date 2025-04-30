package com.example.graminawas.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.graminawas.data.AppDatabase
import com.example.graminawas.data.entities.Project
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val database: AppDatabase) {
    val allProjects: LiveData<List<Project>> = 
        database.projectDao().getAllProjects().asLiveData()

    suspend fun getProjectById(id: String): Project? {
        return database.projectDao().getProjectById(id)
    }

    suspend fun getProjectsByBeneficiary(beneficiaryId: String): List<Project> {
        return database.projectDao().getProjectsByBeneficiary(beneficiaryId).asLiveData().value ?: emptyList()
    }

    suspend fun getProjectsByStatus(status: String): List<Project> {
        return database.projectDao().getProjectsByStatus(status).asLiveData().value ?: emptyList()
    }

    suspend fun insert(project: Project) {
        database.projectDao().insert(project)
    }

    suspend fun update(project: Project) {
        database.projectDao().update(project)
    }

    suspend fun delete(project: Project) {
        database.projectDao().delete(project)
    }
} 