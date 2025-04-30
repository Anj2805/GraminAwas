package com.example.graminawas

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.graminawas.databinding.ActivityAddBeneficiaryBinding
import com.example.graminawas.data.entities.Beneficiary
import com.example.graminawas.data.AppDatabase
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class AddBeneficiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBeneficiaryBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBeneficiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up add button click listener
        binding.btnAddBeneficiary.setOnClickListener {
            if (validateInputs()) {
                addBeneficiary()
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Validate Full Name
        if (binding.etFullName.text.toString().trim().isEmpty()) {
            binding.etFullName.error = "Full name is required"
            isValid = false
        }

        // Validate Aadhar Number
        val aadhar = binding.etAadhar.text.toString().trim()
        if (aadhar.isEmpty()) {
            binding.etAadhar.error = "Aadhar number is required"
            isValid = false
        } else if (aadhar.length != 12) {
            binding.etAadhar.error = "Aadhar number must be 12 digits"
            isValid = false
        }

        // Validate Phone Number
        val phone = binding.etPhone.text.toString().trim()
        if (phone.isEmpty()) {
            binding.etPhone.error = "Phone number is required"
            isValid = false
        } else if (phone.length != 10) {
            binding.etPhone.error = "Phone number must be 10 digits"
            isValid = false
        }

        // Validate Address
        if (binding.etAddress.text.toString().trim().isEmpty()) {
            binding.etAddress.error = "Address is required"
            isValid = false
        }

        // Validate Village
        if (binding.etVillage.text.toString().trim().isEmpty()) {
            binding.etVillage.error = "Village is required"
            isValid = false
        }

        // Validate District
        if (binding.etDistrict.text.toString().trim().isEmpty()) {
            binding.etDistrict.error = "District is required"
            isValid = false
        }

        // Validate State
        if (binding.etState.text.toString().trim().isEmpty()) {
            binding.etState.error = "State is required"
            isValid = false
        }

        // Validate PIN Code
        val pinCode = binding.etPinCode.text.toString().trim()
        if (pinCode.isEmpty()) {
            binding.etPinCode.error = "PIN code is required"
            isValid = false
        } else if (pinCode.length != 6) {
            binding.etPinCode.error = "PIN code must be 6 digits"
            isValid = false
        }

        // Validate Family Members
        val familyMembers = binding.etFamilyMembers.text.toString().trim()
        if (familyMembers.isEmpty()) {
            binding.etFamilyMembers.error = "Number of family members is required"
            isValid = false
        } else if (familyMembers.toIntOrNull() == null || familyMembers.toInt() <= 0) {
            binding.etFamilyMembers.error = "Please enter a valid number"
            isValid = false
        }

        return isValid
    }

    private fun addBeneficiary() {
        val beneficiary = Beneficiary(
            id = UUID.randomUUID().toString(),
            name = binding.etFullName.text.toString().trim(),
            aadharNumber = binding.etAadhar.text.toString().trim(),
            phoneNumber = binding.etPhone.text.toString().trim(),
            address = binding.etAddress.text.toString().trim(),
            village = binding.etVillage.text.toString().trim(),
            district = binding.etDistrict.text.toString().trim(),
            state = binding.etState.text.toString().trim(),
            pinCode = binding.etPinCode.text.toString().trim(),
            familyMembers = binding.etFamilyMembers.text.toString().trim().toInt()
        )

        CoroutineScope(Dispatchers.Main).launch {
            try {
                binding.btnAddBeneficiary.isEnabled = false
                withContext(Dispatchers.IO) {
                    database.beneficiaryDao().insert(beneficiary)
                }
                Snackbar.make(binding.root, 
                    "Beneficiary added successfully", 
                    Snackbar.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@AddBeneficiaryActivity, 
                    "Error: ${e.message}", 
                    Toast.LENGTH_SHORT).show()
            } finally {
                binding.btnAddBeneficiary.isEnabled = true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
