package com.mstf.pmdb.utils.extensions

import android.view.View
import android.view.View.*
import com.google.android.material.snackbar.Snackbar

fun View.visible() {
  visibility = VISIBLE
}

fun View.invisible() {
  visibility = INVISIBLE
}

fun View.gone() {
  visibility = GONE
}

fun View.isVisible(): Boolean {
  return visibility == VISIBLE
}

fun View.isInvisible(): Boolean {
  return visibility == INVISIBLE
}

fun View.isGone(): Boolean {
  return visibility == GONE
}

fun View.isInvisibleOrGone(): Boolean {
  return visibility == GONE || visibility == INVISIBLE
}

fun View.snackBar(
  message: String,
  duration: Int = Snackbar.LENGTH_SHORT,
  functionsToApply: (() -> Unit)? = null
) {
  Snackbar.make(this, message, duration).apply {
    functionsToApply?.invoke()
  }.show()
}
