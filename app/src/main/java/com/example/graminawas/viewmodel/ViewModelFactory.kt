package com.example.graminawas.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BeneficiaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BeneficiaryViewModel(application) as T
        }
        if (modelClass.isAssignableFrom(ProjectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectViewModel(application) as T
        }
        if (modelClass.isAssignableFrom(ProjectDeadlineViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectDeadlineViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 