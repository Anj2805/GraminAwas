package com.example.graminawas.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {
    private const val MAX_IMAGE_SIZE = 1024 // Max width/height in pixels
    private const val COMPRESSION_QUALITY = 80 // Compression quality (0-100)
    private const val MAX_FILE_SIZE = 500 * 1024 // Max file size in bytes (500KB)

    fun compressImage(context: Context, imageUri: Uri): String? {
        return try {
            // Create a temporary file
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "JPEG_${timeStamp}_"
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)

            // Decode the image
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, options)
            }

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE)
            options.inJustDecodeBounds = false

            // Decode the image with the new options
            var bitmap: Bitmap? = null
            context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            }

            // Compress the image
            bitmap?.let { bmp ->
                FileOutputStream(imageFile).use { out ->
                    var quality = COMPRESSION_QUALITY
                    do {
                        bmp.compress(Bitmap.CompressFormat.JPEG, quality, out)
                        quality -= 10
                    } while (imageFile.length() > MAX_FILE_SIZE && quality > 0)
                }
                bitmap?.recycle()
            }

            imageFile.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun deleteImage(imagePath: String) {
        try {
            File(imagePath).delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
} 