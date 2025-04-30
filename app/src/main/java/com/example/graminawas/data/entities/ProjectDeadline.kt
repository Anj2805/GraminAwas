package com.example.graminawas.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "project_deadlines")
data class ProjectDeadline(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    
    @ColumnInfo(name = "projectId")
    val projectId: String,
    
    @ColumnInfo(name = "title")
    val title: String,
    
    @ColumnInfo(name = "description")
    val description: String,
    
    @ColumnInfo(name = "dueDate")
    val dueDate: Date,
    
    @ColumnInfo(name = "beneficiaryName")
    val beneficiaryName: String,
    
    @ColumnInfo(name = "status")
    val status: String = "Pending"
)

