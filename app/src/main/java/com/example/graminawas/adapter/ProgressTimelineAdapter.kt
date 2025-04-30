package com.example.graminawas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.graminawas.data.entities.ProjectUpdate
import com.example.graminawas.databinding.ItemProgressTimelineBinding
import java.text.SimpleDateFormat
import java.util.*

class ProgressTimelineAdapter(
    private val onItemClick: (ProjectUpdate) -> Unit
) : ListAdapter<ProjectUpdate, ProgressTimelineAdapter.ProgressViewHolder>(ProgressDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressViewHolder {
        val binding = ItemProgressTimelineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProgressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProgressViewHolder(
        private val binding: ItemProgressTimelineBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(update: ProjectUpdate) {
            binding.apply {
                tvTitle.text = update.title
                tvDescription.text = update.description
                tvDate.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    .format(update.date)
                tvProgress.text = "${update.progress}%"

                if (update.imagePath != null) {
                    ivUpdateImage.visibility = android.view.View.VISIBLE
                    // TODO: Load image using Glide or another image loading library
                } else {
                    ivUpdateImage.visibility = android.view.View.GONE
                }
            }
        }
    }

    private class ProgressDiffCallback : DiffUtil.ItemCallback<ProjectUpdate>() {
        override fun areItemsTheSame(oldItem: ProjectUpdate, newItem: ProjectUpdate): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProjectUpdate, newItem: ProjectUpdate): Boolean {
            return oldItem == newItem
        }
    }
} 