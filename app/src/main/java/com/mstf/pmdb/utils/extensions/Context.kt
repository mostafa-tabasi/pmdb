package com.mstf.pmdb.utils.extensions

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat


fun Context.toast(message: String) {
  Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(messageResId: Int) {
  toast(this.getString(messageResId))
}

fun Context.hideKeyboard(view: View) {
  val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
  inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.colorFromAttr(attrColor: Int): Int {
  val typedValue = TypedValue()
  theme.resolveAttribute(attrColor, typedValue, true)
  return ContextCompat.getColor(this, typedValue.resourceId)
}

fun Context.color(colorResourceId: Int) = ContextCompat.getColor(this, colorResourceId)