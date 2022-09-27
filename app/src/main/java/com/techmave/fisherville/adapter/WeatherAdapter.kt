package com.techmave.fisherville.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techmave.fisherville.R
import com.techmave.fisherville.databinding.ModelWeatherBinding
import com.techmave.fisherville.model.Daily
import com.techmave.fisherville.util.Utility

class WeatherAdapter(private val context: Context): RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    var items: List<Daily>? = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.model_weather, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items?.get(position)

        if (item != null) {

            val dayText = Utility.getDayStringFromTimestamp(item.dt)

            holder.binding.dayName.text = dayText
            holder.binding.tempUpBadge.text = context.getString(R.string.temperature_text, Utility.convertToOneDecimalPoints(item.temp.max))
            holder.binding.tempDownBadge.text = context.getString(R.string.temperature_text, Utility.convertToOneDecimalPoints(item.temp.min))
            holder.binding.airPressure.text = context.getString(R.string.air_pressure, Utility.convertToOneDecimalPoints(item.pressure))
            holder.binding.windSpeed.text = context.getString(R.string.wind_speed, Utility.convertToOneDecimalPoints(item.windSpeed))
            holder.binding.weatherSummary.text = Utility.convertStringToUpperCase(item.weather[0].description)

//            Picasso.get().load(context.getString(R.string.icon_api, item.weather[0].icon)).into(holder.binding.weatherThumb)
        }

//        holder.binding.transactionFishName.text = item.fishName
//        holder.binding.transactionContent.text = text
//
//        if (item.transactionType == 0L) {
//
//            holder.binding.transactionTypeBadgeBuy.visibility = View.VISIBLE
//            holder.binding.transactionTypeBadgeSell.visibility = View.INVISIBLE
//
//        } else {
//
//            holder.binding.transactionTypeBadgeSell.visibility = View.VISIBLE
//            holder.binding.transactionTypeBadgeBuy.visibility = View.INVISIBLE
//        }
    }

    override fun getItemCount(): Int {

        return items?.size ?: 0
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var binding: ModelWeatherBinding = ModelWeatherBinding.bind(view)
    }
}