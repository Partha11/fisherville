package com.techmave.fisherville.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.techmave.fisherville.R
import com.techmave.fisherville.adapter.NewsAdapter
import com.techmave.fisherville.databinding.FragmentNewsBinding
import com.techmave.fisherville.listener.FragmentListener
import com.techmave.fisherville.model.News
import com.techmave.fisherville.model.OpenWeather
import com.techmave.fisherville.util.Constants
import com.techmave.fisherville.util.SharedPrefs
import com.techmave.fisherville.util.Utility

class NewsFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentNewsBinding

    private var prefs: SharedPrefs? = null
    private var adapter: NewsAdapter? = null
    private var listener: FragmentListener? = null

    private var items = mutableListOf<News>()

    private var updateCount = 0
    private var lastUpdate = 0L

    companion object {

        @JvmStatic
        fun getInstance() = NewsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentNewsBinding.inflate(inflater)
        return binding.root
    }

    override fun onAttach(context: Context) {

        super.onAttach(context)
        (activity as AppCompatActivity).supportActionBar?.hide()

        if (context is FragmentListener) {

            listener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    override fun onResume() {

        super.onResume()

        if (prefs?.weatherData != "") {

            updateWeatherUi(Gson().fromJson(prefs?.weatherData, OpenWeather::class.java))
        }
    }

    private fun initialize() {

        adapter = NewsAdapter(requireContext())
        prefs = SharedPrefs(requireContext())

//        binding.collapsingToolbar.title = "News"
//
//        binding.collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
//        binding.collapsingToolbar.setCollapsedTitleTextColor(Color.rgb(255, 255, 255))

        binding.newsWeatherReportText.setOnClickListener(this)

        setupCollapsingToolbar()
        setupRecyclerView()
        setupNewsListener()
        setupWeatherListener()
        updateUi()

        prefs?.getPrefs()?.registerOnSharedPreferenceChangeListener { _, key ->

            if (key == Constants.PREF_WEATHER_DATA) {

                if (prefs?.weatherData != "") {

                    updateWeatherUi(Gson().fromJson(prefs?.weatherData, OpenWeather::class.java))
                }
            }
        }
    }

    private fun setupRecyclerView() {

        binding.newsRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.newsRecycler.itemAnimator = DefaultItemAnimator()
        binding.newsRecycler.adapter = adapter
    }

    private fun setupCollapsingToolbar() {

        binding.appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {

            var isVisible = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {

                if (scrollRange == -1) scrollRange = appBarLayout.totalScrollRange

                if (scrollRange + verticalOffset == 0) {

                    binding.collapsingToolbar.title = "News"
                    isVisible = true

                } else if (isVisible) {

                    binding.collapsingToolbar.title = " "
                    isVisible = false
                }
            }
        })
    }

    private fun setupNewsListener() {

        updateCount = 0

        listener?.getNews()?.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                Log.d("Data", snapshot.toString())

                try {

                    adapter?.items?.clear()
                    adapter?.notifyDataSetChanged()

                    for (data in snapshot.children) {

                        val item = data.getValue(News::class.java)
                        Log.d("Data", Gson().toJson(item))

                        if (item != null) {

                            adapter?.items?.add(0, item)
                            adapter?.notifyItemInserted(0)

                            if (item.timestamp >= (System.currentTimeMillis() - (60 * 60 * 24 * 1000))) {

                                updateCount++
                            }

                            lastUpdate = item.timestamp
                            updateUi()
                        }
                    }

                } catch (ex: Exception) {

                    Log.d("Exception", ex.localizedMessage, ex)
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Log.d("Error", error.message)
            }
        })

//        listener?.getNews()?.addChildEventListener(object: ChildEventListener {
//
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//
//                Log.d("Data", snapshot.toString())
//
//                try {
//
//                    val item = snapshot.getValue(News::class.java)
//
//                    if (item != null) {
//
//                        adapter?.items?.add(0, item)
//                        adapter?.notifyItemInserted(0)
//
//                        if (item.timestamp >= (System.currentTimeMillis() - (60 * 60 * 24 * 1000))) {
//
//                            updateCount++
//                        }
//
//                        lastUpdate = item.timestamp
//                        updateUi()
//                    }
//
//                } catch (ex: Exception) {
//
//                    Log.d("Exception", ex.localizedMessage, ex)
//                }
//            }
//
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//
//                Log.d("Child:Change", previousChildName.toString())
//            }
//
//            override fun onChildRemoved(snapshot: DataSnapshot) {
//
//                Log.d("Child:Change", "removed")
//            }
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//
//                Log.d("Child:Change", previousChildName.toString())
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//                Log.d("Child:Change", error.message)
//            }
//        })
    }

    private fun setupWeatherListener() {

        prefs?.getPrefs()?.registerOnSharedPreferenceChangeListener { _, key ->

            if (key == Constants.PREF_WEATHER_DATA) {

                val data: OpenWeather? = Gson().fromJson(prefs?.weatherData, OpenWeather::class.java)

                if (data != null) {

                    updateWeatherUi(data)
                }
            }
        }
    }

    private fun updateUi() {

        val greetings = "${Utility.getGreetingMessage()}, ${Utility.getFirstPartFromName(prefs?.userName)}"
        val updateMsg = context?.resources?.getQuantityString(R.plurals.news_update, updateCount, updateCount, Utility.getTimeFromTimestamp(lastUpdate))

        binding.newsWelcomeText.text = greetings
        binding.newsUpdateCountText.text = updateMsg
    }

    private fun updateWeatherUi(data: OpenWeather) {

        val temp = Utility.convertToOneDecimalPoints(data.current?.temp ?: 0f)

        binding.newsTemperatureText.text = context?.getString(R.string.temperature_text, temp)
        binding.newsWeatherTypeText.text = Utility.convertStringToUpperCase(data.current?.weather?.get(0)?.description)
    }

    override fun onClick(v: View?) {

        listener?.onWeatherButtonClicked()
    }
}