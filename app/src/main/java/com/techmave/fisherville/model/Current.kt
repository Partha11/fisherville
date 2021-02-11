package com.techmave.fisherville.model

import com.google.gson.annotations.SerializedName

   
data class Current (

   @SerializedName("dt") var dt : Long,
   @SerializedName("sunrise") var sunrise : Long,
   @SerializedName("sunset") var sunset : Long,
   @SerializedName("temp") var temp : Float,
   @SerializedName("feels_like") var feelsLike : Float,
   @SerializedName("pressure") var pressure : Float,
   @SerializedName("humidity") var humidity : Float,
   @SerializedName("dew_point") var dewPoint : Float,
   @SerializedName("uvi") var uvi : Float,
   @SerializedName("clouds") var clouds : Float,
   @SerializedName("visibility") var visibility : Int,
   @SerializedName("wind_speed") var windSpeed : Float,
   @SerializedName("wind_deg") var windDeg : Float,
   @SerializedName("weather") var weather : List<Weather>

)