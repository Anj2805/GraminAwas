package com.example.graminawas.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "project_updates")
data class ProjectUpdate(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: String,
    val title: String,
    val description: String,
    val progress: Int,
    val imagePath: String?,
    val date: Date
) 