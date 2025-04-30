package com.example.graminawas.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.graminawas.data.AppDatabase
import com.example.graminawas.data.entities.Beneficiary
import kotlinx.coroutines.flow.Flow

class BeneficiaryRepository(private val database: AppDatabase) {
    private val beneficiaryDao = database.beneficiaryDao()

    val allBeneficiaries: Flow<List<Beneficiary>> = 
        beneficiaryDao.getAllBeneficiaries()

    val allocatedBeneficiaries: Flow<List<Beneficiary>> = 
        beneficiaryDao.getAllocatedBeneficiaries()

    fun getBeneficiaryById(id: String): Flow<Beneficiary?> {
        return beneficiaryDao.getBeneficiaryById(id)
    }

    fun getBeneficiaryByAadhar(aadharNumber: String): Flow<Beneficiary?> {
        return beneficiaryDao.getBeneficiaryByAadhar(aadharNumber)
    }

    fun getBeneficiaryByPhone(phoneNumber: String): Flow<Beneficiary?> {
        return beneficiaryDao.getBeneficiaryByPhone(phoneNumber)
    }

    suspend fun insert(beneficiary: Beneficiary) {
        beneficiaryDao.insert(beneficiary)
    }

    suspend fun update(beneficiary: Beneficiary) {
        beneficiaryDao.update(beneficiary)
    }

    suspend fun delete(beneficiary: Beneficiary) {
        beneficiaryDao.delete(beneficiary)
    }
} 