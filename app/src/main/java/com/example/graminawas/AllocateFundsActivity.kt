package com.example.graminawas

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graminawas.adapter.BeneficiaryAdapter
import com.example.graminawas.data.entities.Beneficiary
import com.example.graminawas.databinding.ActivityAllocateFundsBinding
import com.example.graminawas.viewmodel.BeneficiaryViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AllocateFundsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllocateFundsBinding
    private lateinit var viewModel: BeneficiaryViewModel
    private lateinit var adapter: BeneficiaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllocateFundsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Allocate Funds"

        viewModel = ViewModelProvider(this)[BeneficiaryViewModel::class.java]
        adapter = BeneficiaryAdapter { beneficiary ->
            showAllocationDialog(beneficiary)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AllocateFundsActivity)
            adapter = this@AllocateFundsActivity.adapter
        }

        observeBeneficiaries()
    }

    private fun observeBeneficiaries() {
        lifecycleScope.launch {
            viewModel.allBeneficiaries.collect { beneficiaries ->
                adapter.submitList(beneficiaries)
            }
        }
    }

    private fun showAllocationDialog(beneficiary: Beneficiary) {
        val dialog = AllocationDialogFragment.newInstance(beneficiary)
        dialog.show(supportFragmentManager, "allocation_dialog")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
} 