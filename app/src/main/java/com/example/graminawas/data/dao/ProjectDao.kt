package com.example.graminawas.data.dao

import androidx.room.*
import com.example.graminawas.data.entities.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getProjectById(id: String): Project?

    @Query("SELECT * FROM projects WHERE beneficiaryId = :beneficiaryId")
    fun getProjectsByBeneficiary(beneficiaryId: String): Flow<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(project: Project)

    @Update
    suspend fun update(project: Project)

    @Delete
    suspend fun delete(project: Project)

    @Query("SELECT * FROM projects WHERE status = :status")
    fun getProjectsByStatus(status: String): Flow<List<Project>>

    @Query("SELECT * FROM projects LIMIT 1")
    suspend fun getFirstProject(): Project?
} 