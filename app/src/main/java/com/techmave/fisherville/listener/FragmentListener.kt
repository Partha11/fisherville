package com.techmave.fisherville.listener

import com.techmave.fisherville.view.dialog.CustomDialog
import com.techmave.fisherville.view.dialog.CustomProgress

interface FragmentListener {

    fun showProgress(title: String?, content: String?): CustomProgress
    fun showDialog(title: String?, content: String?, enableConfirm: Boolean = false): CustomDialog

    fun showHideBottomNav(isVisible: Boolean)
}