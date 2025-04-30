package com.example.graminawas.data

import androidx.room.*
import com.example.graminawas.model.Beneficiary
import kotlinx.coroutines.flow.Flow

@Dao
interface BeneficiaryDao {
    @Query("SELECT * FROM beneficiaries")
    fun getAllBeneficiaries(): Flow<List<Beneficiary>>

    @Query("SELECT * FROM beneficiaries WHERE id = :id")
    suspend fun getBeneficiaryById(id: String): Beneficiary?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(beneficiary: Beneficiary)

    @Update
    suspend fun update(beneficiary: Beneficiary)

    @Delete
    suspend fun delete(beneficiary: Beneficiary)
} 