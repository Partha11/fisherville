package com.techmave.fisherville.util

import android.os.Build
import android.text.Html
import android.text.Spanned
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

            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            in 16..20 -> "Good Evening"
            in 21..23 -> "Good Night"
            else -> "Good Morning"
        }
    }
}