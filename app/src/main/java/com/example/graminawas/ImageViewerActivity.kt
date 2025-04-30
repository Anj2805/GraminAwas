package com.example.graminawas

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.graminawas.databinding.ActivityImageViewerBinding
import com.github.chrisbanes.photoview.PhotoViewAttacher

class ImageViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageViewerBinding
    private lateinit var photoViewAttacher: PhotoViewAttacher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        // Get image path from intent
        val imagePath = intent.getStringExtra(EXTRA_IMAGE_PATH)
        if (imagePath == null) {
            finish()
            return
        }

        // Load image with Glide
        Glide.with(this)
            .load(imagePath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.photoView)

        // Set up PhotoView
        photoViewAttacher = PhotoViewAttacher(binding.photoView)
        photoViewAttacher.setOnViewTapListener { _, _, _ ->
            toggleToolbar()
        }
    }

    private fun toggleToolbar() {
        binding.toolbar.visibility = if (binding.toolbar.visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE
        }
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

    companion object {
        const val EXTRA_IMAGE_PATH = "image_path"
    }
} 