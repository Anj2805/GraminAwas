package com.example.graminawas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.graminawas.data.AppDatabase
import com.example.graminawas.data.entities.ProjectDeadline
import kotlinx.coroutines.launch

class ProjectDeadlineViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val projectDeadlineDao = database.projectDeadlineDao()

    private val _deadlines = MutableLiveData<List<ProjectDeadline>>()
    val deadlines: LiveData<List<ProjectDeadline>> = _deadlines

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadAllDeadlines()
    }

    private fun loadAllDeadlines() {
        viewModelScope.launch {
            try {
                projectDeadlineDao.getAllDeadlines().collect { deadlines ->
                    _deadlines.value = deadlines
                }
            } catch (e: Exception) {
                _error.value = "Error loading deadlines: ${e.message}"
            }
        }
    }

    fun addDeadline(deadline: ProjectDeadline) {
        viewModelScope.launch {
            try {
                projectDeadlineDao.insert(deadline)
            } catch (e: Exception) {
                _error.value = "Error adding deadline: ${e.message}"
            }
        }
    }

    fun updateDeadline(deadline: ProjectDeadline) {
        viewModelScope.launch {
            try {
                projectDeadlineDao.update(deadline)
            } catch (e: Exception) {
                _error.value = "Error updating deadline: ${e.message}"
            }
        }
    }

    fun deleteDeadline(deadline: ProjectDeadline) {
        viewModelScope.launch {
            try {
                projectDeadlineDao.delete(deadline)
            } catch (e: Exception) {
                _error.value = "Error deleting deadline: ${e.message}"
            }
        }
    }

    fun getDeadlineById(id: String): LiveData<ProjectDeadline?> {
        val result = MutableLiveData<ProjectDeadline?>()
        viewModelScope.launch {
            try {
                result.value = projectDeadlineDao.getDeadlineById(id)
            } catch (e: Exception) {
                _error.value = "Error getting deadline: ${e.message}"
            }
        }
        return result
    }

    fun getDeadlinesByProject(projectId: String): LiveData<List<ProjectDeadline>> {
        val result = MutableLiveData<List<ProjectDeadline>>()
        viewModelScope.launch {
            try {
                projectDeadlineDao.getDeadlinesByProject(projectId).collect { deadlines ->
                    result.value = deadlines
                }
            } catch (e: Exception) {
                _error.value = "Error getting project deadlines: ${e.message}"
            }
        }
        return result
    }
} 