package com.example.graminawas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.graminawas.databinding.ActivityAllocateFundBinding

class AllocateFundActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllocateFundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllocateFundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupClickListeners() {
        binding.btnAllocate.setOnClickListener {
            val amount = binding.etAmount.text.toString()
            val description = binding.etDescription.text.toString()
            
            if (amount.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: Implement fund allocation logic
            Toast.makeText(this, "Fund allocated successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
