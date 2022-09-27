package com.techmave.fisherville.view.fragment.auth

import android.animation.Animator
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.techmave.fisherville.R
import com.techmave.fisherville.databinding.FragmentLoginBinding
import com.techmave.fisherville.model.state.DialogState
import com.techmave.fisherville.util.SharedPrefs
import com.techmave.fisherville.util.extension.LifeCycleExtensions.getNavigationResult
import com.techmave.fisherville.util.extension.LifeCycleExtensions.observeOnce
import com.techmave.fisherville.view.dialog.CustomProgress
import com.techmave.fisherville.view.fragment.BaseFragment
import com.techmave.fisherville.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment @Inject constructor(): BaseFragment<FragmentLoginBinding, LoginViewModel>(FragmentLoginBinding::inflate) {

    override val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var prefs: SharedPrefs

    @Inject
    lateinit var auth: FirebaseAuth

    private var verifyToken: PhoneAuthProvider.ForceResendingToken? = null
    private var verificationId: String? = null

    private var progressDialog: CustomProgress? = null

    private var callback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) { authenticate(credential) }

        override fun onVerificationFailed(exception: FirebaseException) {

            Log.d("Verification:Failed", exception.message, exception)

//            findNavController().navigate(LoginFragmentDirections.actionLoginToNews())
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {

            this@LoginFragment.verificationId = verificationId
            this@LoginFragment.verifyToken = token

            Handler(Looper.getMainLooper()).postDelayed({

                findNavController().navigate(LoginFragmentDirections.actionLoginToOtp())

            }, 5000)
        }
    }

    override fun onResume() {

        super.onResume().also {

            if (auth.currentUser != null) {

                findNavController().navigate(LoginFragmentDirections.actionLoginToNews())
            }
        }
    }

    override fun initialize() {

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.numValidated.observe(viewLifecycleOwner) {

            if (it) {

                viewModel.user.observeOnce(viewLifecycleOwner) { user -> beginFirebaseAuth(user.number) }
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {

            if (it.status == DialogState.Status.DIALOG_SHOW) {

                this.progressDialog = listener?.showProgress(it.title, it.content)

            } else {

                this.progressDialog?.dismiss()
            }
        }

        if (prefs.loggedIn) {

            Handler(Looper.getMainLooper()).postDelayed({ findNavController().navigate(LoginFragmentDirections.actionLoginToNews()) }, 1200)

        } else {

            initiateSplashAnimation()
        }

        createBackstackObserver()
    }

    private fun createBackstackObserver() {

        getNavigationResult<String>(R.id.fragment_login, resources.getString(R.string.action_otp)) { token ->

            verificationId?.let { id ->

                authenticate(PhoneAuthProvider.getCredential(id, token))
            }
        }
    }

    private fun authenticate(credential: PhoneAuthCredential) {

        updateDialogState(resources.getString(R.string.title_please_wait), resources.getString(R.string.label_communicating), DialogState.Status.DIALOG_SHOW)

        auth.signInWithCredential(credential).addOnCompleteListener {

            if (it.isSuccessful) {

                updateDialogState(null, null, DialogState.Status.DIALOG_HIDE).also {

                    findNavController().navigate(LoginFragmentDirections.actionLoginToNews())
                }
            }

        }.addOnFailureListener {

            it.printStackTrace().also { updateDialogState(null, null, DialogState.Status.DIALOG_HIDE) }
        }
    }

    private fun beginFirebaseAuth(number: String) {

        if (auth.currentUser != null) {

            findNavController().navigate(LoginFragmentDirections.actionLoginToNews())

        } else {

            updateDialogState(resources.getString(R.string.title_please_wait), resources.getString(R.string.label_communicating), DialogState.Status.DIALOG_SHOW)

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+88$number")
                .setTimeout(60, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(callback)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
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

                                override fun onAnimationStart(animation: Animator?) { }

                                override fun onAnimationEnd(animation: Animator?) {

                                    binding.bodyContainer.visibility = View.VISIBLE
                                    binding.loginLogoTitle.visibility = View.VISIBLE

                                    binding.loginLogoTitle.animate()
                                        .translationX((binding.root.width - binding.loginLogoTitle.width) / 2f)
                                        .setInterpolator(AccelerateDecelerateInterpolator()).duration = 600

                                    val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
                                    binding.bodyContainer.startAnimation(fadeIn)
                                }

                                override fun onAnimationCancel(animation: Animator?) { }

                                override fun onAnimationRepeat(animation: Animator?) { }
                            })

                    }, 800)
                }
            })
        }
    }

    private fun updateDialogState(title: String? = null, message: String? = null, status: DialogState.Status) {

        viewModel.state.value = DialogState(title, message, status)
    }
}