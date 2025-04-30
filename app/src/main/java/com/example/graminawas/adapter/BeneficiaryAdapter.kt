package com.example.graminawas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.graminawas.databinding.ItemBeneficiaryBinding
import com.example.graminawas.data.entities.Beneficiary

class BeneficiaryAdapter(
    private val onItemClick: (Beneficiary) -> Unit
) : ListAdapter<Beneficiary, BeneficiaryAdapter.BeneficiaryViewHolder>(BeneficiaryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeneficiaryViewHolder {
        val binding = ItemBeneficiaryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BeneficiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BeneficiaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BeneficiaryViewHolder(
        private val binding: ItemBeneficiaryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(beneficiary: Beneficiary) {
            binding.apply {
                tvName.text = beneficiary.name
                tvAadhar.text = "Aadhar: ${beneficiary.aadharNumber}"
                tvPhone.text = "Phone: ${beneficiary.phoneNumber}"
                tvAddress.text = beneficiary.address
            }
        }
    }

    private class BeneficiaryDiffCallback : DiffUtil.ItemCallback<Beneficiary>() {
        override fun areItemsTheSame(oldItem: Beneficiary, newItem: Beneficiary): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Beneficiary, newItem: Beneficiary): Boolean {
            return oldItem == newItem
        }
    }
}
