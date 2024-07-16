package com.beblue.gfpf.test.bebluegfpftest.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object Util {
    fun hideKeyboard(act: Activity) {
        val imm = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(act.window.decorView.windowToken, 0)
    }
}
