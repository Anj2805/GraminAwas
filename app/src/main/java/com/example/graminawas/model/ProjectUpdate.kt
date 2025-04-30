package com.example.graminawas.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class ProjectUpdate(
    val title: String,
    val description: String,
    val date: Date,
    val location: String = "",
    val status: UpdateStatus = UpdateStatus.IN_PROGRESS,
    val priority: UpdatePriority = UpdatePriority.MEDIUM,
    val imageUris: List<Uri> = emptyList()
) : Parcelable {
    enum class UpdateStatus {
        IN_PROGRESS,
        COMPLETED,
        DELAYED,
        ON_HOLD
    }

    enum class UpdatePriority {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }
} 