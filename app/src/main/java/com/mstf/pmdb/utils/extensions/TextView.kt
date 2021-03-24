package com.mstf.pmdb.utils.extensions

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("selected")
fun TextView.selected(isSelected: Boolean) {
  setSelected(isSelected)
}
