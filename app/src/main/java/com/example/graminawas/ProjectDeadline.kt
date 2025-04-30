package com.example.graminawas

import androidx.room.Entity
import androidx.room.PrimaryKey

// This annotation marks the class as a Room entity
@Entity(tableName = "project_deadlines")
data class ProjectDeadline(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // autoGenerate ID for each entry
    val projectName: String,                     // Name of the project
    val expectedDate: String                    // The expected completion date (e.g., "2025-05-10")
)
