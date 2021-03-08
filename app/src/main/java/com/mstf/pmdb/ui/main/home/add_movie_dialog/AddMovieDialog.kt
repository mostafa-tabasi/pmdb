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
import com.mstf.pmdb.utils.extensions.gone
import com.mstf.pmdb.utils.extensions.hideKeyboard
import com.mstf.pmdb.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_find_movie.view.*


@AndroidEntryPoint
class AddMovieDialog :
  BaseBottomSheetDialog<DialogAddMovieBinding, AddMovieViewModel>(),
  AddMovieNavigator, View.OnFocusChangeListener {

  override val viewModel: AddMovieViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.dialog_add_movie

  private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

    override fun onStateChanged(bottomSheet: View, newState: Int) {
      viewDataBinding?.let {
        when (newState) {
          BottomSheetBehavior.STATE_EXPANDED -> it.includeFindMovie.root.gone()
          BottomSheetBehavior.STATE_COLLAPSED -> {
            it.includeFindMovie.edtSearchTitle.requestFocus()
            it.includeBlankForm.root.gone()
          }
        }
      }
    }

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
      // کیبورد در صورت باز بودن، با تغییر وضعیت باتم شیت، بسته شود
      requireContext().hideKeyboard(bottomSheet)
      // اگر جستجویی در حال انجام بود، با تغییر وضعیت باتم شیت، کنسل شود
      viewModel.cancelSearching()
      calculateLayoutsAlpha(slideOffset)
    }

    /**
     * محاسبه ی مقدار نمایش لایه ها متناسب با میزان جابجایی باتم شیت
     *
     * @param slideOffset میزان جابجایی
     */
    private fun calculateLayoutsAlpha(slideOffset: Float) {
      viewDataBinding?.let {
        it.includeFindMovie.root.visible()
        it.includeBlankForm.root.visible()
        it.includeFindMovie.root.alpha = 1 - (slideOffset * 5)
        it.includeBlankForm.root.alpha = slideOffset * 5
      }
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
    viewDataBinding?.let {
      it.includeBlankForm.root.gone()
      it.includeFindMovie.edtSearchTitle.onFocusChangeListener = this
      it.includeFindMovie.edtSearchId.onFocusChangeListener = this
    }

    setUpBottomSheet()
  }

  private fun setUpBottomSheet() {
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
          behavior.addBottomSheetCallback(bottomSheetCallback)
        }
      })
    }
  }

  override fun onFocusChange(v: View?, hasFocus: Boolean) {
    v?.let {
      if (it.edt_search_title != null && hasFocus) {
        // اگر فیلد جستجو براساس عنوان انتخاب شده بود، فیلد شناسه خالی میشود
        viewModel.clearMovieId()
        prepareSearchTitleField()
        return
      }
      if (it.edt_search_id != null && hasFocus) {
        // اگر فیلد جستجو براساس شناسه انتخاب شده بود، فیلد عنوان خالی میشود
        viewModel.clearMovieTitle()
        prepareSearchIdField()
        return
      }
    }
  }

  /**
   * آماده کردن فیلدِ جستجو براساس عنوان فیلم
   */
  override fun prepareSearchTitleField() {
    viewDataBinding?.let {
      it.includeFindMovie.edtSearchId.hint = "ID"
      expandTextField(it.includeFindMovie.edtSearchTitle, it.includeFindMovie.edtSearchId)
    }
  }

  /**
   * آماده کردن فیلدِ جستجو براساس شناسه ی فیلم در سایت imdb
   */
  override fun prepareSearchIdField() {
    viewDataBinding?.let {
      it.includeFindMovie.edtSearchId.hint = "IMDb ID"
      expandTextField(it.includeFindMovie.edtSearchId, it.includeFindMovie.edtSearchTitle)
    }
  }

  /**
   * تغییر فضای فیلد موردنظر همراه با انیمیشن
   *
   * @param viewToExpand فیلدی که قرار است بزرگتر شود
   * @param viewToCollapse فیلدی که قرار است کوچکتر شود
   */
  private fun expandTextField(viewToExpand: EditText, viewToCollapse: EditText) {
    val animator =
      ValueAnimator.ofFloat((viewToExpand.layoutParams as LinearLayout.LayoutParams).weight, 3f)
    with(animator) {
      duration = 150
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

  override fun onDestroyView() {
    view?.viewTreeObserver?.addOnGlobalLayoutListener(null)
    super.onDestroyView()
  }
}