package com.example.graminawas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.graminawas.R
import com.example.graminawas.data.entities.Beneficiary
import com.example.graminawas.viewmodel.BeneficiaryViewModel

class TrackProgressAdapter(
    private var list: List<Beneficiary>,
    private val viewModel: BeneficiaryViewModel
) : RecyclerView.Adapter<TrackProgressAdapter.ViewHolder>() {

    private val statusOptions = arrayOf("Not Started", "In Progress", "Completed")

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvLocation = view.findViewById<TextView>(R.id.tvLocation)
        val spinner = view.findViewById<Spinner>(R.id.spinnerStatus)
        val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_track_progress, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val beneficiary = list[position]
        holder.tvName.text = beneficiary.name
        holder.tvLocation.text = "${beneficiary.village}, ${beneficiary.pinCode}"

        val adapter = ArrayAdapter(
            holder.itemView.context,
            android.R.layout.simple_spinner_item,
            statusOptions
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinner.adapter = adapter
        holder.spinner.setSelection(statusOptions.indexOf(beneficiary.houseStatus))

        holder.btnUpdate.setOnClickListener {
            val selected = holder.spinner.selectedItem.toString()
            if (selected != beneficiary.houseStatus) {
                viewModel.updateBeneficiary(beneficiary.copy(houseStatus = selected))
                Toast.makeText(holder.itemView.context, "Status updated", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount() = list.size

    fun updateData(newList: List<Beneficiary>) {
        list = newList
        notifyDataSetChanged()
    }
}
