package com.techmave.fisherville.model

import com.google.gson.annotations.SerializedName

class Weather {

   @SerializedName("id")
   var id: Int = 0

   @SerializedName("main")
   var main: String? = null

   @SerializedName("description")
   var description: String? = null

   @SerializedName("icon")
   var icon: String? = null

   @SerializedName("image")
   var image: String? = null
}