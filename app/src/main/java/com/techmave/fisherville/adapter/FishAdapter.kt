package com.techmave.fisherville.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techmave.fisherville.R
import com.techmave.fisherville.databinding.ModelFishBinding
import com.techmave.fisherville.model.Price
import com.techmave.fisherville.util.Utility

class FishAdapter(private val context: Context): RecyclerView.Adapter<FishAdapter.ViewHolder>() {

    var items: List<Price>? = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.model_fish, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items?.get(position)

        if (item != null) {

            val highestPrice = context.getString(R.string.highest_price, Utility.convertToOneDecimalPoints(item.highestPrice))
            val lowestPrice = context.getString(R.string.lowest_price, Utility.convertToOneDecimalPoints(item.lowestPrice))

            holder.binding.locationName.text = item.location
            holder.binding.highestPrice.text = highestPrice
            holder.binding.lowestPrice.text = lowestPrice

            if (item.available == 1L) {

                holder.binding.availableBadge.visibility = View.VISIBLE
                holder.binding.unavailableBadge.visibility = View.INVISIBLE

            } else {

                holder.binding.availableBadge.visibility = View.INVISIBLE
                holder.binding.unavailableBadge.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {

        return items?.size ?: 0
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var binding: ModelFishBinding = ModelFishBinding.bind(view)
    }
}