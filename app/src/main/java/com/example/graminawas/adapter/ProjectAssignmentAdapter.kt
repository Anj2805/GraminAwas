package com.example.graminawas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.graminawas.databinding.ItemProjectAssignmentBinding
import com.example.graminawas.models.ProjectAssignment
import com.example.graminawas.models.ProjectStatus

class ProjectAssignmentAdapter(
    private val onItemClick: (ProjectAssignment) -> Unit,
    private val onAssignClick: (ProjectAssignment) -> Unit
) : ListAdapter<ProjectAssignment, ProjectAssignmentAdapter.ProjectViewHolder>(ProjectDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = ItemProjectAssignmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProjectViewHolder(
        private val binding: ItemProjectAssignmentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = layoutPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }

            binding.btnAssign.setOnClickListener {
                val position = layoutPosition
                if (position != RecyclerView.NO_POSITION) {
                    onAssignClick(getItem(position))
                }
            }
        }

        fun bind(project: ProjectAssignment) {
            binding.apply {
                tvBeneficiaryName.text = project.beneficiaryName
                tvAddress.text = project.address
                tvProgress.text = "${project.progressPercentage}% Complete"
                tvDeadline.text = "Deadline: ${project.deadline}"
                tvStatus.text = project.status.name
                
                // Set status color based on project status
                val statusColor = when (project.status) {
                    ProjectStatus.PENDING -> android.graphics.Color.GRAY
                    ProjectStatus.IN_PROGRESS -> android.graphics.Color.BLUE
                    ProjectStatus.COMPLETED -> android.graphics.Color.GREEN
                    ProjectStatus.DELAYED -> android.graphics.Color.RED
                }
                tvStatus.setTextColor(statusColor)

                // Show/hide assign button based on project status
                btnAssign.visibility = if (project.status == ProjectStatus.PENDING) {
                    android.view.View.VISIBLE
                } else {
                    android.view.View.GONE
                }
            }
        }
    }

    private class ProjectDiffCallback : DiffUtil.ItemCallback<ProjectAssignment>() {
        override fun areItemsTheSame(oldItem: ProjectAssignment, newItem: ProjectAssignment): Boolean {
            return oldItem.projectId == newItem.projectId
        }

        override fun areContentsTheSame(oldItem: ProjectAssignment, newItem: ProjectAssignment): Boolean {
            return oldItem == newItem
        }
    }
} 