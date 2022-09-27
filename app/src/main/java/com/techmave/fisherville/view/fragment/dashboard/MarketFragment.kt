package com.techmave.fisherville.view.fragment.dashboard

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.techmave.fisherville.adapter.MarketAdapter
import com.techmave.fisherville.databinding.FragmentMarketBinding
import com.techmave.fisherville.model.Market
import com.techmave.fisherville.util.extension.LifeCycleExtensions.observeOnce
import com.techmave.fisherville.view.fragment.BaseFragment
import com.techmave.fisherville.viewmodel.MarketViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MarketFragment @Inject constructor(): BaseFragment<FragmentMarketBinding, MarketViewModel>(FragmentMarketBinding::inflate) {

    override val viewModel: MarketViewModel by viewModels()

    @Inject
    lateinit var adapter: MarketAdapter

    override fun initialize() {

        binding.marketRecycler.adapter = adapter

        viewModel.markets.observe(viewLifecycleOwner) { adapter.submitList(it) }

        viewModel.getMarketItems("", 100).addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                snapshot.getValue(Market::class.java)?.let { market ->

                    viewModel.markets.observeOnce(viewLifecycleOwner) { list ->

                        if (!list.contains(market)) {

                            viewModel.markets.value = (viewModel.markets.value?.plus(market) ?: mutableListOf(market)) as MutableList<Market>?
                        }
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                viewModel.markets.observeOnce(viewLifecycleOwner) { list ->

                    snapshot.getValue(Market::class.java)?.let { markets ->

                        val item = list.find { it.id == markets.id }
                        val index = list.indexOf(item)

                        viewModel.markets.value?.set(index, markets).also { adapter.notifyItemChanged(index) }
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

                viewModel.markets.observeOnce(viewLifecycleOwner) { list ->

                    snapshot.getValue(Market::class.java)?.let { market ->

                        val index = list.indexOf(market)

                        viewModel.markets.value?.remove(market).also { adapter.notifyItemRemoved(index) }
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onCancelled(error: DatabaseError) { }
        })
    }

    private fun initializeRecyclerView() {

        binding.marketRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.marketRecycler.itemAnimator = DefaultItemAnimator()
        binding.marketRecycler.adapter = adapter
    }

//    private fun setupMarketListener() {
//
//        viewModel.getMarketItems("", 100).observe(viewLifecycleOwner) {
//
//            if (it != null) {
//
//                adapter?.items?.clear().let {
//
//                    adapter?.notifyDataSetChanged()
//                }
//
//                for (data in it.children) {
//
//                    val item = data.getValue(Market::class.java)
//
//                    if (item != null) {
//
//                        adapter?.items?.add(item)
//                        adapter?.notifyItemInserted((adapter?.itemCount ?: 1) - 1)
//
//                        Log.d("Price", Gson().toJson(item))
//                    }
//                }
//            }
//        }
//    }
}