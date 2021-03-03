package com.mstf.pmdb.utils.extensions

import android.text.InputType
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
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

@BindingAdapter("onEditorEnterAction")
fun EditText.onEditorEnterAction(f: (() -> Unit)?) {

  if (f == null) setOnEditorActionListener(null)
  else setOnEditorActionListener { v, actionId, event ->

    val imeAction = when (actionId) {
      EditorInfo.IME_ACTION_DONE,
      EditorInfo.IME_ACTION_SEND,
      EditorInfo.IME_ACTION_GO -> true
      else -> false
    }

    val keyDownEvent = event?.keyCode == KeyEvent.KEYCODE_ENTER
        && event.action == KeyEvent.ACTION_DOWN

    if (imeAction or keyDownEvent)
      true.also { f.invoke() }
    else false
  }
}