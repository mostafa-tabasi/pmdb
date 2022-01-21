package com.pmdb.android.utils.extensions

import android.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("selected")
fun TextView.selected(isSelected: Boolean) {
  setSelected(isSelected)
}

/**
 * ست کردن لیست آیتم های مربوط به فیلد auto complete برای نمایش
 *
 * @param items لیست آیتم های موردنظر
 */
fun MultiAutoCompleteTextView.setItems(
  items: List<String>,
  onFocusChangeListener: View.OnFocusChangeListener,
  onItemClickListener: AdapterView.OnItemClickListener
) {
  val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
    context,
    R.layout.simple_dropdown_item_1line,
    items
  )
  setOnFocusChangeListener(onFocusChangeListener)
  setAdapter(adapter)
  setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
  threshold = 1
  this.onItemClickListener = AdapterView.OnItemClickListener { adapterView, v, pos, id ->
    clear()
    onItemClickListener.onItemClick(adapterView, v, pos, id)
  }
}
