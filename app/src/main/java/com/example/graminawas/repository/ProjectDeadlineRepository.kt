package com.example.graminawas.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.graminawas.data.AppDatabase
import com.example.graminawas.data.entities.ProjectDeadline
import kotlinx.coroutines.flow.Flow

class ProjectDeadlineRepository(private val database: AppDatabase) {
    val allDeadlines: LiveData<List<ProjectDeadline>> = 
        database.projectDeadlineDao().getAllDeadlines().asLiveData()

    suspend fun getDeadlinesByProject(projectId: String): List<ProjectDeadline> {
        return database.projectDeadlineDao().getDeadlinesByProject(projectId).asLiveData().value ?: emptyList()
    }

    suspend fun getDeadlineById(id: String): ProjectDeadline? {
        return database.projectDeadlineDao().getDeadlineById(id)
    }

    suspend fun insert(deadline: ProjectDeadline) {
        database.projectDeadlineDao().insert(deadline)
    }

    suspend fun update(deadline: ProjectDeadline) {
        database.projectDeadlineDao().update(deadline)
    }

    suspend fun delete(deadline: ProjectDeadline) {
        database.projectDeadlineDao().delete(deadline)
    }

    suspend fun getDeadlinesByStatus(status: String): List<ProjectDeadline> {
        return database.projectDeadlineDao().getDeadlinesByStatus(status).asLiveData().value ?: emptyList()
    }
} 