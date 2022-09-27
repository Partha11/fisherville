package com.techmave.fisherville.model

import androidx.recyclerview.widget.DiffUtil

class Market {

    var id: String = ""
    var name: String = ""
    var thumb: String? = ""
    var summary: String = ""
    var highestPrice: Float = 0f
    var lowestPrice: Float = 0f
    var prices: HashMap<String, Price>? = null

    override fun equals(other: Any?) = other is Market && other.id == this.id && other.name == this.name

    override fun hashCode(): Int {

        var result = id.hashCode()

        result = 31 * result + name.hashCode()
        result = 31 * result + (thumb?.hashCode() ?: 0)
        result = 31 * result + summary.hashCode()
        result = 31 * result + highestPrice.hashCode()
        result = 31 * result + lowestPrice.hashCode()
        result = 31 * result + (prices?.hashCode() ?: 0)

        return result
    }

    object MarketDiffUtil: DiffUtil.ItemCallback<Market>() {

        override fun areItemsTheSame(oldItem: Market, newItem: Market) = oldItem.id == newItem.id && oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Market, newItem: Market) = oldItem == newItem
    }
}