package com.example.graminawas

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.graminawas.databinding.ActivityLoginBinding
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val roles = arrayOf("Select Role", "Admin", "Contractor", "Beneficiary")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup role dropdown
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        (binding.roleSpinner as MaterialAutoCompleteTextView).setAdapter(adapter)

        // Set default selection
        (binding.roleSpinner as MaterialAutoCompleteTextView).setText(roles[0], false)

        binding.loginButton.setOnClickListener {
            val userRole = binding.roleSpinner.text.toString().trim()
            val user = binding.usernameInput.text.toString().trim()
            val pass = binding.passwordInput.text.toString().trim()

            if (user.isNotEmpty() && pass.isNotEmpty() && userRole != "Select Role") {
                when (userRole) {
                    "Admin" -> startActivity(Intent(this, AdminDashboardActivity::class.java))
                    "Contractor" -> startActivity(Intent(this, ContractorDashboardActivity::class.java))
                    "Beneficiary" -> startActivity(Intent(this, BeneficiaryDashboardActivity::class.java))
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
