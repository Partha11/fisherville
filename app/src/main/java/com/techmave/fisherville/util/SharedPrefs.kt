package com.techmave.fisherville.util

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context?) {

    private var editor: SharedPreferences.Editor? = null
    private var prefs = context?.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

    var isLoggedIn: Boolean

        get() = prefs?.getBoolean(Constants.PREF_IS_LOGGED_IN, false) ?: false

        set(value) {

            editor = prefs?.edit()

            editor?.putBoolean(Constants.PREF_IS_LOGGED_IN, value)
            editor?.apply()
        }

    var userNumber: String

        get() = prefs?.getString(Constants.PREF_USER_NUMBER, "") ?: ""

        set(number) {

            editor = prefs?.edit()

            editor?.putString(Constants.PREF_USER_NUMBER, number)
            editor?.apply()
        }

    var userName: String

        get() = prefs?.getString(Constants.PREF_USER_NAME, "Mr.") ?: "Mr."

        set(name) {

            editor = prefs?.edit()

            editor?.putString(Constants.PREF_USER_NAME, name)
            editor?.apply()
        }

    var userType: Long

        get() = prefs?.getLong(Constants.PREF_USER_TYPE, 0) ?: 0

        set(type) {

            editor = prefs?.edit()

            editor?.putLong(Constants.PREF_USER_TYPE, type)
            editor?.apply()
        }

    var weatherFetchTimeMillis: Long

        get() = prefs?.getLong(Constants.PREF_LAST_WEATHER_FETCHED, 0) ?: 0

        set(time) {

            editor = prefs?.edit()

            editor?.putLong(Constants.PREF_LAST_WEATHER_FETCHED, time)
            editor?.apply()
        }
}