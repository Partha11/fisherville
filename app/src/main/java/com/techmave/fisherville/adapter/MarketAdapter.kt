package com.techmave.fisherville.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.techmave.fisherville.R
import com.techmave.fisherville.databinding.ModelMarketBinding
import com.techmave.fisherville.model.Market
import com.techmave.fisherville.util.Utility

class MarketAdapter(context: Context): RecyclerView.Adapter<MarketAdapter.ViewHolder>() {

    var items = mutableListOf<Market>()
    var listener: MarketItemClickListener? = null

    init {

        if (context is MarketItemClickListener) {

            listener = context
        }
    }

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

        if (!item.thumb.isNullOrEmpty()) {

            Picasso.get().load(item.thumb).into(holder.binding.marketThumb)
        }
    }

    override fun getItemCount(): Int {

        return items.size
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

        var binding: ModelMarketBinding = ModelMarketBinding.bind(view)

        init {

            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            listener?.onMarketItemClicked(items[adapterPosition])
        }
    }

    interface MarketItemClickListener {

        fun onMarketItemClicked(market: Market)
    }
}