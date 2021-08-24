package com.example.mapactivity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mapactivity.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var map1: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    lateinit var databaseRef: DatabaseReference
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var dbReference: DatabaseReference = database.getReference("MQ location")
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var button: Button
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        databaseRef = Firebase.database.reference
        databaseRef.addValueEventListener(getPartnerLocation())

        setupLocationClient()
        toolbar = findViewById(R.id.toolbar)
        button = findViewById(R.id.btn_request)
        text = findViewById(R.id.text_warning)


        toolbar.title = "Team Reactive Map"
        setSupportActionBar(toolbar)
        button.visibility = View.GONE


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.map_options, menu)
        return true
    }

//    private lateinit var fusedLocationClient : FusedLocationProviderClient

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map1 = googleMap
        getLocationAccess()
        getPartnerLocation()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
        }
        map1.isMyLocationEnabled = true
    }


    // Use LocationRequest to get my location and LocationCallback to keep tracking my location

    private fun getMyLocationUpdates() {
        locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation

                    databaseRef = Firebase.database.reference
                    val locationlogging = LocationLogging(location.latitude, location.longitude)
                    databaseRef.child("Peter location").setValue(locationlogging)
                        .addOnSuccessListener {
                            Log.d("Success", "Location Logeed into the data base")
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                applicationContext,
                                "Error occured while writing the locations",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    // display my current location from call back location getter
                    val latLng = LatLng(location.latitude, location.longitude)
                    map.clear()
//                    map.addMarker(MarkerOptions().position(latLng).title("peter")
//                      .icon(bitmapDescriptorFromVector(applicationContext, R.mipmap.ic_petr_round)))
//                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))



                    var markerOptionDennis = map.addMarker(MarkerOptions().position(latLng).title("Peter")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                    markerOptionDennis?.position = latLng
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
        }
    }




    // prompt the user to grant/deny access
    private fun requestLocPermissions() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), //permission in the manifest
            REQUEST_LOCATION)
    }



    /**
     * This function enables the user to change the map type
     */

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        // Change the map type based on the user's selection.
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    private fun setupLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    companion object {
        const val REQUEST_LOCATION = 1
        const val TAG = "MapsActivity"
    }


//    private fun getCurrentLocation() {
//
//        // Check if the ACCESS_FINE_LOCATION permission was granted before requesting a location
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) !=
//            PackageManager.PERMISSION_GRANTED
//        ) {
//
//            // call requestLocPermissions() if permission isn't granted
//            getMyLocationUpdates()
//        } else {
//            fusedLocationClient.lastLocation.addOnCompleteListener {
//                // lastLocation is a task running in the background
//                val location = it.result //obtain location
//                //reference to the database
//
//                val database: FirebaseDatabase = FirebaseDatabase.getInstance()
//                val databaseReference: DatabaseReference = database.getReference("Peter location")
//
//                if (location != null) {
//
//                    val latLng = LatLng(location.latitude, location.longitude)
//                    // create a marker at the exact location
//                    map.addMarker(
//                        MarkerOptions().position(latLng)
//                            .title("peter is currently here!")
//                    )
//                    // create an object that will specify how the camera will be updated
//                    val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)
//
//                    map.moveCamera(update)
//                    //Save the location data to the database
//                    databaseReference.setValue(location)
//                } else {
//                    // if location is null , log an error message
//                    Log.e(TAG, "No location found")
//                }
//
//            }
//        }
//    }

    /**
     * Manage the location permission for the app
     */

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray,
    ) {

//        check if request result matches the request location
        if (requestCode == REQUEST_LOCATION) {

//            check if grantResult contains permission granted else call getCurrentLocation
            if (grantResults.size == 1 && grantResults[0] == (PackageManager.PERMISSION_GRANTED)) {
                getLocationAccess()
            } else {
//                text.visibility = View.VISIBLE
                Log.e(TAG, "Location permission has been denied")
                text.visibility = View.VISIBLE
                button.visibility =View.VISIBLE
            }
        }
    }


    /**
     * Seek for all necessary permissions from user if permission wasn't granted on app installation
     */


    private fun getPartnerLocation() : ValueEventListener {
        val logListner = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {


                    val locatingPartner =
                        snapshot.child("MQ location").getValue(Location::class.java)
                    val locationLat = locatingPartner?.latitude
                    val locationLong = locatingPartner?.longitude
//                    button.setOnClickListener {
                        // check if the latitude and longitude is not null
                        if (locationLat != null && locationLong != null) {
                            // create a LatLng object from location
                            val latLng = LatLng(locationLat, locationLong)
                            //create a marker at the read location and display it on the map
                            map1.clear()
                            map1.addMarker(
                                MarkerOptions().position(latLng)
                                    .title("The user is currently here")
                                    .icon(
                                        bitmapDescriptorFromVector(
                                            this@MapsActivity,
                                            R.drawable.fred_maker_02
                                        )
                                    )
                            )
                            //specify how the map camera is updated
                            val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)
                            //update the camera with the CameraUpdate object
                            map1.moveCamera(update)
                        } else {
                            // if location is null , log an error message
                            Log.e(TAG, "user location cannot be found")
                        }

                    }
//                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    "Could not read MQ Location",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        return logListner
    }






    //get current location updates using FusedLocationProviderClient
    private fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }


    //Location access and permissions controls
    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(
                this@MapsActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            getMyLocationUpdates()
            startLocationUpdates()
        } else
            ActivityCompat.requestPermissions(
                this@MapsActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION
            )
    }

    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}

