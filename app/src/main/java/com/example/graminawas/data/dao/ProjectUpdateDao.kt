package com.example.graminawas.data.dao

import androidx.room.*
import com.example.graminawas.data.entities.ProjectUpdate
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectUpdateDao {
    @Query("SELECT * FROM project_updates WHERE projectId = :projectId ORDER BY date DESC")
    fun getUpdatesForProject(projectId: String): Flow<List<ProjectUpdate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(update: ProjectUpdate)

    @Update
    suspend fun update(update: ProjectUpdate)

    @Delete
    suspend fun delete(update: ProjectUpdate)

    @Query("SELECT * FROM project_updates WHERE id = :id")
    suspend fun getUpdateById(id: String): ProjectUpdate?
} 