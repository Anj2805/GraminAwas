package com.example.graminawas.data.dao

import androidx.room.*
import com.example.graminawas.data.entities.Beneficiary
import kotlinx.coroutines.flow.Flow

@Dao
interface BeneficiaryDao {
    @Query("SELECT * FROM beneficiaries")
    fun getAllBeneficiaries(): Flow<List<Beneficiary>>

    @Query("SELECT * FROM beneficiaries WHERE fundStatus != 'Pending'")
    fun getAllocatedBeneficiaries(): Flow<List<Beneficiary>>

    @Query("SELECT * FROM beneficiaries WHERE id = :id")
    fun getBeneficiaryById(id: String): Flow<Beneficiary?>

    @Query("SELECT * FROM beneficiaries WHERE aadharNumber = :aadharNumber LIMIT 1")
    fun getBeneficiaryByAadhar(aadharNumber: String): Flow<Beneficiary?>

    @Query("SELECT * FROM beneficiaries WHERE phoneNumber = :phoneNumber LIMIT 1")
    fun getBeneficiaryByPhone(phoneNumber: String): Flow<Beneficiary?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(beneficiary: Beneficiary)

    @Update
    suspend fun update(beneficiary: Beneficiary)

    @Delete
    suspend fun delete(beneficiary: Beneficiary)

    @Query("SELECT * FROM beneficiaries WHERE fundStatus != 'Pending'")
    fun getAllWithFunds(): Flow<List<Beneficiary>>

    @Update
    suspend fun updateBeneficiary(beneficiary: Beneficiary)
}
