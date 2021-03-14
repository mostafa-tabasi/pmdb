package com.mstf.pmdb.utils.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.mstf.pmdb.R

@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) {
  url?.let {
    Glide.with(context)
      .asBitmap()
      .load(it.replace("SX300", "SX750"))
      .into(object : CustomTarget<Bitmap>() {

        override fun onLoadCleared(placeholder: Drawable?) {}

        override fun onLoadFailed(errorDrawable: Drawable?) {
          Glide.with(context).load(R.drawable.ic_foreground).into(this@loadImage)
        }

        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
          setImageBitmap(resource)
        }
      })

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
  setColorFilter(context.getColorFromAttr(tintColor), android.graphics.PorterDuff.Mode.SRC_IN)
}

/**
 * ست کردن آیکن متناسب با وضعیت انتخاب شدن یا نشدن آن
 *
 * @param isSelected آیکن انتخاب شده است یا خیر
 * @param selectedIcon آیکنی که در حالت انتخاب شده قراراست نمایش داده شود
 * @param unselectedIcon آیکنی که در حالت انتخاب نشده قراراست نمایش داده شود
 */
@BindingAdapter(value = ["isSelected", "selectedIcon", "unselectedIcon"], requireAll = true)
fun ImageView.selectableIcon(
  isSelected: Boolean,
  selectedIcon: Drawable,
  unselectedIcon: Drawable
) {
  setImageDrawable(if (isSelected) selectedIcon else unselectedIcon)
}