package com.techmave.fisherville.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.techmave.fisherville.databinding.DialogProgressBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomProgress @Inject constructor(): BaseDialog<DialogProgressBinding>(DialogProgressBinding::inflate) {

    var title = MutableLiveData<String>()
    var content = MutableLiveData<String>()

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

        binding.dialog = this
        binding.lifecycleOwner = viewLifecycleOwner
    }
}
