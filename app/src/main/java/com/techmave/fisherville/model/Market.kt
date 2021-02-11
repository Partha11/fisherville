package com.techmave.fisherville.model

class Market {

    var id: String = ""
    var name: String = ""
    var summary: String = ""
    var highestPrice: Float = 0f
    var lowestPrice: Float = 0f
    var prices: List<Price>? = null
}