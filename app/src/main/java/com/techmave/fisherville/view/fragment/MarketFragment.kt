package com.techmave.fisherville.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techmave.fisherville.adapter.MarketAdapter
import com.techmave.fisherville.databinding.FragmentMarketBinding
import com.techmave.fisherville.listener.FragmentListener
import com.techmave.fisherville.model.Market

class MarketFragment : Fragment() {

    private lateinit var binding: FragmentMarketBinding

    private var listener: FragmentListener? = null
    private var adapter: MarketAdapter? = null

    companion object {

        @JvmStatic
        fun getInstance() = MarketFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentMarketBinding.inflate(inflater)
        return binding.root
    }

    override fun onAttach(context: Context) {

        super.onAttach(context)

        if (context is FragmentListener) {

            listener = context
        }

        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).title = "Market"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {

        adapter = MarketAdapter()

        initializeRecyclerView()
        setupMarketListener()
    }

    private fun initializeRecyclerView() {

        binding.marketRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.marketRecycler.itemAnimator = DefaultItemAnimator()
        binding.marketRecycler.adapter = adapter
    }

    private fun setupMarketListener() {

        listener?.getMarketItems("", 200)?.observe(viewLifecycleOwner, {

            if (it != null) {

                adapter?.items?.clear().let {

                    adapter?.notifyDataSetChanged()
                }

                for (data in it.children) {

                    val item = data.getValue(Market::class.java)

                    if (item != null) {

                        adapter?.items?.add(item)
                        adapter?.notifyItemInserted((adapter?.itemCount ?: 1) - 1)
                    }
                }
            }
        })
    }
}