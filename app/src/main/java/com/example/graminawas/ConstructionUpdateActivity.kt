package com.example.graminawas

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ConstructionUpdateActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var imgPreview: ImageView
    private lateinit var tvLocation: TextView
    private lateinit var etStatus: EditText
    private var imageUri: Uri? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var map: GoogleMap? = null

    // Image picker launcher
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            imgPreview.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_construction_update)

        imgPreview = findViewById(R.id.imgPreview)
        tvLocation = findViewById(R.id.tvLocation)
        etStatus = findViewById(R.id.etStatus)

        findViewById<Button>(R.id.btnUploadPhoto).setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        findViewById<Button>(R.id.btnSubmitUpdate).setOnClickListener {
            val status = etStatus.text.toString().trim()
            if (status.isNotEmpty() && imageUri != null && latitude != null && longitude != null) {
                // Save to DB or send to backend
                Toast.makeText(this, "Update Submitted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Initialize map fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                latitude = it.latitude
                longitude = it.longitude
                tvLocation.text = "Location: $latitude, $longitude"

                val currentLatLng = LatLng(latitude!!, longitude!!)
                map?.let { googleMap ->
                    googleMap.clear()
                    googleMap.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            } ?: run {
                tvLocation.text = "Location not available"
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // If location already available before map loads
        if (latitude != null && longitude != null) {
            val currentLatLng = LatLng(latitude!!, longitude!!)
            googleMap.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}
