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
import com.example.graminawas.databinding.ActivityFundAllocationBinding
import com.example.graminawas.viewmodel.BeneficiaryViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FundAllocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFundAllocationBinding
    private lateinit var viewModel: BeneficiaryViewModel
    private lateinit var adapter: BeneficiaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFundAllocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Fund Allocation"

        viewModel = ViewModelProvider(this)[BeneficiaryViewModel::class.java]
        adapter = BeneficiaryAdapter { beneficiary ->
            showAllocationDialog(beneficiary)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FundAllocationActivity)
            adapter = this@FundAllocationActivity.adapter
        }

        observeAllocatedBeneficiaries()
    }

    private fun observeAllocatedBeneficiaries() {
        lifecycleScope.launch {
            viewModel.allocatedBeneficiaries.collect { beneficiaries: List<Beneficiary> ->
                adapter.submitList(beneficiaries)
            }
        }
    }

    private fun showAllocationDialog(beneficiary: Beneficiary) {
        val dialog = AllocationDialogFragment.newInstance(beneficiary)
        dialog.show(supportFragmentManager, "allocation_dialog")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

