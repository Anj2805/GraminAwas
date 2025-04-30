package com.example.graminawas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.graminawas.data.AppDatabase
import com.example.graminawas.data.entities.Project
import com.example.graminawas.data.entities.ProjectUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val projectDao = database.projectDao()
    private val projectUpdateDao = database.projectUpdateDao()

    private val _project = MutableLiveData<Project?>()
    val project: LiveData<Project?> = _project

    private val _updates = MutableLiveData<List<ProjectUpdate>>()
    val updates: LiveData<List<ProjectUpdate>> = _updates

    fun addProject(project: Project) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                projectDao.insert(project)
            }
        }
    }

    fun getFirstProjectId(callback: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                val project = withContext(Dispatchers.IO) {
                    projectDao.getFirstProject()
                }
                callback(project?.id)
            } catch (e: Exception) {
                callback(null)
            }
        }
    }

    fun loadProject(projectId: String) {
        viewModelScope.launch {
            try {
                val project = withContext(Dispatchers.IO) {
                    projectDao.getProjectById(projectId)
                }
                _project.value = project
            } catch (e: Exception) {
                _project.value = null
            }
        }
    }

    fun loadProjectUpdates(projectId: String) {
        viewModelScope.launch {
            try {
                val updatesList = withContext(Dispatchers.IO) {
                    projectUpdateDao.getUpdatesForProject(projectId).first()
                }
                _updates.value = updatesList
            } catch (e: Exception) {
                _updates.value = emptyList()
            }
        }
    }

    fun addProjectUpdate(update: ProjectUpdate) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                projectUpdateDao.insert(update)
            }
            loadProjectUpdates(update.projectId)
        }
    }

    fun deleteProjectUpdate(update: ProjectUpdate) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                projectUpdateDao.delete(update)
            }
            loadProjectUpdates(update.projectId)
        }
    }

    fun getProjectById(id: String): LiveData<Project?> {
        val result = MutableLiveData<Project?>()
        viewModelScope.launch {
            try {
                val project = withContext(Dispatchers.IO) {
                    projectDao.getProjectById(id)
                }
                result.value = project
            } catch (e: Exception) {
                result.value = null
            }
        }
        return result
    }

    fun getProjectUpdates(projectId: String): LiveData<List<ProjectUpdate>> {
        val result = MutableLiveData<List<ProjectUpdate>>()
        viewModelScope.launch {
            try {
                val updates = withContext(Dispatchers.IO) {
                    projectUpdateDao.getUpdatesForProject(projectId).first()
                }
                result.value = updates
            } catch (e: Exception) {
                result.value = emptyList()
            }
        }
        return result
    }

    fun refreshProject(projectId: String) {
        loadProject(projectId)
        loadProjectUpdates(projectId)
    }
} 