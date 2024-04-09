package com.omdb.app.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class Utils {

    fun View.hideSoftKeyBoard() {
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(this.windowToken, 0)
    }

}