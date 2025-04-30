package com.example.graminawas

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.graminawas.adapter.ProgressTimelineAdapter
import com.example.graminawas.data.entities.ProjectUpdate
import com.example.graminawas.databinding.ActivityTrackProgressBinding
import com.example.graminawas.viewmodel.ProjectViewModel
import com.example.graminawas.viewmodel.ViewModelFactory
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.text.SimpleDateFormat
import java.util.*

class TrackProgressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackProgressBinding
    private lateinit var viewModel: ProjectViewModel
    private lateinit var adapter: ProgressTimelineAdapter
    private var projectId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        projectId = intent.getStringExtra("projectId") ?: ""
        if (projectId.isEmpty()) {
            finish()
            return
        }

        setupToolbar()
        setupViewModel()
        setupRecyclerView()
        setupSwipeRefresh()
        setupFab()
        observeProject()
        observeUpdates()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Track Progress"
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(application)).get(ProjectViewModel::class.java)
    }

    private fun setupRecyclerView() {
        adapter = ProgressTimelineAdapter { update ->
            // Handle update click if needed
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@TrackProgressActivity)
            adapter = this@TrackProgressActivity.adapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshProject(projectId)
        }
    }

    private fun setupFab() {
        binding.fabAddUpdate.setOnClickListener {
            val intent = Intent(this, AddProgressUpdateActivity::class.java).apply {
                putExtra("projectId", projectId)
            }
            startActivity(intent)
        }
    }

    private fun observeProject() {
        viewModel.getProjectById(projectId).observe(this) { project ->
            project?.let {
                binding.tvProjectName.text = it.name
                binding.tvProjectDescription.text = it.description
                binding.tvProjectStatus.text = it.status
                binding.tvProjectProgress.text = "${it.progress}%"
            }
        }
    }

    private fun observeUpdates() {
        viewModel.getProjectUpdates(projectId).observe(this) { updates ->
            adapter.submitList(updates)
            updateProgressChart(updates)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun updateProgressChart(updates: List<ProjectUpdate>) {
        val entries = mutableListOf<Entry>()
        val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
        
        updates.sortedBy { it.date }.forEachIndexed { index, update ->
            entries.add(Entry(index.toFloat(), update.progress.toFloat()))
        }

        val dataSet = LineDataSet(entries, "Progress").apply {
            color = resources.getColor(R.color.colorPrimary, theme)
            valueTextColor = resources.getColor(R.color.colorPrimary, theme)
            lineWidth = 2f
            setDrawCircles(true)
            setDrawValues(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        binding.progressChart.apply {
            data = LineData(dataSet)
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            axisRight.isEnabled = false
            xAxis.isEnabled = false
            animateY(1000)
            invalidate()
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
