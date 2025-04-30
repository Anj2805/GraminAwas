package com.example.graminawas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.graminawas.R
import com.example.graminawas.models.Transaction
import com.example.graminawas.models.TransactionStatus
import com.example.graminawas.models.TransactionType
import com.example.graminawas.utils.ColorUtils
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionAdapter : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

        fun bind(transaction: Transaction) {
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            tvDate.text = dateFormat.format(transaction.date)
            tvDescription.text = transaction.description
            tvAmount.text = "₹${transaction.amount}"
            tvStatus.text = transaction.status.name

            // Set text color based on transaction type
            tvAmount.setTextColor(ColorUtils.getTransactionTypeColor(itemView.context, transaction.type))
            
            // Set status color
            tvStatus.setTextColor(ColorUtils.getTransactionStatusColor(itemView.context, transaction.status))
        }
    }

    private class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }
} 