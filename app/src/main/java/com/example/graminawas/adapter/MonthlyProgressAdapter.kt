package com.example.graminawas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.graminawas.databinding.ItemMonthlyProgressBinding
import com.example.graminawas.models.MonthlyProgress
import java.text.NumberFormat
import java.util.Locale

class MonthlyProgressAdapter : ListAdapter<MonthlyProgress, MonthlyProgressAdapter.MonthlyProgressViewHolder>(MonthlyProgressDiffCallback()) {

    private val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyProgressViewHolder {
        val binding = ItemMonthlyProgressBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MonthlyProgressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthlyProgressViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MonthlyProgressViewHolder(
        private val binding: ItemMonthlyProgressBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(progress: MonthlyProgress) {
            binding.apply {
                tvMonth.text = progress.month
                tvProjectsCompleted.text = "Completed: ${progress.projectsCompleted}"
                tvProjectsStarted.text = "Started: ${progress.projectsStarted}"
                tvExpenditure.text = "Expenditure: ${formatter.format(progress.totalExpenditure)}"
            }
        }
    }

    private class MonthlyProgressDiffCallback : DiffUtil.ItemCallback<MonthlyProgress>() {
        override fun areItemsTheSame(oldItem: MonthlyProgress, newItem: MonthlyProgress): Boolean {
            return oldItem.month == newItem.month
        }

        override fun areContentsTheSame(oldItem: MonthlyProgress, newItem: MonthlyProgress): Boolean {
            return oldItem == newItem
        }
    }
} 