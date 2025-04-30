package com.example.graminawas

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.graminawas.adapter.ProjectUpdateAdapter
import com.example.graminawas.model.ProjectUpdate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.app.Dialog
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.graminawas.databinding.ActivityProjectStatusBinding
import com.example.graminawas.databinding.DialogImagePreviewBinding
import com.example.graminawas.databinding.ItemProjectUpdateBinding
import com.github.chrisbanes.photoview.PhotoView

class ProjectStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProjectStatusBinding
    private lateinit var tvProjectId: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvStartDate: TextView
    private lateinit var tvEndDate: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgress: TextView
    private lateinit var rvUpdates: RecyclerView
    private lateinit var updateAdapter: ProjectUpdateAdapter
    private val updates = mutableListOf<ProjectUpdate>()
    private val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    private var lastDeletedUpdate: ProjectUpdate? = null
    private var lastDeletedPosition: Int = -1
    private val imageUris = mutableListOf<Uri>()
    private val tempImageDir by lazy { File(cacheDir, "temp_images") }

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri>? ->
        uris?.let { 
            imageUris.addAll(it)
            showImagePreviewDialog(imageUris.last())
        }
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            result.uriContent?.let { croppedUri ->
                imageUris.add(croppedUri)
                showImagePreviewDialog(croppedUri)
            }
        } else {
            Toast.makeText(this, "Image cropping failed", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val MAX_IMAGE_SIZE = 1024 // Max width/height in pixels
        private const val COMPRESSION_QUALITY = 80 // Compression quality (0-100)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize views
        tvProjectId = binding.tvProjectId
        tvStatus = binding.tvStatus
        tvStartDate = binding.tvStartDate
        tvEndDate = binding.tvEndDate
        progressBar = binding.progressBar
        tvProgress = binding.tvProgress
        rvUpdates = binding.recyclerViewUpdates

        // Setup RecyclerView with animations
        updateAdapter = ProjectUpdateAdapter(
            onUpdateClick = { showUpdateDetails(it) },
            onUpdateLongClick = { showUpdateOptions(it) }
        )
        rvUpdates.layoutManager = LinearLayoutManager(this)
        rvUpdates.adapter = updateAdapter
        rvUpdates.itemAnimator = DefaultItemAnimator()

        // Load test data
        loadTestData()

        // Create temp directory for images if it doesn't exist
        if (!tempImageDir.exists()) {
            tempImageDir.mkdirs()
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnAddImage.setOnClickListener {
            val options = CropImageContractOptions(null, CropImageOptions())
            cropImage.launch(options)
        }

        binding.btnAddUpdate.setOnClickListener {
            val title = binding.etUpdateTitle.text?.toString() ?: ""
            val description = binding.etUpdateDescription.text?.toString() ?: ""
            
            if (title.isNotEmpty() && description.isNotEmpty()) {
                val update = ProjectUpdate(
                    title = title,
                    description = description,
                    date = Date(), // Use current date
                    imageUris = imageUris.toList()
                )
                
                updates.add(update)
                updateAdapter.submitList(updates)
                
                // Clear inputs
                binding.etUpdateTitle.text?.clear()
                binding.etUpdateDescription.text?.clear()
                imageUris.clear()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showUpdateDetails(update: ProjectUpdate) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_details, null)
        
        dialogView.findViewById<TextView>(R.id.tvUpdateTitle).text = update.title
        dialogView.findViewById<TextView>(R.id.tvUpdateDescription).text = update.description
        dialogView.findViewById<TextView>(R.id.tvUpdateDate).text = dateFormat.format(update.date)
        
        MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showUpdateOptions(update: ProjectUpdate) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Update Options")
            .setItems(arrayOf("View Details", "Edit Update", "Delete Update")) { _, which ->
                when (which) {
                    0 -> showUpdateDetails(update)
                    1 -> showEditUpdateDialog(update)
                    2 -> showDeleteConfirmation(update)
                }
            }
            .show()
    }

    private fun showEditUpdateDialog(update: ProjectUpdate) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_update, null)
        val etTitle = dialogView.findViewById<TextInputEditText>(R.id.etUpdateTitle)
        val etDescription = dialogView.findViewById<TextInputEditText>(R.id.etUpdateDescription)
        val etLocation = dialogView.findViewById<TextInputEditText>(R.id.etUpdateLocation)
        val etDate = dialogView.findViewById<TextInputEditText>(R.id.etUpdateDate)
        val ivImage = dialogView.findViewById<ImageView>(R.id.ivUpdateImage)
        val btnAddImage = dialogView.findViewById<Button>(R.id.btnAddImage)
        val toggleStatus = dialogView.findViewById<com.google.android.material.button.MaterialButtonToggleGroup>(R.id.toggleStatus)
        val togglePriority = dialogView.findViewById<com.google.android.material.button.MaterialButtonToggleGroup>(R.id.togglePriority)

        etTitle.setText(update.title)
        etDescription.setText(update.description)
        etLocation.setText(update.location)
        etDate.setText(dateFormat.format(update.date))

        // Set initial status and priority
        when (update.status) {
            ProjectUpdate.UpdateStatus.IN_PROGRESS -> toggleStatus.check(R.id.btnStatusInProgress)
            ProjectUpdate.UpdateStatus.COMPLETED -> toggleStatus.check(R.id.btnStatusCompleted)
            ProjectUpdate.UpdateStatus.DELAYED -> toggleStatus.check(R.id.btnStatusDelayed)
            ProjectUpdate.UpdateStatus.ON_HOLD -> toggleStatus.check(R.id.btnStatusOnHold)
        }

        when (update.priority) {
            ProjectUpdate.UpdatePriority.LOW -> togglePriority.check(R.id.btnPriorityLow)
            ProjectUpdate.UpdatePriority.MEDIUM -> togglePriority.check(R.id.btnPriorityMedium)
            ProjectUpdate.UpdatePriority.HIGH -> togglePriority.check(R.id.btnPriorityHigh)
            ProjectUpdate.UpdatePriority.CRITICAL -> togglePriority.check(R.id.btnPriorityCritical)
        }

        // Load image if exists
        update.imageUris.firstOrNull()?.let { uri ->
            Glide.with(this)
                .load(uri)
                .placeholder(R.drawable.bg_image_placeholder)
                .error(R.drawable.bg_image_placeholder)
                .into(ivImage)
        } ?: run {
            ivImage.setBackgroundResource(R.drawable.bg_image_placeholder)
        }

        btnAddImage.setOnClickListener {
            val options = CropImageContractOptions(null, CropImageOptions())
            cropImage.launch(options)
        }

        etDate.setOnClickListener {
            val calendar = Calendar.getInstance().apply { time = update.date }
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    etDate.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnSave).setOnClickListener {
            val newTitle = etTitle.text?.toString() ?: ""
            val newDescription = etDescription.text?.toString() ?: ""
            val newLocation = etLocation.text?.toString() ?: ""
            
            if (newTitle.isBlank() || newDescription.isBlank()) {
                showToast("Please fill in all required fields")
                return@setOnClickListener
            }

            val newStatus = when (toggleStatus.checkedButtonId) {
                R.id.btnStatusInProgress -> ProjectUpdate.UpdateStatus.IN_PROGRESS
                R.id.btnStatusCompleted -> ProjectUpdate.UpdateStatus.COMPLETED
                R.id.btnStatusDelayed -> ProjectUpdate.UpdateStatus.DELAYED
                R.id.btnStatusOnHold -> ProjectUpdate.UpdateStatus.ON_HOLD
                else -> update.status
            }

            val newPriority = when (togglePriority.checkedButtonId) {
                R.id.btnPriorityLow -> ProjectUpdate.UpdatePriority.LOW
                R.id.btnPriorityMedium -> ProjectUpdate.UpdatePriority.MEDIUM
                R.id.btnPriorityHigh -> ProjectUpdate.UpdatePriority.HIGH
                R.id.btnPriorityCritical -> ProjectUpdate.UpdatePriority.CRITICAL
                else -> update.priority
            }

            val index = updates.indexOfFirst { it.title == update.title && it.date == update.date }
            if (index != -1) {
                val newDate = dateFormat.parse(etDate.text?.toString() ?: "") ?: update.date
                updates[index] = update.copy(
                    title = newTitle,
                    description = newDescription,
                    location = newLocation,
                    date = newDate,
                    status = newStatus,
                    priority = newPriority,
                    imageUris = imageUris.toList()
                )
                updateAdapter.submitList(updates.toList())
                showToast("Update saved successfully")
                dialog.dismiss()
            }
        }

        // Add image preview functionality
        ivImage.setOnClickListener {
            imageUris.firstOrNull()?.let { uri ->
                showImagePreview(uri)
            }
        }

        dialog.show()
    }

    private fun showDeleteConfirmation(update: ProjectUpdate) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Update")
            .setMessage("Are you sure you want to delete this update?")
            .setPositiveButton("Delete") { _, _ ->
                val index = updates.indexOfFirst { it.title == update.title && it.date == update.date }
                if (index != -1) {
                    lastDeletedUpdate = updates[index]
                    lastDeletedPosition = index
                    updates.removeAt(index)
                    updateAdapter.submitList(updates.toList())
                    
                    // Show undo snackbar
                    Snackbar.make(rvUpdates, "Update deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            lastDeletedUpdate?.let { deletedUpdate ->
                                updates.add(lastDeletedPosition, deletedUpdate)
                                updateAdapter.submitList(updates.toList())
                                lastDeletedUpdate = null
                                lastDeletedPosition = -1
                            }
                        }
                        .show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun loadImageWithGlide(url: String, imageView: ImageView) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.bg_image_placeholder)
            .error(R.drawable.bg_image_placeholder)
            .transition(DrawableTransitionOptions.withCrossFade())
            .override(MAX_IMAGE_SIZE)
            .centerCrop()
            .into(imageView)
    }

    private fun showImagePreview(uri: Uri) {
        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        val binding = DialogImagePreviewBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        Glide.with(this)
            .load(uri)
            .into(binding.photoView)

        binding.photoView.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showImagePreviewDialog(imageUri: Uri) {
        val dialogBinding = DialogImagePreviewBinding.inflate(layoutInflater)
        val dialog = Dialog(this).apply {
            setContentView(dialogBinding.root)
            window?.setLayout(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        // Load image using Glide
        Glide.with(this)
            .load(imageUri)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(dialogBinding.photoView)

        // Set up click listeners
        dialogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnDelete.setOnClickListener {
            imageUris.remove(imageUri)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            data.data?.let { uri ->
                imageUris.add(uri)
                // Load the selected image with Glide
                Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.bg_image_placeholder)
                    .error(R.drawable.bg_image_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .override(MAX_IMAGE_SIZE)
                    .centerCrop()
                    .into(binding.ivUpdateImage)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up temp images
        tempImageDir.listFiles()?.forEach { it.delete() }
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun loadTestData() {
        // Set project details
        tvProjectId.text = "Project ID: P001"
        tvStatus.text = "Status: In Progress"
        
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val startDate = Calendar.getInstance().apply {
            set(2024, Calendar.MARCH, 1)
        }
        val endDate = Calendar.getInstance().apply {
            set(2024, Calendar.SEPTEMBER, 30)
        }
        
        tvStartDate.text = "Start Date: ${dateFormat.format(startDate.time)}"
        tvEndDate.text = "End Date: ${dateFormat.format(endDate.time)}"

        // Set progress
        val progress = 35
        progressBar.progress = progress
        tvProgress.text = "$progress% Complete"

        // Load recent updates
        updates.clear()
        updates.addAll(listOf(
            ProjectUpdate(
                "Foundation work completed",
                "The foundation work has been completed successfully. All measurements and quality checks have passed the inspection.",
                Calendar.getInstance().apply { set(2024, Calendar.MARCH, 15) }.time,
                status = ProjectUpdate.UpdateStatus.COMPLETED,
                priority = ProjectUpdate.UpdatePriority.HIGH,
                location = "Site A"
            ),
            ProjectUpdate(
                "Wall construction started",
                "Wall construction has begun as per the approved plan. The first layer of bricks has been laid and curing process has started.",
                Calendar.getInstance().apply { set(2024, Calendar.APRIL, 1) }.time,
                status = ProjectUpdate.UpdateStatus.IN_PROGRESS,
                priority = ProjectUpdate.UpdatePriority.MEDIUM,
                location = "Site B"
            ),
            ProjectUpdate(
                "First inspection completed",
                "The first inspection was conducted and all work was found satisfactory. Minor suggestions were given for improvement.",
                Calendar.getInstance().apply { set(2024, Calendar.APRIL, 15) }.time,
                status = ProjectUpdate.UpdateStatus.COMPLETED,
                priority = ProjectUpdate.UpdatePriority.LOW,
                location = "Site C"
            )
        ))
        updateAdapter.submitList(updates)
    }
} 