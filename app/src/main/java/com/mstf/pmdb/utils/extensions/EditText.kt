package com.mstf.pmdb.utils.extensions

import android.text.InputType
import android.widget.EditText
import androidx.databinding.BindingAdapter

fun EditText.clear() {
  setText("")
}

@BindingAdapter("multiLineWithAction")
fun EditText.setMultiLineWithNextAction(action: Int) {
  imeOptions = action
  setRawInputType(InputType.TYPE_CLASS_TEXT)
}