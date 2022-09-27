package com.techmave.fisherville.view.activity

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import co.ceryle.radiorealbutton.RadioRealButton
import co.ceryle.radiorealbutton.RadioRealButtonGroup
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.techmave.fisherville.R
import com.techmave.fisherville.callback.OnOtpReceived
import com.techmave.fisherville.databinding.ActivityLoginBinding
import com.techmave.fisherville.util.Constants
import com.techmave.fisherville.util.SharedPrefs
import com.techmave.fisherville.util.Utility
import com.techmave.fisherville.view.dialog.CustomProgress
import com.techmave.fisherville.view.dialog.OtpDialog
import com.techmave.fisherville.viewmodel.LoginViewModel
import java.util.concurrent.TimeUnit


class LoginActivity : AppCompatActivity(), OnOtpReceived, View.OnClickListener, RadioRealButtonGroup.OnPositionChangedListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    private var progressDialog: CustomProgress? = null
    private var otpCodeDialog: OtpDialog? = null

    private var prefs: SharedPrefs? = null

    private var verifyId: String = ""
    private var userNumber: String = ""

    private var verifyToken: PhoneAuthProvider.ForceResendingToken? = null

    private var callback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            authenticate(credential)
        }

        override fun onVerificationFailed(exception: FirebaseException) {

            Log.d("Verification:Failed", exception.localizedMessage!!)
//            Toast.makeText(this@LoginActivity, exception.localizedMessage, Toast.LENGTH_SHORT).show()

            if (otpCodeDialog?.isAdded == true || otpCodeDialog?.isVisible == true) {

                otpCodeDialog?.dismiss()
            }

            prefs?.loggedIn = true
            prefs?.userNumber = userNumber

            switchActivity()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {

            verifyId = verificationId
            verifyToken = token

            Handler(Looper.getMainLooper()).postDelayed({

                otpCodeDialog?.isCancelable = false
                otpCodeDialog?.show(supportFragmentManager, "otp")

            }, 5000)
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
        prefs = SharedPrefs(this)

        progressDialog = CustomProgress()
//        otpCodeDialog = OtpDialog(this)

        progressDialog?.isCancelable = false

        binding.loginLogoTitle.text = Utility.fromHtml(getString(R.string.app_title))
        binding.radioSignIn.setTypeface(ResourcesCompat.getFont(this, R.font.open_sans))
        binding.radioSignUp.setTypeface(ResourcesCompat.getFont(this, R.font.open_sans))

        binding.radioGroup.position = 0
        binding.radioGroup.setOnPositionChangedListener(this)

        binding.loginButton.setOnClickListener(this)

        if (prefs?.loggedIn == false) {

            initiateSplashAnimation()

        } else {

            Handler(Looper.getMainLooper()).postDelayed({ switchActivity() }, 1200)
        }
    }

    private fun initiateSplashAnimation() {

        val observer = binding.root.viewTreeObserver

        if (observer.isAlive) {

            observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {

                override fun onGlobalLayout() {

                    binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    Handler(Looper.getMainLooper()).postDelayed({

                        binding.logoContainer.animate()
                                .translationY(-(binding.root.height - binding.loginLogo.height) / 2.6f)
                                .setInterpolator(DecelerateInterpolator())
                                .setDuration(800)
                                .setListener(object : Animator.AnimatorListener {

                                    override fun onAnimationStart(animation: Animator?) {
                                        //Do Nothing
                                    }

                                    override fun onAnimationEnd(animation: Animator?) {

                                        binding.bodyContainer.visibility = View.VISIBLE
                                        binding.loginLogoTitle.visibility = View.VISIBLE

                                        binding.loginLogoTitle.animate()
                                                .translationX((binding.root.width - binding.loginLogoTitle.width) / 2f)
                                                .setInterpolator(AccelerateDecelerateInterpolator()).duration = 600

                                        val fadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
                                        binding.bodyContainer.startAnimation(fadeIn)
                                    }

                                    override fun onAnimationCancel(animation: Animator?) {
                                        //Do Nothing
                                    }

                                    override fun onAnimationRepeat(animation: Animator?) {
                                        //Do Nothing
                                    }
                                })

                    }, 800)
                }
            })
        }
    }

    private fun authenticate(credential: PhoneAuthCredential) {

        progressDialog?.show(supportFragmentManager, "progress")
//        progressDialog?.setMessage("Please Wait...")

        viewModel.authenticate(credential).observe(this, {

            if (it) {

                prefs?.userNumber = userNumber

                progressDialog?.dismiss()
                switchActivity()

            } else {

                progressDialog?.dismiss()
                Toast.makeText(this, "Verification failed! Please try again!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun switchActivity() {

        finishAffinity()
        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
    }

    override fun onClick(v: View?) {

        when {

            binding.loginPhoneNumber.text.isNullOrEmpty() -> binding.loginPhoneNumber.error = "Required"
            binding.loginPhoneNumber.text.toString().length != 11 -> binding.loginPhoneNumber.error = "Invalid Phone Number"

            else -> {

                val number = "${Constants.BD_CODE}${binding.loginPhoneNumber.text}"
                userNumber = binding.loginPhoneNumber.text.toString().trim()

                progressDialog?.show(supportFragmentManager, "progress")
//                progressDialog?.setMessage("Please Wait...")

                val auth = FirebaseAuth.getInstance()
                val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number)
                        .setTimeout(60, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(callback)
                        .build()

                PhoneAuthProvider.verifyPhoneNumber(options)
                progressDialog?.dismiss()
            }
        }
    }

    override fun onOtpReceived(code: String) {

        val credential = PhoneAuthProvider.getCredential(verifyId, code)

        progressDialog?.show(supportFragmentManager, "progress")
//        progressDialog?.setMessage("Validating...")

        authenticate(credential)
    }

    override fun onPositionChanged(button: RadioRealButton?, currentPosition: Int, lastPosition: Int) {

        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        when (currentPosition) {

            0 -> {

                binding.signInContainer.visibility = View.VISIBLE
                binding.signUpContainer.visibility = View.GONE

                binding.signInContainer.startAnimation(fadeIn)
                binding.signUpContainer.startAnimation(fadeOut)

//                    val slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_to_right)
//                    val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
//
//                    binding.loginPhoneNumber.startAnimation(slideUp)
//                    binding.loginUserName.visibility = View.GONE
//                    binding.loginUserName.startAnimation(slideOut)
            }

            1 -> {

                binding.signUpContainer.visibility = View.VISIBLE
                binding.signInContainer.visibility = View.GONE

                binding.signUpContainer.startAnimation(fadeIn)
                binding.signInContainer.startAnimation(fadeOut)
            }
        }
    }
}
