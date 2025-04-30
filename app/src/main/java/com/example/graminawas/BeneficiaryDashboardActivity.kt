package com.example.graminawas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.graminawas.databinding.ActivityBeneficiaryDashboardBinding
import com.google.android.material.color.MaterialColors

class BeneficiaryDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBeneficiaryDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBeneficiaryDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Beneficiary Dashboard"
        }

        // Set up click listeners for the buttons
        binding.btnMyProjectStatus.setOnClickListener {
            startActivity(Intent(this, ProjectStatusActivity::class.java))
        }

        binding.btnFundDetails.setOnClickListener {
            startActivity(Intent(this, FundTrackingActivity::class.java))
        }

        binding.btnDeadlines.setOnClickListener {
            startActivity(Intent(this, ProjectDeadlinesActivity::class.java))
        }

        binding.btnFeedback.setOnClickListener {
            startActivity(Intent(this, FeedbackActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
