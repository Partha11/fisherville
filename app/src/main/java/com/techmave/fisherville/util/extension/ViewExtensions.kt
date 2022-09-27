package com.techmave.fisherville.util.extension

import android.util.Patterns
import android.widget.EditText
import java.util.regex.Pattern

object ViewExtensions {

    private val pattern = Pattern.compile("10\\d{9,13}")

    fun String.isValidEmail() = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    fun String.isValidPhoneNumber() = this.isNotEmpty() && Patterns.PHONE.matcher(this).matches()

    fun EditText.isValidPid() = this.text.toString().isNotEmpty() && pattern.matcher(this.text.toString()).matches()
}