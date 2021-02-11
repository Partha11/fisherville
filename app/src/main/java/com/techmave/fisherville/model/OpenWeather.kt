package com.techmave.fisherville.model

import com.google.gson.annotations.SerializedName

class OpenWeather {

    @SerializedName("lat")
    var lat : Double = 0.0

    @SerializedName("lon")
    var lon : Double = 0.0

    @SerializedName("timezone")
    var timezone : String = ""

    @SerializedName("timezone_offset")
    var timezoneOffset : Int = 0

    @SerializedName("current")
    var current : Current? = null

    @SerializedName("daily")
    var daily : List<Daily>? = null
}