package com.techmave.fisherville.view.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query
import com.google.gson.Gson
import com.techmave.fisherville.R
import com.techmave.fisherville.adapter.MarketAdapter
import com.techmave.fisherville.databinding.ActivityDashboardBinding
import com.techmave.fisherville.listener._FragmentListener
import com.techmave.fisherville.model.Market
import com.techmave.fisherville.model.Transaction
import com.techmave.fisherville.model.User
import com.techmave.fisherville.service.WeatherService
import com.techmave.fisherville.util.Constants
import com.techmave.fisherville.util.SharedPrefs
import com.techmave.fisherville.util.Utility
import com.techmave.fisherville.view.dialog.CustomDialog
import com.techmave.fisherville.view.dialog.TransactionDialog
import com.techmave.fisherville.viewmodel.DashboardViewModel

class DashboardActivity : AppCompatActivity(), (Int) -> Unit, _FragmentListener, TransactionDialog.TransactionListener, MarketAdapter.MarketItemClickListener {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    private var prefs: SharedPrefs? = null
    private var transactionDialog: TransactionDialog? = null

    private var fragments = mutableListOf<Fragment>()
    private var fishes = mutableListOf<Market>()

    private var doubleBackPressed = false

    companion object {

        @Volatile
        var isTransactionVisible = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initialize()
    }

    override fun onResume() {

        super.onResume()

        updateBasicInfo()
        startWeatherService()
    }

    override fun onBackPressed() {

        if (doubleBackPressed) {

            super.onBackPressed()

        } else {

            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({ doubleBackPressed = false }, 2000)
        }
    }

    private fun initialize() {

        setSupportActionBar(binding.toolbar)

//        fragments.add(MarketFragment.getInstance())
//        fragments.add(ProfileFragment.getInstance())

        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        transactionDialog = TransactionDialog(this)
        prefs = SharedPrefs(this)

        if (prefs?.userNumber != null) {

            viewModel.setLiveData(prefs?.userNumber!!)
        }

        binding.bottomNavigation.onItemSelected = this
        loadFragment(getFragment(0))

        checkLocationPermission()

        prefs?.getPrefs()?.registerOnSharedPreferenceChangeListener { _, key ->

            if (key == Constants.PREF_LOCATION_LAT || key == Constants.PREF_LOCATION_LON) {

                startWeatherService()
            }
        }

        startWeatherService()
        setupFishObserver()
    }

    private fun updateBasicInfo() {

        viewModel.getUserProfile()?.observe(this, {

            if (it != null) {

                val user = it.getValue(User::class.java)

                if (user != null) {

                    prefs?.userName = user.name
                    prefs?.userType = user.type
                }
            }
        })
    }

    private fun getFragment(position: Int): Fragment {

        return fragments[position]
    }

    private fun loadFragment(fragment: Fragment) {

        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
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

                if (Utility.isGPSEnabled(this@DashboardActivity)) {

                    checkLocationPermission()

                    val currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                    val lat = currentLocation?.latitude
                    val lon = currentLocation?.longitude

                    if (lat != null && lon != null) {

                        prefs?.lat = lat.toFloat()
                        prefs?.lon = lon.toFloat()
                    }

//                    Log.d("Location:Gps", "Def Lat: ${location.latitude}, My Lat: $lat")

                } else {

                    val currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                    val lat = currentLocation?.latitude
                    val lon = currentLocation?.longitude

                    if (lat != null && lon != null) {

                        prefs?.lat = lat.toFloat()
                        prefs?.lon = lon.toFloat()
                    }
                }
            }

            override fun onProviderDisabled(provider: String) {}
            override fun onProviderEnabled(provider: String) {}

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60 * 60 * 1000, 0f, listener)
    }

    private fun startWeatherService() {

        if (System.currentTimeMillis() - (prefs?.lastWeatherFetched ?: 0L) > 0) {

            if (prefs?.lat != 0f && prefs?.lon != 0f) {

                val intent = Intent()

                intent.putExtra(Constants.INTENT_FLAG_LAT, prefs?.lat)
                intent.putExtra(Constants.INTENT_FLAG_LON, prefs?.lon)

                WeatherService.enqueueWork(application, intent)
            }

            prefs?.lastWeatherFetched = System.currentTimeMillis()
        }
    }

    private fun setupFishObserver() {

        viewModel.getMarketItems("", 200).observe(this, {

            if (it != null) {

                val items = mutableListOf<Market>()

                for (item in it.children) {

                    val market = item.getValue(Market::class.java)

                    if (market != null) {

                        items.add(market)
                    }
                }

                fishes.clear()
                fishes.addAll(items)
            }
        })
    }

    private fun showTransactionDialog(items: List<Market>) {

        if (transactionDialog == null) {

            transactionDialog = TransactionDialog(this)
        }

        if (transactionDialog?.isVisible == true || transactionDialog?.isAdded == true) {

            transactionDialog?.dismiss()
        }

        transactionDialog?.items = items
        isTransactionVisible = true
        transactionDialog?.show(supportFragmentManager, "transaction")
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

    override fun invoke(position: Int) {

        loadFragment(getFragment(position))
    }

    override fun getGreetingsMessage(): String {

        return "${Utility.getGreetingMessage()}, ${prefs?.userName}"
    }

    override fun getNews(): Query {

        return viewModel.getNews()
    }

    override fun getAllNews(): LiveData<DataSnapshot?> {

        return viewModel.getAllNews()
    }

    override fun getMarketItems(id: String, limit: Int): LiveData<DataSnapshot?> {

        return viewModel.getMarketItems(id, limit)
    }

    override fun getDailyTransactions(): LiveData<DataSnapshot?>? {

        return viewModel.getDailyTransactions()
    }

    override fun onTransactionAddClicked() {

        showTransactionDialog(fishes)
    }

    override fun onWeatherButtonClicked() {

        val intent = Intent(this, WeatherActivity::class.java)
        startActivity(intent)
    }

    override fun onSignOutClicked() {

        val dialog = CustomDialog()

        dialog.title = getString(R.string.warning)
        dialog.message = getString(R.string.sign_out_msg)
        dialog.enableConfirmButton = true

        dialog.listener = object : CustomDialog.OnClickListener {

            override fun onConfirmed() {

                val user = FirebaseAuth.getInstance().currentUser

                if (user != null) {

                    FirebaseAuth.getInstance().signOut()
                }

                prefs?.loggedIn = false

                finishAffinity()
                startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
            }
        }

        dialog.show(supportFragmentManager, "dialog")
    }

    override fun onTransactionAdded(transaction: Transaction) {

        isTransactionVisible = false
        viewModel.addNewTransaction(prefs?.userNumber ?: "", transaction)
    }

//    override fun onClicked(news: News) {
//
//        val json = Gson().toJson(news)
//        val intent = Intent(this, NewsActivity::class.java)
//
//        intent.putExtra(Constants.INTENT_DATA_NEWS, json)
//        startActivity(intent)
//
//        viewModel.updateNewsClickCount(news)
//    }

    override fun onMarketItemClicked(market: Market) {

        val json = Gson().toJson(market)
        val intent = Intent(this, FishActivity::class.java)

        intent.putExtra(Constants.INTENT_DATA_FISH, json)
        startActivity(intent)
    }
}
