package com.techmave.fisherville.view.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.techmave.fisherville.R
import com.techmave.fisherville.adapter.FishAdapter
import com.techmave.fisherville.databinding.ActivityFishBinding
import com.techmave.fisherville.model.Market
import com.techmave.fisherville.model.Price
import com.techmave.fisherville.util.Constants
import com.techmave.fisherville.util.SharedPrefs

class FishActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFishBinding

    private var prefs: SharedPrefs? = null
    private var adapter: FishAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityFishBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initialize()
    }

    private fun initialize() {

        setSupportActionBar(binding.toolbar)
        title = "Fish Market"

        prefs = SharedPrefs(this)
        adapter = FishAdapter(this)

        setupRecyclerView()
        retrieveData()

        if (prefs?.userType == 0L) {

            binding.fishFab.hide()

        } else {

            binding.fishFab.show()
        }
    }

    private fun retrieveData() {

        if (intent.hasExtra(Constants.INTENT_DATA_FISH)) {

            val data = intent.getStringExtra(Constants.INTENT_DATA_FISH)

            if (!data.isNullOrEmpty()) {

                val fish: Market = Gson().fromJson(data, Market::class.java)
                updateUi(fish)
            }
        }
    }

    private fun setupRecyclerView() {

        binding.fishRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.fishRecycler.itemAnimator = DefaultItemAnimator()
        binding.fishRecycler.adapter = adapter
    }

    private fun updateUi(fish: Market) {

        binding.fishName.text = fish.name
        binding.fishDescription.text = fish.summary

        if (fish.thumb.isNullOrEmpty()) {

            Picasso.get().load(R.drawable.ic_placeholder)
                    .into(binding.fishThumb)

        } else {

            Picasso.get().load(fish.thumb)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(binding.fishThumb)
        }

        createMarketPriceListener(fish.id)
    }

    private fun createMarketPriceListener(id: String) {

        val reference = FirebaseDatabase.getInstance().getReference("market")
        val priceList = mutableListOf<Price>()

        reference.child(id).child("prices").addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.children) {

                    val price = data.getValue(Price::class.java)

                    if (price != null) {

                        priceList.add(price)
                    }
                }

                adapter?.items = priceList
                adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

                Log.d("Error", error.message)
            }
        })
    }
}