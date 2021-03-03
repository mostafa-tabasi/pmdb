package com.mstf.pmdb.ui.main.home.add_movie_dialog

import android.view.View
import androidx.databinding.ObservableField
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.synthetic.main.layout_find_movie.view.*
import javax.inject.Inject

@HiltViewModel
class AddMovieViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<AddMovieNavigator>(dataManager), View.OnFocusChangeListener {

  val searchLayoutVisibility = ObservableField<Int>().apply { set(View.VISIBLE) }
  val blankFormLayoutVisibility = ObservableField<Int>().apply { set(View.GONE) }
  val searchLayoutAlpha = ObservableField<Float>().apply { set(1F) }
  val blankFormLayoutAlpha = ObservableField<Float>().apply { set(0F) }

  val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
    override fun onStateChanged(bottomSheet: View, newState: Int) {
      when (newState) {
        BottomSheetBehavior.STATE_EXPANDED -> {
          searchLayoutVisibility.set(View.GONE)
        }
        BottomSheetBehavior.STATE_COLLAPSED -> blankFormLayoutVisibility.set(View.GONE)
        BottomSheetBehavior.STATE_DRAGGING -> {
          searchLayoutVisibility.set(View.VISIBLE)
          blankFormLayoutVisibility.set(View.VISIBLE)
        }
      }
    }

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
      searchLayoutAlpha.set(1 - (slideOffset * 5))
      blankFormLayoutAlpha.set(slideOffset * 5)
    }
  }

  override fun onFocusChange(v: View?, hasFocus: Boolean) {
    v?.let {
      if (it.edt_search_title != null && hasFocus) {
        navigator?.expandSearchTitleField()
        return
      }
      if (it.edt_search_id != null && hasFocus) {
        navigator?.expandSearchIdField()
        return
      }
    }
  }

}