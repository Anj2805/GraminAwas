package com.example.graminawas

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.graminawas.data.AppDatabase
import com.example.graminawas.data.entities.ProjectDeadline
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ProjectDeadlinesActivity : AppCompatActivity() {

    private lateinit var etBeneficiaryName: EditText
    private lateinit var tvDate: TextView
    private lateinit var spinnerStatus: Spinner
    private lateinit var btnSave: Button
    private lateinit var listView: ListView

    private var selectedDateInMillis: Long = 0L
    private lateinit var database: AppDatabase
    private lateinit var adapter: ArrayAdapter<String>
    private val deadlineList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_deadlines)

        etBeneficiaryName = findViewById(R.id.etBeneficiaryName)
        tvDate = findViewById(R.id.tvDate)
        spinnerStatus = findViewById(R.id.spinnerStatus)
        btnSave = findViewById(R.id.btnSave)
        listView = findViewById(R.id.listView)

        database = AppDatabase.getDatabase(this)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, deadlineList)
        listView.adapter = adapter

        val statusOptions = arrayOf("On Track", "Overdue", "Delayed")
        spinnerStatus.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statusOptions)

        tvDate.setOnClickListener {
            showDatePicker()
        }

        btnSave.setOnClickListener {
            saveDeadline()
        }

        loadDeadlines()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                selectedDateInMillis = calendar.timeInMillis
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                tvDate.text = sdf.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun saveDeadline() {
        val name = etBeneficiaryName.text.toString().trim()
        val status = spinnerStatus.selectedItem.toString()

        if (name.isEmpty() || selectedDateInMillis == 0L) {
            Toast.makeText(this, "Please enter name and date", Toast.LENGTH_SHORT).show()
            return
        }

        val deadline = ProjectDeadline(
            id = UUID.randomUUID().toString(),
            projectId = "", // You'll need to get this from somewhere
            title = "Project Deadline",
            description = "Deadline for $name",
            dueDate = Date(selectedDateInMillis),
            beneficiaryName = name,
            status = status
        )

        lifecycleScope.launch {
            try {
                database.projectDeadlineDao().insert(deadline)
                runOnUiThread {
                    Toast.makeText(this@ProjectDeadlinesActivity, "Saved", Toast.LENGTH_SHORT).show()
                    etBeneficiaryName.text.clear()
                    tvDate.text = "Select date"
                    selectedDateInMillis = 0L
                    loadDeadlines()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@ProjectDeadlinesActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadDeadlines() {
        lifecycleScope.launch {
            try {
                database.projectDeadlineDao().getAllDeadlines().collect { deadlines ->
                    deadlineList.clear()
                    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    deadlineList.addAll(deadlines.map {
                        "${it.beneficiaryName} - Due: ${sdf.format(it.dueDate)} - ${it.status}"
                    })
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProjectDeadlinesActivity, "Error loading deadlines: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
