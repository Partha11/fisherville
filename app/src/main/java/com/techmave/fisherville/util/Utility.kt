package com.techmave.fisherville.util

import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.text.Html
import android.text.Spanned
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


object Utility {

    @JvmStatic
    fun fromHtml(html: String?): Spanned? {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)

        } else {

            Html.fromHtml(html)
        }
    }

    @JvmStatic
    fun getGreetingMessage(): String {

        val calendar = Calendar.getInstance()

        return when (calendar.get(Calendar.HOUR_OF_DAY)) {

            in 6..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            in 16..20 -> "Good Evening"
            in 21..23 -> "Good Night"
            in 0..6 -> "Good Night"
            else -> "Good Morning"
        }
    }

    @JvmStatic
    fun getTimeFromTimestamp(millis: Long): String {

        if (millis == 0L) {

            return ""
        }

        val calendar = Calendar.getInstance()
        val sdFormat = SimpleDateFormat("hh:mm aa", Locale.US)

        calendar.timeInMillis = millis

        return sdFormat.format(calendar.time)
    }

    @JvmStatic
    fun getDateFromTimestamp(millis: Long): String {

        if (millis == 0L) {

            return ""
        }

        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    @JvmStatic
    fun getDayStringFromTimestamp(millis: Long): String {

        val c = Calendar.getInstance()
        val s = SimpleDateFormat("EEEE", Locale.US)

        c.timeInMillis = millis * 1000L

        return s.format(c.time)
    }

    @JvmStatic
    fun isGPSEnabled(context: Context): Boolean {

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @JvmStatic
    fun convertToOneDecimalPoints(number: Float): String {

        val df = DecimalFormat("0.0")
        return df.format(number)
    }

    @JvmStatic
    fun convertStringToUpperCase(text: String?): String {

        if (text.isNullOrEmpty()) {

            return "Empty"
        }

        val parts = text.split(" ")
        val nPart = mutableListOf<String>()

        for (part in parts) {

            nPart.add(part.capitalize(Locale.US))
        }

        return nPart.joinToString(" ")
    }

    @JvmStatic
    fun getFirstPartFromName(text: String?): String {

        if (text.isNullOrEmpty()) return ""

        val parts = text.split(" ")
        return if (parts.isNotEmpty()) parts[0].capitalize(Locale.US) else text
    }
}