package com.techmave.fisherville.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techmave.fisherville.R
import com.techmave.fisherville.databinding.ModelMarketBinding
import com.techmave.fisherville.model.Market
import com.techmave.fisherville.util.Utility

class MarketAdapter: RecyclerView.Adapter<MarketAdapter.ViewHolder>() {

    var items = mutableListOf<Market>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.model_market, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        holder.binding.marketFishName.text = item.name
        holder.binding.marketContent.text = item.summary

        holder.binding.priceUpBadge.text = Utility.convertToOneDecimalPoints(item.highestPrice)
        holder.binding.priceDownBadge.text = Utility.convertToOneDecimalPoints(item.lowestPrice)
    }

    override fun getItemCount(): Int {

        return items.size
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var binding: ModelMarketBinding = ModelMarketBinding.bind(view)
    }
}