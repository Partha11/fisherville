package com.techmave.fisherville.view.dialog

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.techmave.fisherville.R

@Suppress("UNCHECKED_CAST")
abstract class BaseDialog<VB: ViewBinding>(private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB): DialogFragment() {

    private var _binding: ViewBinding? = null

//    protected var listener: DialogListener? = null
//    protected var videoListener: VideoListener? = null

    protected val binding: VB

        get() = _binding as VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = bindingInflater.invoke(inflater, container, false)
        return _binding?.root
    }

    override fun onStart() {

        super.onStart().also {

            dialog?.window?.setWindowAnimations(R.style.DialogAnimation)
        }
    }

    override fun onDestroy() {

        super.onDestroy().also { _binding = null }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState).also { initialize() }
    }

    fun DialogFragment.setWidthPercentage(percentage: Int) {

        val percent = percentage.toFloat() / 100
        val metrics = Resources.getSystem().displayMetrics
        val rect = metrics.run { Rect(0, 0, widthPixels, heightPixels) }
        val width = rect.width() * percent

        dialog?.window?.setLayout(width.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun DialogFragment.setDialogFullScreen() {

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    abstract fun initialize()
}