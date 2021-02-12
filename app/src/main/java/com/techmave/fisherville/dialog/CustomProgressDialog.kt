package com.techmave.fisherville.dialog

import android.graphics.Point
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.techmave.fisherville.R
import com.techmave.fisherville.databinding.DialogProgressBinding

class CustomProgressDialog : DialogFragment() {

    private lateinit var binding: DialogProgressBinding
    private var message = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DialogProgressBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {

        super.onStart()

        if (dialog != null) {

            dialog?.window?.setWindowAnimations(R.style.DialogAnimation)
        }
    }

    override fun onResume() {

        super.onResume()

        val window = dialog?.window
        val size = Point()

        if (window != null) {

            val display = window.windowManager.defaultDisplay
            display.getSize(size)

            val width = size.x

            window.setLayout((width * 0.95).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            window.setGravity(Gravity.CENTER_HORIZONTAL)
        }
    }

    private fun initialize() {

        binding.indeterminateLoadingText.text = message
    }

    fun setMessage(message: String) {

        if (dialog != null) {

            binding.indeterminateLoadingText.text = message
        }
    }
}
