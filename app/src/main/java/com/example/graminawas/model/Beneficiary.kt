package com.example.graminawas.model

data class Beneficiary(
    val id: String = "",
    val fullName: String = "",
    val aadharNumber: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val village: String = "",
    val district: String = "",
    val state: String = "",
    val pinCode: String = "",
    val familyMembers: Int = 0,
    val projectId: String = "",
    val status: String = "Pending" // Pending, Approved, Rejected
) 