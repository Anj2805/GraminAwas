package com.example.graminawas.model

data class Project(
    val id: String = "",
    val name: String = "",
    val status: String = "",
    val location: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val budget: String = "",
    val description: String = "",
    val contractorId: String = "",
    val beneficiaryId: String = ""
) 