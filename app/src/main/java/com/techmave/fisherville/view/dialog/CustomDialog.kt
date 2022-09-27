package com.techmave.fisherville.view.dialog

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.DialogFragment
import com.techmave.fisherville.R
import com.techmave.fisherville.databinding.DialogCustomBinding

class CustomDialog : DialogFragment(), View.OnClickListener {

    private lateinit var binding: DialogCustomBinding

    var listener: OnClickListener? = null

    var title: String? = null
    var message: String? = null
    var confirmButtonText: String? = null
    var enableConfirmButton = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DialogCustomBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        initialize()
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
        val display = window?.windowManager?.defaultDisplay

        display?.getSize(size)
        val width = size.x

        window?.setLayout((width * 0.90).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER_HORIZONTAL)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initialize() {

        if (title != null && !TextUtils.isEmpty(title)) {

            binding.dialogCustomTitle.text = title
        }

        if (message != null && !TextUtils.isEmpty(message)) {

            binding.dialogCustomMessage.text = message
        }

        if (enableConfirmButton) {

            binding.confirmButton.visibility = View.VISIBLE

            if (confirmButtonText != null && confirmButtonText!!.isNotEmpty()) {

                binding.confirmButton.text = confirmButtonText
            }
        }

        binding.confirmButton.setOnClickListener(this)
        binding.cancelButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {

        if (view?.id == R.id.confirm_button) listener?.onConfirmed()
        dismiss()
    }

    interface OnClickListener {

        fun onConfirmed()
    }
}
