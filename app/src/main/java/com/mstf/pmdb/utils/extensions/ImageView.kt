package com.mstf.pmdb.utils.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.mstf.pmdb.R

@BindingAdapter(value = ["omdbImageUrl", "quality"], requireAll = false)
fun ImageView.loadImage(url: String?, quality: Int?) {
  Glide.with(context).clear(this)
  url?.let {
    if (it == "N/A") {
      Glide.with(context).load(R.drawable.ic_foreground).into(this)
      return
    }

    Glide.with(context)
      .asBitmap()
      .load(if (quality == null) it else it.replace("SX300", "SX$quality"))
      .placeholder(R.drawable.ic_foreground)
      .error(R.drawable.ic_foreground)
      .into(this)
    return
  }
}

/**
 * ست کردن tint روی آیکن با توجه به وضعیت آن
 *
 * @param isActivated آیکن فعال است یا خیر
 */
@BindingAdapter("isActivated")
fun ImageView.changeTint(isActivated: Boolean) {
  val tintColor: Int =
    if (isActivated) R.attr.colorSelectedIconsTint else R.attr.colorUnSelectedIconsTint
  setColorFilter(context.colorFromAttr(tintColor), android.graphics.PorterDuff.Mode.SRC_IN)
}

/**
 * ست کردن آیکن متناسب با وضعیت انتخاب شدن یا نشدن آن
 *
 * @param isActivated آیکن انتخاب شده است یا خیر
 * @param activatedIcon آیکنی که در حالت انتخاب شده قراراست نمایش داده شود
 * @param deactivatedIcon آیکنی که در حالت انتخاب نشده قراراست نمایش داده شود
 */
@BindingAdapter(value = ["isActivated", "activatedIcon", "deactivatedIcon"], requireAll = true)
fun ImageView.selectableIcon(
  isActivated: Boolean,
  activatedIcon: Drawable,
  deactivatedIcon: Drawable
) {
  setImageDrawable(if (isActivated) activatedIcon else deactivatedIcon)
}