package com.mstf.pmdb.utils.extensions

import android.app.Activity
import android.graphics.Color
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import androidx.core.content.ContextCompat

fun Activity.transparentStatusBar() {
  window.apply {
    decorView.systemUiVisibility =
      SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE
    statusBarColor = Color.TRANSPARENT
  }
}

fun Activity.coloredStatusBar(color: Int) {
  window.apply {
    decorView.systemUiVisibility = 0
    statusBarColor = ContextCompat.getColor(context, color)
  }
}