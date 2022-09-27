package com.techmave.fisherville.util.databinding

import android.content.res.ColorStateList
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@BindingAdapter("setErrorText")
fun TextInputLayout.setErrorText(text: String?) {

    this.isErrorEnabled = !text.isNullOrEmpty()
    this.error = text
}

//@BindingAdapter("setData")
//fun LineChart.setLineData(data: TLineData) {
//
//    setData(data).also { invalidate() }
//}

@BindingAdapter("setImageSrc")
fun AppCompatImageView.setImageSrc(src: Int) {

    if (src != 0) {

        this.setImageResource(src)
    }
}

@BindingAdapter("setImageFromUri")
fun AppCompatImageView.setImageFromUri(src: String?) {

    src?.let {

        CoroutineScope(Dispatchers.Main).launch {

            Glide.with(context).load(it).into(this@setImageFromUri)
        }
    }
}

@BindingAdapter("setBgTint")
fun MaterialTextView.setBgTint(color: Int) {

    if (color != 0) {

        backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, color))
    }
}

@BindingAdapter("setBorderColor")
fun CircleImageView.setImageBorderColor(color: Int) {

    if (color != 0) {

        borderColor = ContextCompat.getColor(context, color)
    }
}