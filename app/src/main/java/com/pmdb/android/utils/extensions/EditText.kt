package com.pmdb.android.utils.extensions

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
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

@BindingAdapter("numberSeparator")
fun EditText.setNumberSeparator(isEnable: Boolean) {

  val textWatcher = object : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
      p0?.let {
        removeTextChangedListener(this)
        try {
          setText("%,d".format(p0.toString().replace(",", "").toLong()))
          setSelection(text.length)
        } catch (e: Exception) {
        }
        addTextChangedListener(this)
      }
    }
  }

  if (isEnable) addTextChangedListener(textWatcher)
  else removeTextChangedListener(textWatcher)
}