package com.techmave.fisherville.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.techmave.fisherville.R
import com.techmave.fisherville.databinding.DialogOtpBinding
import com.techmave.fisherville.util.extension.LifeCycleExtensions.observeOnce
import com.techmave.fisherville.util.extension.LifeCycleExtensions.setNavigationResult
import com.techmave.fisherville.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OtpDialog @Inject constructor(): BaseDialog<DialogOtpBinding>(DialogOtpBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState).also { setWidthPercentage(90) }
    }

    override fun onResume() {

        super.onResume().also {

            dialog?.window?.apply {

                setGravity(Gravity.CENTER_HORIZONTAL)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    override fun initialize() {

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.otpCancel.setOnClickListener { findNavController().popBackStack() }

        viewModel.otpValidated.observe(viewLifecycleOwner) {

            if (it) {

                viewModel.mainOtp.observeOnce(viewLifecycleOwner) { otp ->

                    setNavigationResult(resources.getString(R.string.action_otp), otp).also { findNavController().navigateUp() }
                }
            }
        }
    }

//    private fun countDown() {
//
//        val text = context?.getString(R.string.resend)
//        val timer: CountDownTimer = object : CountDownTimer(300000, 1000) {
//
//            override fun onTick(millisUntilFinished: Long) {
//
//                var seconds = millisUntilFinished / 1000
//                val minutes: Long
//                var temp = text
//
//                if (seconds >= 60) {
//
//                    minutes = seconds / 60
//                    seconds -= minutes * 60
//                    temp += if (minutes > 9) {
//
//                        if (seconds > 9) " ($minutes:$seconds)" else " ($minutes:0$seconds)"
//
//                    } else {
//
//                        if (seconds > 9) " (0$minutes:$seconds)" else " (0$minutes:0$seconds)"
//                    }
//
//                } else {
//
//                    temp += if (seconds > 9) " (00:$seconds)" else " (00:0$seconds)"
//                }
//
//                binding.otpResend.text = temp
//            }
//
//            override fun onFinish() {
//
//                binding.otpResend.isEnabled = true
//            }
//        }
//
//        timer.start()
//    }

//    override fun onClick(v: View?) {
//
//        when (v?.id) {
//
//            R.id.otp_submit -> validateCode()
//        }
//    }
//
//    private fun validateCode() {
//
//        when {
//
//            binding.otpText.text.isNullOrEmpty() -> binding.otpText.error = "Required"
//            binding.otpText.text.toString().length != 6 -> binding.otpText.error = "Incorrect Code Length"
//
//            else -> {
//
//                listener?.onOtpReceived(binding.otpText.text.toString())
//                dismiss()
//            }
//        }
//    }
}