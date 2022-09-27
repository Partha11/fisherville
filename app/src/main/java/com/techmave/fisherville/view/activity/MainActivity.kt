package com.techmave.fisherville.view.activity

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.techmave.fisherville.R
import com.techmave.fisherville.databinding.ActivityMainBinding
import com.techmave.fisherville.listener.FragmentListener
import com.techmave.fisherville.util.Constants
import com.techmave.fisherville.util.SharedPrefs
import com.techmave.fisherville.util.Utility
import com.techmave.fisherville.view.dialog.CustomDialog
import com.techmave.fisherville.view.dialog.CustomProgress
import com.techmave.fisherville.worker.WeatherWorker
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor(): AppCompatActivity(), FragmentListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var configuration: AppBarConfiguration

    @Inject
    lateinit var progressDialog: CustomProgress

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var prefs: SharedPrefs

    @Inject
    lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initialize()
    }

    override fun onResume() {

        super.onResume()

        if (auth.currentUser != null) {

            checkLocationPermission()
        }
    }

    private fun initialize() {

        setSupportActionBar(binding.toolbar)

        supportFragmentManager.findFragmentById(R.id.fragment_container)?.findNavController()?.let {

            configuration = AppBarConfiguration.Builder(R.id.fragment_login, R.id.fragment_news, R.id.fragment_market, R.id.fragment_profile).build()

            NavigationUI.setupWithNavController(binding.toolbar, it, configuration)
            binding.bottomNavigation.setupWithNavController(it)
        }

        if (auth.currentUser != null) {

            val data = Data.Builder()
                .putFloat(Constants.INTENT_FLAG_LAT, prefs.lat)
                .putFloat(Constants.INTENT_FLAG_LON, prefs.lon)
                .build()

            val request = PeriodicWorkRequestBuilder<WeatherWorker>(Duration.ofMinutes(15))
                .setInputData(data)
                .build()

            workManager.enqueueUniquePeriodicWork("location", ExistingPeriodicWorkPolicy.REPLACE, request)
        }
    }

    private fun checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= 23) {

                val permissions = listOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                ActivityCompat.requestPermissions(this, permissions.toTypedArray(), Constants.PERMISSION_CODE_LOCATION)

            } else {

                getLatLonFromLocation()
            }

        } else {

            getLatLonFromLocation()
        }
    }

    private fun getLatLonFromLocation() {

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val listener = object : LocationListener {

            override fun onLocationChanged(location: Location) {

                if (Utility.isGPSEnabled(this@MainActivity)) {

                    checkLocationPermission()

                    val currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                    val lat = currentLocation?.latitude
                    val lon = currentLocation?.longitude

                    if (lat != null && lon != null) {

                        prefs.lat = lat.toFloat()
                        prefs.lon = lon.toFloat()
                    }

//                    Log.d("Location:Gps", "Def Lat: ${location.latitude}, My Lat: $lat")

                } else {

                    val currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                    val lat = currentLocation?.latitude
                    val lon = currentLocation?.longitude

                    if (lat != null && lon != null) {

                        prefs.lat = lat.toFloat()
                        prefs.lon = lon.toFloat()
                    }
                }
            }

            override fun onProviderDisabled(provider: String) { }
            override fun onProviderEnabled(provider: String) { }

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) { }
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60 * 60 * 1000, 0f, listener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.PERMISSION_CODE_LOCATION) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLatLonFromLocation()

            } else {

                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun showProgress(title: String?, content: String?): CustomProgress {

        return progressDialog.apply {

            this.title.value = title
            this.content.value = content

            this.isCancelable = false

            if (!isAdded && !isVisible) {

                show(supportFragmentManager, "progress")
            }
        }
    }

    override fun showDialog(title: String?, content: String?, enableConfirm: Boolean): CustomDialog {
        TODO("Not yet implemented")
    }

    override fun showHideBottomNav(isVisible: Boolean) {

        binding.bottomNavigation.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}