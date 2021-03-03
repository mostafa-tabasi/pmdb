package com.mstf.pmdb.ui.main.home.add_movie_dialog

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mstf.pmdb.BR
import com.mstf.pmdb.R
import com.mstf.pmdb.databinding.DialogAddMovieBinding
import com.mstf.pmdb.ui.base.BaseBottomSheetDialog
import com.mstf.pmdb.utils.extensions.clear
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddMovieDialog :
  BaseBottomSheetDialog<DialogAddMovieBinding, AddMovieViewModel>(),
  AddMovieNavigator {

  override val viewModel: AddMovieViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.dialog_add_movie

  companion object {
    const val TAG = "AddMovieDialog"
    fun newInstance(): AddMovieDialog {
      val args = Bundle()
      val fragment = AddMovieDialog()
      fragment.arguments = args
      return fragment
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.setNavigator(this)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setUp()
  }

  private fun setUp() {
    viewDataBinding?.includeFindMovie?.edtSearchTitle?.onFocusChangeListener = viewModel
    viewDataBinding?.includeFindMovie?.edtSearchId?.onFocusChangeListener = viewModel

    view?.let {
      it.viewTreeObserver.addOnGlobalLayoutListener(object :
        ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
          it.viewTreeObserver.removeOnGlobalLayoutListener(this)

          val dialog = dialog as BottomSheetDialog
          val bottomSheet =
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
          val behavior = BottomSheetBehavior.from(bottomSheet!!)
          bottomSheet.layoutParams.height = MATCH_PARENT
          behavior.peekHeight = viewDataBinding?.includeFindMovie?.layoutSearch!!.height
          behavior.addBottomSheetCallback(viewModel.bottomSheetCallback)
        }
      })
    }
  }

  override fun onDestroyView() {
    view?.viewTreeObserver?.addOnGlobalLayoutListener(null)
    super.onDestroyView()
  }

  override fun expandSearchTitleField() {
    viewDataBinding?.let {
      it.includeFindMovie.edtSearchId.hint = "ID"
      it.includeFindMovie.edtSearchId.clear()
      expandTextField(it.includeFindMovie.edtSearchTitle, it.includeFindMovie.edtSearchId)
    }
  }

  override fun expandSearchIdField() {
    viewDataBinding?.let {
      it.includeFindMovie.edtSearchId.hint = "IMDb ID"
      it.includeFindMovie.edtSearchTitle.clear()
      expandTextField(it.includeFindMovie.edtSearchId, it.includeFindMovie.edtSearchTitle)
    }
  }

  private fun expandTextField(viewToExpand: EditText, viewToCollapse: EditText) {
    val animator =
      ValueAnimator.ofFloat((viewToExpand.layoutParams as LinearLayout.LayoutParams).weight, 3f)
    with(animator) {
      duration = 50
      addUpdateListener {
        (viewToCollapse.layoutParams as LinearLayout.LayoutParams).weight =
          4 - (it.animatedValue as Float)
        (viewToExpand.layoutParams as LinearLayout.LayoutParams).weight = it.animatedValue as Float

        viewToCollapse.requestLayout()
        viewToExpand.requestLayout()
      }
      start()
    }
  }
}