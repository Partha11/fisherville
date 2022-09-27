package com.techmave.fisherville.view.fragment.dashboard.news

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.gson.Gson
import com.techmave.fisherville.R
import com.techmave.fisherville.adapter.NewsAdapter
import com.techmave.fisherville.databinding.FragmentNewsBinding
import com.techmave.fisherville.model.News
import com.techmave.fisherville.model.OpenWeather
import com.techmave.fisherville.util.Constants
import com.techmave.fisherville.util.SharedPrefs
import com.techmave.fisherville.util.Utility
import com.techmave.fisherville.util.extension.LifeCycleExtensions.observeOnce
import com.techmave.fisherville.view.fragment.BaseFragment
import com.techmave.fisherville.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment @Inject constructor(): BaseFragment<FragmentNewsBinding, NewsViewModel>(FragmentNewsBinding::inflate) {

    override val viewModel: NewsViewModel by viewModels()

    @Inject
    lateinit var prefs: SharedPrefs

    @Inject
    lateinit var adapter: NewsAdapter

    private var updateCount = 0
    private var lastUpdate = 0L

    override fun onResume() {

        super.onResume()

        if (prefs.weatherData != "") {

            updateWeatherUi(gson.fromJson(prefs.weatherData, OpenWeather::class.java))
        }
    }

    override fun initialize() {

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

//        binding.collapsingToolbar.title = "News"
//
//        binding.collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
//        binding.collapsingToolbar.setCollapsedTitleTextColor(Color.rgb(255, 255, 255))

//        binding.newsWeatherReportText.setOnClickListener(this)

        binding.newsRecycler.adapter = adapter

        adapter.listener = NewsAdapter.OnClickListener {

            findNavController().navigate(NewsFragmentDirections.actionNewsToDetails(gson.toJson(it)))
        }

        viewModel.news.observe(viewLifecycleOwner) { adapter.submitList(it) }

        viewModel.getNews(0, 50).addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                snapshot.getValue(News::class.java)?.let { news ->

                    viewModel.news.observeOnce(viewLifecycleOwner) { list ->

                        if (!list.contains(news)) {

                            viewModel.news.value = (viewModel.news.value?.plus(news) ?: mutableListOf(news)) as MutableList<News>?
                        }
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                viewModel.news.observeOnce(viewLifecycleOwner) { list ->

                    snapshot.getValue(News::class.java)?.let { news ->

                        val item = list.find { it.id == news.id }
                        val index = list.indexOf(item)

                        viewModel.news.value?.set(index, news).also { adapter.notifyItemChanged(index) }
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

                viewModel.news.observeOnce(viewLifecycleOwner) { list ->

                    snapshot.getValue(News::class.java)?.let { news ->

                        val index = list.indexOf(news)

                        viewModel.news.value?.remove(news).also { adapter.notifyItemRemoved(index) }
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onCancelled(error: DatabaseError) { }
        })

//        setupWeatherListener()
//        updateUi()

        prefs.getPrefs()?.registerOnSharedPreferenceChangeListener { _, key ->

            if (key == Constants.PREF_WEATHER_DATA) {

                if (prefs.weatherData.isNotEmpty()) {

                    updateWeatherUi(Gson().fromJson(prefs.weatherData, OpenWeather::class.java))
                }
            }
        }
    }

    private fun setupNewsListener() {

        updateCount = 0

//        viewModel.getNews(0, 50).addValueEventListener(object: ValueEventListener {
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                Log.d("Data", snapshot.toString())
//
//                try {
//
//                    adapter?.items?.clear()
//                    adapter?.notifyDataSetChanged()
//
//                    for (data in snapshot.children) {
//
//                        val item = data.getValue(News::class.java)
//                        Log.d("Data", Gson().toJson(item))
//
//                        if (item != null) {
//
//                            adapter?.items?.add(0, item)
//                            adapter?.notifyItemInserted(0)
//
//                            if (item.timestamp >= (System.currentTimeMillis() - (60 * 60 * 24 * 1000))) {
//
//                                updateCount++
//                            }
//
//                            lastUpdate = item.timestamp
//                            updateUi()
//                        }
//                    }
//
//                } catch (ex: Exception) {
//
//                    Log.d("Exception", ex.localizedMessage, ex)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//                Log.d("Error", error.message)
//            }
//        })

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
}