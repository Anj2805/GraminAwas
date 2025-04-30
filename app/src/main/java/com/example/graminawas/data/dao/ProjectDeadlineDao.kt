package com.example.graminawas.data.dao

import androidx.room.*
import com.example.graminawas.data.entities.ProjectDeadline
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDeadlineDao {
    @Query("SELECT * FROM project_deadlines")
    fun getAllDeadlines(): Flow<List<ProjectDeadline>>

    @Query("SELECT * FROM project_deadlines WHERE projectId = :projectId")
    fun getDeadlinesByProject(projectId: String): Flow<List<ProjectDeadline>>

    @Query("SELECT * FROM project_deadlines WHERE id = :id")
    suspend fun getDeadlineById(id: String): ProjectDeadline?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deadline: ProjectDeadline)

    @Update
    suspend fun update(deadline: ProjectDeadline)

    @Delete
    suspend fun delete(deadline: ProjectDeadline)

    @Query("SELECT * FROM project_deadlines WHERE status = :status")
    fun getDeadlinesByStatus(status: String): Flow<List<ProjectDeadline>>
}
