package com.example.graminawas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.graminawas.databinding.ItemActivityBinding
import com.example.graminawas.models.ProjectActivity

class ActivityAdapter : ListAdapter<ProjectActivity, ActivityAdapter.ActivityViewHolder>(ActivityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemActivityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ActivityViewHolder(
        private val binding: ItemActivityBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(activity: ProjectActivity) {
            binding.apply {
                tvActivityType.text = activity.activityType.name
                tvTimestamp.text = activity.timestamp
                tvDescription.text = activity.description
                tvBeneficiaryName.text = activity.beneficiaryName
            }
        }
    }

    private class ActivityDiffCallback : DiffUtil.ItemCallback<ProjectActivity>() {
        override fun areItemsTheSame(oldItem: ProjectActivity, newItem: ProjectActivity): Boolean {
            return oldItem.projectId == newItem.projectId && oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: ProjectActivity, newItem: ProjectActivity): Boolean {
            return oldItem == newItem
        }
    }
} 