package com.example.graminawas.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "description")
    val description: String,
    
    @ColumnInfo(name = "location")
    val location: String,
    
    @ColumnInfo(name = "startDate")
    val startDate: Date,
    
    @ColumnInfo(name = "endDate")
    val endDate: Date,
    
    @ColumnInfo(name = "status")
    val status: String,
    
    @ColumnInfo(name = "beneficiaryId")
    val beneficiaryId: String,
    
    @ColumnInfo(name = "budget")
    val budget: Double,
    
    @ColumnInfo(name = "progress")
    val progress: Int = 0
) 