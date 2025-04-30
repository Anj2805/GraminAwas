package com.example.graminawas

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graminawas.adapter.BeneficiaryAdapter
import com.example.graminawas.data.entities.Beneficiary
import com.example.graminawas.databinding.ActivityViewBeneficiaryBinding
import com.example.graminawas.viewmodel.BeneficiaryViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ViewBeneficiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewBeneficiaryBinding
    private var currentBeneficiary: Beneficiary? = null
    private lateinit var adapter: BeneficiaryAdapter
    private val viewModel: BeneficiaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBeneficiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        // Get beneficiary ID from intent
        val beneficiaryId = intent.getStringExtra("beneficiary_id")
        if (beneficiaryId != null) {
            loadBeneficiaryDetails(beneficiaryId)
        }
    }

    private fun setupRecyclerView() {
        adapter = BeneficiaryAdapter { beneficiary ->
            // Handle beneficiary click
            currentBeneficiary = beneficiary
            updateUI(beneficiary)
            binding.scrollView.smoothScrollTo(0, 0)
        }
        binding.rvBeneficiaries.layoutManager = LinearLayoutManager(this)
        binding.rvBeneficiaries.adapter = adapter
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.allBeneficiaries.collect { beneficiaries ->
                adapter.submitList(beneficiaries)
            }
        }
    }

    private fun setupClickListeners() {
        binding.fabEdit.setOnClickListener {
            currentBeneficiary?.let { beneficiary ->
                val intent = Intent(this, AddBeneficiaryActivity::class.java).apply {
                    putExtra("beneficiary_id", beneficiary.id)
                    putExtra("beneficiary_name", beneficiary.name)
                    putExtra("beneficiary_aadhar", beneficiary.aadharNumber)
                    putExtra("beneficiary_phone", beneficiary.phoneNumber)
                    putExtra("beneficiary_address", beneficiary.address)
                    putExtra("beneficiary_village", beneficiary.village)
                    putExtra("beneficiary_district", beneficiary.district)
                    putExtra("beneficiary_state", beneficiary.state)
                    putExtra("beneficiary_pincode", beneficiary.pinCode)
                    putExtra("beneficiary_family_members", beneficiary.familyMembers)
                    putExtra("is_edit", true)
                }
                startActivity(intent)
            } ?: run {
                Toast.makeText(this, "Please select a beneficiary first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadBeneficiaryDetails(beneficiaryId: String) {
        lifecycleScope.launch {
            viewModel.getBeneficiaryById(beneficiaryId).collect { beneficiary ->
                beneficiary?.let {
                    currentBeneficiary = it
                    updateUI(it)
                } ?: run {
                    Toast.makeText(this@ViewBeneficiaryActivity, "Beneficiary not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun updateUI(beneficiary: Beneficiary) {
        // Personal Information
        binding.tvFullName.text = beneficiary.name
        binding.tvStatus.text = beneficiary.status
        binding.tvAadhar.text = beneficiary.aadharNumber
        binding.tvPhone.text = beneficiary.phoneNumber
        binding.tvFamilyMembers.text = beneficiary.familyMembers.toString()

        // Address Information
        binding.tvAddress.text = beneficiary.address
        binding.tvVillage.text = beneficiary.village
        binding.tvDistrict.text = beneficiary.district
        binding.tvState.text = beneficiary.state
        binding.tvPinCode.text = beneficiary.pinCode

        // Fund Information
        binding.tvFundAllocated.text = "₹${beneficiary.fundAllocated}"
        binding.tvFundStatus.text = beneficiary.fundStatus
        binding.tvHouseStatus.text = beneficiary.houseStatus

        // Show the details section
        binding.detailsSection.visibility = android.view.View.VISIBLE
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}