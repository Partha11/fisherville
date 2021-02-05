package com.techmave.fisherville.dialog

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import androidx.fragment.app.DialogFragment
import com.techmave.fisherville.R
import com.techmave.fisherville.callback.OnOtpReceived
import com.techmave.fisherville.databinding.DialogOtpBinding

class CustomOtpDialog(context: Context): DialogFragment(), View.OnClickListener {

    private lateinit var binding: DialogOtpBinding
    private var listener: OnOtpReceived? = null

    init {

        if (context is OnOtpReceived) {

            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DialogOtpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {

        countDown()

        binding.otpSubmit.setOnClickListener(this)
        binding.otpResend.setOnClickListener(this)
    }

    private fun countDown() {

        val text = context?.getString(R.string.resend)
        val timer: CountDownTimer = object : CountDownTimer(300000, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                var seconds = millisUntilFinished / 1000
                val minutes: Long
                var temp = text

                if (seconds >= 60) {

                    minutes = seconds / 60
                    seconds -= minutes * 60
                    temp += if (minutes > 9) {

                        if (seconds > 9) " ($minutes:$seconds)" else " ($minutes:0$seconds)"

                    } else {

                        if (seconds > 9) " (0$minutes:$seconds)" else " (0$minutes:0$seconds)"
                    }

                } else {

                    temp += if (seconds > 9) " (00:$seconds)" else " (00:0$seconds)"
                }

                binding.otpResend.text = temp
            }

            override fun onFinish() {

                binding.otpResend.isEnabled = true
            }
        }

        timer.start()
    }

    override fun onResume() {

        super.onResume()

        val window = dialog?.window
        val size = Point()

        if (window != null) {

            val display = window.windowManager.defaultDisplay
            display.getSize(size)

            val width = size.x

            window.setLayout((width * 0.90).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            window.setGravity(Gravity.CENTER_HORIZONTAL)
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.otp_submit -> validateCode()
        }
    }

    private fun validateCode() {

        when {

            binding.otpText.text.isNullOrEmpty() -> binding.otpText.error = "Required"
            binding.otpText.text.toString().length != 6 -> binding.otpText.error = "Incorrect Code Length"

            else -> {

                listener?.onOtpReceived(binding.otpText.text.toString())
                dismiss()
            }
        }
    }
}