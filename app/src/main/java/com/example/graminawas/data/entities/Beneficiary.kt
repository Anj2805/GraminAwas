package com.example.graminawas.data.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "beneficiaries")
data class Beneficiary(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "aadharNumber")
    val aadharNumber: String,
    
    @ColumnInfo(name = "phoneNumber")
    val phoneNumber: String,
    
    @ColumnInfo(name = "address")
    val address: String,
    
    @ColumnInfo(name = "village")
    val village: String,
    
    @ColumnInfo(name = "district")
    val district: String,
    
    @ColumnInfo(name = "state")
    val state: String,
    
    @ColumnInfo(name = "pinCode")
    val pinCode: String,
    
    @ColumnInfo(name = "familyMembers")
    val familyMembers: Int,
    
    @ColumnInfo(name = "status")
    val status: String = "Active",
    
    @ColumnInfo(name = "fundAllocated")
    val fundAllocated: Double = 0.0,
    
    @ColumnInfo(name = "fundStatus")
    val fundStatus: String = "Pending",
    
    @ColumnInfo(name = "assignedContractor")
    val assignedContractor: String? = null,
    
    @ColumnInfo(name = "latitude")
    val latitude: Double? = null,
    
    @ColumnInfo(name = "longitude")
    val longitude: Double? = null,
    
    @ColumnInfo(name = "houseStatus")
    val houseStatus: String = "Not Started"
) : Parcelable
