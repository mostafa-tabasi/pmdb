package com.pmdb.android.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pmdb.android.R
import com.pmdb.android.data.model.api.MatchedMovieCompact
import com.pmdb.android.ui.main.home.add_movie_dialog.adapter.MatchedMoviesAdapter
import com.pmdb.android.utils.extensions.color

object BindingUtils {

  @JvmStatic
  @BindingAdapter("adapter")
  fun RecyclerView.addMatchedMovieItems(movies: List<MatchedMovieCompact>?) {
    movies?.let {
      with(adapter as MatchedMoviesAdapter) {
        clearItems()
        addItems(movies)
      }
    }
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

  /**
   * تغییر وضعیت فعال بودن تمام فیلدهای EditText موجود در یک لایه
   *
   * @param isEnable وضعیت EditText که باید به آن تغییر کند
   */
  @JvmStatic
  @BindingAdapter("editTextsEditingEnable")
  fun ViewGroup.setEditTextsEditingEnable(isEnable: Boolean) {
    children.forEach { child ->
      if (child is ViewGroup) child.setEditTextsEditingEnable(isEnable)
      else if (child is EditText) {
        child.isEnabled = isEnable
        // تغییر رنگ مربوط به خط زیرِ فیلد EditText
        if (isEnable)
          child.backgroundTintList =
            ContextCompat.getColorStateList(context, R.color.selector_color_movie_form_field_tint)
        else {
          val colorFrom = context.color(R.color.selector_color_movie_form_field_tint)
          val colorTo = context.color(android.R.color.transparent)
          ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo).apply {
            duration = 200
            addUpdateListener { animator ->
              child.backgroundTintList = ColorStateList.valueOf(animator.animatedValue as Int)
            }
          }.start()
        }
      }
    }
  }
}
