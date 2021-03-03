package com.mstf.pmdb.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

object BindingUtils {

/*
  @JvmStatic
  @BindingAdapter("adapter")
  fun addBlogItems(recyclerView: RecyclerView, blogs: List<BlogResponse.Blog>?) {
      val adapter: BlogAdapter = recyclerView.adapter as BlogAdapter
      if (adapter != null) {
          adapter.clearItems()
          adapter.addItems(blogs)
      }
  }
*/

  @JvmStatic
  @BindingAdapter("imageUrl")
  fun setImageUrl(imageView: ImageView, url: String) {
    val context = imageView.context
    Glide.with(context).load(url).into(imageView)
  }

  @JvmStatic
  @BindingAdapter("animatedVisibility")
  fun BottomAppBar.setAnimatedVisibility(isVisible: Boolean) {
    if (isVisible) performShow() else performHide()
  }

  @JvmStatic
  @BindingAdapter("animatedVisibility")
  fun FloatingActionButton.setAnimatedVisibility(isVisible: Boolean) {
    if (isVisible) show() else hide()
  }
}
