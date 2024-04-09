package com.omdb.app.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide

fun View.hideSoftKeyBoard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun ImageView.loadImageFromUrl(url: String) {

    Glide.with(this)
        .load(url)
        .centerCrop()
        //.placeholder(R.drawable.load)
        .into(this)
}