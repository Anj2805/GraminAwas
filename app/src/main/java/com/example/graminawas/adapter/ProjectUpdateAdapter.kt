package com.example.graminawas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graminawas.R
import com.example.graminawas.databinding.ItemProjectUpdateBinding
import com.example.graminawas.model.ProjectUpdate
import java.text.SimpleDateFormat
import java.util.Locale

class ProjectUpdateAdapter(
    private val onUpdateClick: (ProjectUpdate) -> Unit,
    private val onUpdateLongClick: (ProjectUpdate) -> Unit
) : ListAdapter<ProjectUpdate, ProjectUpdateAdapter.UpdateViewHolder>(UpdateDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateViewHolder {
        val binding = ItemProjectUpdateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UpdateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpdateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UpdateViewHolder(
        private val binding: ItemProjectUpdateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onUpdateClick(getItem(position))
                }
            }

            binding.root.setOnLongClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onUpdateLongClick(getItem(position))
                }
                true
            }
        }

        fun bind(update: ProjectUpdate) {
            binding.tvUpdateTitle.text = update.title
            binding.tvUpdateDescription.text = update.description
            binding.tvUpdateDate.text = dateFormat.format(update.date)

            // Load image if available
            if (update.imageUris.isNotEmpty()) {
                binding.ivUpdateImage.visibility = android.view.View.VISIBLE
                Glide.with(binding.ivUpdateImage)
                    .load(update.imageUris.first())
                    .placeholder(R.drawable.bg_image_placeholder)
                    .error(R.drawable.bg_image_placeholder)
                    .into(binding.ivUpdateImage)
            } else {
                binding.ivUpdateImage.visibility = android.view.View.GONE
            }

            // Set status color
            val statusColor = when (update.status) {
                ProjectUpdate.UpdateStatus.IN_PROGRESS -> R.color.status_in_progress
                ProjectUpdate.UpdateStatus.COMPLETED -> R.color.status_completed
                ProjectUpdate.UpdateStatus.DELAYED -> R.color.status_delayed
                ProjectUpdate.UpdateStatus.ON_HOLD -> R.color.status_on_hold
            }
            binding.statusIndicator.setBackgroundColor(binding.root.context.getColor(statusColor))

            // Set priority indicator
            val priorityColor = when (update.priority) {
                ProjectUpdate.UpdatePriority.LOW -> R.color.priority_low
                ProjectUpdate.UpdatePriority.MEDIUM -> R.color.priority_medium
                ProjectUpdate.UpdatePriority.HIGH -> R.color.priority_high
                ProjectUpdate.UpdatePriority.CRITICAL -> R.color.priority_critical
            }
            binding.priorityIndicator.setBackgroundColor(binding.root.context.getColor(priorityColor))
        }
    }

    private class UpdateDiffCallback : DiffUtil.ItemCallback<ProjectUpdate>() {
        override fun areItemsTheSame(oldItem: ProjectUpdate, newItem: ProjectUpdate): Boolean {
            return oldItem.title == newItem.title && oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: ProjectUpdate, newItem: ProjectUpdate): Boolean {
            return oldItem == newItem
        }
    }
} 