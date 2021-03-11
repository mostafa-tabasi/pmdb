package com.mstf.pmdb.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mstf.pmdb.data.model.api.MatchedMovieCompact
import com.mstf.pmdb.ui.main.home.add_movie_dialog.adapter.MatchedMoviesAdapter

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
}
