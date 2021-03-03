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