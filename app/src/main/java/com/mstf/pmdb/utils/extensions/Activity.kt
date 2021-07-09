package com.mstf.pmdb.utils.extensions

import android.app.Activity
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import androidx.core.content.ContextCompat
import kotlin.math.pow
import kotlin.math.sqrt

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

fun Activity.isTablet(): Boolean {
  val display = windowManager.defaultDisplay
  val metrics = DisplayMetrics()
  display.getMetrics(metrics)
  val widthInches = metrics.widthPixels / metrics.xdpi
  val heightInches = metrics.heightPixels / metrics.ydpi
  val diagonalInches =
    sqrt(widthInches.toDouble().pow(2.0) + heightInches.toDouble().pow(2.0))
  return diagonalInches >= 7.0
}

fun Activity.actionBarSize(): Int {
  val tv = TypedValue()
  return if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
    TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
  } else 0
}