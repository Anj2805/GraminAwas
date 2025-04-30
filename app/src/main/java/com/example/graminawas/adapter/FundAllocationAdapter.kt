package com.example.graminawas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.graminawas.data.entities.Beneficiary
import com.example.graminawas.databinding.ItemFundAllocationBinding

class FundAllocationAdapter : ListAdapter<Beneficiary, FundAllocationAdapter.FundViewHolder>(FundDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FundViewHolder {
        val binding = ItemFundAllocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FundViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FundViewHolder(
        private val binding: ItemFundAllocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(beneficiary: Beneficiary) {
            binding.apply {
                nameText.text = beneficiary.name
                aadhaarText.text = "Aadhar: ${beneficiary.aadharNumber}"
                amountText.text = "Amount: ₹${beneficiary.fundAllocated}"
                statusText.text = "Status: ${beneficiary.fundStatus}"
            }
        }
    }

    private class FundDiffCallback : DiffUtil.ItemCallback<Beneficiary>() {
        override fun areItemsTheSame(oldItem: Beneficiary, newItem: Beneficiary): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Beneficiary, newItem: Beneficiary): Boolean {
            return oldItem == newItem
        }
    }
}
