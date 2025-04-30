package com.example.graminawas

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.graminawas.data.entities.ProjectUpdate
import java.io.Serializable

class DeleteUpdateDialog : DialogFragment() {
    private var onDeleteConfirmed: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val update = arguments?.getSerializable(ARG_UPDATE) as? ProjectUpdate
        onDeleteConfirmed = arguments?.getSerializable(ARG_CALLBACK) as? () -> Unit

        return AlertDialog.Builder(requireContext())
            .setTitle("Delete Update")
            .setMessage("Are you sure you want to delete this progress update?")
            .setPositiveButton("Delete") { _, _ ->
                onDeleteConfirmed?.invoke()
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    companion object {
        private const val ARG_UPDATE = "update"
        private const val ARG_CALLBACK = "callback"

        fun newInstance(update: ProjectUpdate, onDeleteConfirmed: () -> Unit): DeleteUpdateDialog {
            return DeleteUpdateDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_UPDATE, update as Serializable)
                    putSerializable(ARG_CALLBACK, onDeleteConfirmed as Serializable)
                }
            }
        }
    }
} 