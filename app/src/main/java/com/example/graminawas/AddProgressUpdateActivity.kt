package com.example.graminawas

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.graminawas.data.entities.ProjectUpdate
import com.example.graminawas.databinding.ActivityAddProgressUpdateBinding
import com.example.graminawas.viewmodel.ProjectViewModel
import com.example.graminawas.viewmodel.ViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class AddProgressUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProgressUpdateBinding
    private lateinit var viewModel: ProjectViewModel
    private var projectId: String = ""
    private var imagePath: String? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                try {
                    val imageFile = createImageFile()
                    copyImageToInternalStorage(uri, imageFile)
                    imagePath = imageFile.absolutePath
                    binding.ivProgressImage.setImageURI(uri)
                    binding.ivProgressImage.visibility = android.view.View.VISIBLE
                } catch (e: IOException) {
                    Toast.makeText(this, "Error saving image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProgressUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        projectId = intent.getStringExtra("projectId") ?: ""
        if (projectId.isEmpty()) {
            finish()
            return
        }

        setupToolbar()
        setupViewModel()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Progress Update"
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(application)).get(ProjectViewModel::class.java)
    }

    private fun setupClickListeners() {
        binding.btnAddImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getContent.launch(intent)
        }

        binding.btnSave.setOnClickListener {
            saveProgressUpdate()
        }
    }

    private fun saveProgressUpdate() {
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()
        val progress = binding.etProgress.text.toString().toIntOrNull() ?: 0

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (progress < 0 || progress > 100) {
            Toast.makeText(this, "Progress must be between 0 and 100", Toast.LENGTH_SHORT).show()
            return
        }

        val update = ProjectUpdate(
            id = 0, // Room will auto-generate this
            projectId = projectId,
            title = title,
            description = description,
            progress = progress,
            imagePath = imagePath,
            date = Date(System.currentTimeMillis())
        )

        viewModel.addProjectUpdate(update)
        finish()
    }

    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir(null)
        return File.createTempFile(
            "update_image_${System.currentTimeMillis()}_",
            ".jpg",
            storageDir
        )
    }

    private fun copyImageToInternalStorage(uri: Uri, destinationFile: File) {
        contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(destinationFile).use { output ->
                input.copyTo(output)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
} 