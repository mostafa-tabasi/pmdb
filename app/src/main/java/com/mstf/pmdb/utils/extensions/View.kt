package com.mstf.pmdb.utils.extensions

import android.view.View
import android.view.View.*

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
