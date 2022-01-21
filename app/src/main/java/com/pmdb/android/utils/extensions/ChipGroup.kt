package com.pmdb.android.utils.extensions

import android.animation.LayoutTransition
import android.view.View.generateViewId
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


/**
 * اضافه کردن چیپ به لایه
 *
 * @param label عنوان چیپ
 * @param isRemovable چیپ قابل حذف شدن است یا خیر
 * @param animate چیپ همراه با انیمیشن اضافه شود یا خیر
 * @param onChipDelete عملیات لازم بعد از حذف چیپ
 */
fun ChipGroup.addChip(
  label: String,
  isRemovable: Boolean,
  animate: Boolean,
  onChipDelete: (String) -> Unit
) {
  // اگر بدون انیمیشن بود، انیمیشن مربوط به لایه ی parent را غیرفعال میکنیم
  if (!animate) layoutTransition = null

  Chip(context).apply {
    id = generateViewId()
    text = label
    isCheckable = false
    isCheckedIconVisible = false
    isCloseIconVisible = isRemovable
    setOnCloseIconClickListener {
      removeView(this)
      onChipDelete(label)
    }
    addView(this)
  }
  // در آخر انیمیشن مربوط به لایه ی parent فعال میکنیم
  layoutTransition = LayoutTransition()
}

/**
 * حذف کردن چیپ از لایه
 *
 * @param label عنوان چیپ موردنظر برای حذف شدن
 */
fun ChipGroup.removeChip(label: String) {
  children.forEach { if ((it as Chip).text == label) removeView(it) }
}

/**
 * حذف کردن تمام چیپ های موجود از لایه
 */
fun ChipGroup.removeAllChips() {
  layoutTransition = null
  removeAllViews()
  layoutTransition = LayoutTransition()
}

/**
 *چیپ ها قابلیت حذف شدن داشته باشند یا خیر
 */
@BindingAdapter("chipsRemovable")
fun ChipGroup.isChipsRemovable(removable: Boolean) {
  children.forEach { (it as Chip).isCloseIconVisible = removable }
}
