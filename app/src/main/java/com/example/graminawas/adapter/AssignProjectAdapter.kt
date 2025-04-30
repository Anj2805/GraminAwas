package com.example.graminawas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graminawas.R
import com.example.graminawas.data.entities.Beneficiary

class AssignProjectAdapter(
    private var list: List<Beneficiary>,
    private val contractorName: String,
    private val onAssignClicked: (Beneficiary) -> Unit
) : RecyclerView.Adapter<AssignProjectAdapter.AssignViewHolder>() {

    inner class AssignViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.beneficiaryName)
        val village: TextView = itemView.findViewById(R.id.beneficiaryVillage)
        val assignBtn: Button = itemView.findViewById(R.id.assignButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_assign_project, parent, false)
        return AssignViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssignViewHolder, position: Int) {
        val beneficiary = list[position]
        holder.name.text = "Name: ${beneficiary.name}"
        holder.village.text = "Village: ${beneficiary.village}"
        holder.assignBtn.setOnClickListener {
            onAssignClicked(beneficiary)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<Beneficiary>) {
        list = newList
        notifyDataSetChanged()
    }
}
