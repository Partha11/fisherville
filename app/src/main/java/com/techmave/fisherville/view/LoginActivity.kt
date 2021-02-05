package com.techmave.fisherville.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.techmave.fisherville.R
import com.techmave.fisherville.callback.OnOtpReceived
import com.techmave.fisherville.databinding.ActivityLoginBinding
import com.techmave.fisherville.dialog.CustomProgressDialog
import com.techmave.fisherville.util.Constants
import com.techmave.fisherville.util.Utility
import com.techmave.fisherville.viewmodel.LoginViewModel
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity(), OnOtpReceived, View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    private var dialog: CustomProgressDialog? = null
    private var vToken: String? = null
    private var callback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            authenticate(credential)
        }

        override fun onVerificationFailed(exception: FirebaseException) {

            Toast.makeText(this@LoginActivity, exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initialize()
    }

    private fun initialize() {

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        dialog = CustomProgressDialog()
        dialog?.isCancelable = false

        binding.termsText.text = Utility.fromHtml(getString(R.string.terms_message))
        binding.loginRadioGroup.check(R.id.radio_fisherman)

        binding.loginButton.setOnClickListener(this)
    }

    private fun authenticate(credential: PhoneAuthCredential) {

        dialog?.show(supportFragmentManager, "progress")
        dialog?.setMessage("Please Wait...")

        viewModel.authenticate(credential).observe(this, {

            if (it) {

                dialog?.dismiss()

            } else {

                dialog?.dismiss()
                Toast.makeText(this, "Verification failed! Please try again!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(v: View?) {

        when {

            binding.loginPhoneNumber.text.isNullOrEmpty() -> binding.loginPhoneNumber.error = "Required"
            binding.loginPhoneNumber.text.toString().length != 11 -> binding.loginPhoneNumber.error = "Invalid Phone Number"

            else -> {

                val number = "${Constants.BD_CODE}${binding.loginPhoneNumber.text}"

                dialog?.show(supportFragmentManager, "progress")
                dialog?.setMessage("Please Wait...")

                val auth = FirebaseAuth.getInstance()
                val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number)
                        .setTimeout(60, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(callback)
                        .build()

                PhoneAuthProvider.verifyPhoneNumber(options)
                dialog?.dismiss()
            }
        }
    }

    override fun onOtpReceived(code: String) {

        if (vToken != null) {

            val credential = PhoneAuthProvider.getCredential(vToken!!, code)

            dialog?.show(supportFragmentManager, "progress")
            dialog?.setMessage("Validating...")

            authenticate(credential)
        }
    }
}
