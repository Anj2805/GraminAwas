package com.example.graminawas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.graminawas.data.AppDatabase
import com.example.graminawas.data.entities.Beneficiary
import com.example.graminawas.repository.BeneficiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.map

class BeneficiaryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BeneficiaryRepository
    private val _allocatedBeneficiaries = MutableStateFlow<List<Beneficiary>>(emptyList())
    val allocatedBeneficiaries: StateFlow<List<Beneficiary>> = _allocatedBeneficiaries

    private val _allBeneficiaries = MutableStateFlow<List<Beneficiary>>(emptyList())
    val allBeneficiaries: StateFlow<List<Beneficiary>> = _allBeneficiaries

    init {
        val database = AppDatabase.getDatabase(application)
        repository = BeneficiaryRepository(database)
        loadAllocatedBeneficiaries()
        loadAllBeneficiaries()
    }

    private fun loadAllocatedBeneficiaries() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.allocatedBeneficiaries.collect { beneficiaries: List<Beneficiary> ->
                _allocatedBeneficiaries.value = beneficiaries
            }
        }
    }

    private fun loadAllBeneficiaries() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.allBeneficiaries.collect { beneficiaries: List<Beneficiary> ->
                _allBeneficiaries.value = beneficiaries
            }
        }
    }

    fun getBeneficiaryById(id: String): Flow<Beneficiary?> {
        return repository.getBeneficiaryById(id)
    }

    fun getBeneficiaryByPhone(phone: String): Flow<Beneficiary?> {
        return repository.getBeneficiaryByPhone(phone)
    }

    suspend fun allocateFunds(beneficiaryId: String, amount: Double) {
        try {
            val beneficiary = repository.getBeneficiaryById(beneficiaryId).first()
            beneficiary?.let { beneficiary ->
                val updatedBeneficiary = beneficiary.copy(
                    fundAllocated = amount,
                    fundStatus = "Allocated"
                )
                repository.update(updatedBeneficiary)
            }
        } catch (e: Exception) {
            // Handle error
        }
    }

    fun updateBeneficiary(beneficiary: Beneficiary) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(beneficiary)
        }
    }
}

