package com.techmave.fisherville.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.techmave.fisherville.databinding.ModelMarketBinding
import com.techmave.fisherville.model.Market
import javax.inject.Inject

class MarketAdapter @Inject constructor(): ListAdapter<Market, MarketAdapter.ViewHolder>(Market.MarketDiffUtil) {

    var listener: MarketItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ModelMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        getItem(position)?.let { holder.bind(it) }

//        val item = items[position]
//
//        holder.binding.marketFishName.text = item.name
//        holder.binding.marketContent.text = item.summary
//
//        holder.binding.priceUpBadge.text = Utility.convertToOneDecimalPoints(item.highestPrice)
//        holder.binding.priceDownBadge.text = Utility.convertToOneDecimalPoints(item.lowestPrice)

//        if (!item.thumb.isNullOrEmpty()) {
//
//            Picasso.get().load(item.thumb).into(holder.binding.marketThumb)
//        }
    }

    inner class ViewHolder(val binding: ModelMarketBinding): RecyclerView.ViewHolder(binding.root) {

        init {

//            binding.listener = listener
        }

        fun bind(market: Market) {

            binding.market = market
            binding.executePendingBindings()
        }
    }

    fun interface MarketItemClickListener {

        fun onMarketItemClicked(market: Market)
    }
}