package com.mstf.pmdb.ui.main.home.add_movie_dialog

import android.animation.Animator
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
import com.mstf.pmdb.data.model.api.MatchedMovieCompact
import com.mstf.pmdb.databinding.DialogAddMovieBinding
import com.mstf.pmdb.ui.base.BaseBottomSheetDialog
import com.mstf.pmdb.ui.main.home.add_movie_dialog.adapter.MatchedMoviesAdapter
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
  private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

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
      // لایه ی فرم خالی بصورت پیش فرض دیده نمیشود
      it.includeMovieForm.root.gone()
      // لایه ی نتیجه ی فیلم جستجو شده بصورت پیش فرض دیده نمیشود
      it.includeMatchedMovieList.root.gone()

      it.includeFindMovie.edtSearchTitle.onFocusChangeListener = this
      it.includeFindMovie.edtSearchId.onFocusChangeListener = this
    }

    setUpMatchedMovieList()
    setUpBottomSheet()
  }

  private fun setUpMatchedMovieList() {
    viewDataBinding?.let {
      with(MatchedMoviesAdapter(arrayListOf())) {
        setListener(object : MatchedMoviesAdapter.Listener {
          override fun onMovieSelect(movie: MatchedMovieCompact, itemPosition: Int) {
            viewModel.getMovieDetails(movie, itemPosition)
          }
        })
        it.includeMatchedMovieList.moviesRecyclerView.adapter = this
      }
    }
  }

  override fun showItemLoadingAtPosition(itemPosition: Int) {
    viewDataBinding?.let {
      (it.includeMatchedMovieList.moviesRecyclerView.findViewHolderForAdapterPosition(itemPosition)
          as MatchedMoviesAdapter.MatchedMovieViewHolder).showLoading(itemPosition)
    }
  }

  override fun hideItemLoadingAtPosition(itemPosition: Int) {
    viewDataBinding?.let {
      (it.includeMatchedMovieList.moviesRecyclerView.findViewHolderForAdapterPosition(itemPosition)
          as MatchedMoviesAdapter.MatchedMovieViewHolder).hideLoading(itemPosition)
    }
  }

  override fun showFormLayout() {
    // باتم شیت کامل باز شود تا لایه ی مربوط به فرم نمایش داده شود
    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
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
          bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
          bottomSheet.layoutParams.height = MATCH_PARENT
          bottomSheetBehavior.peekHeight = viewDataBinding?.includeFindMovie?.root!!.height
          bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
        }
      })
    }
  }

  private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

    override fun onStateChanged(bottomSheet: View, newState: Int) {
      viewDataBinding?.let {
        when (newState) {
          BottomSheetBehavior.STATE_EXPANDED -> it.includeFindMovie.root.gone()
          BottomSheetBehavior.STATE_COLLAPSED -> {
            it.includeMovieForm.root.gone()
            viewModel.onFormClear()
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
        it.includeMovieForm.root.visible()
        it.includeFindMovie.root.alpha = 1 - (slideOffset * 5)
        it.includeMovieForm.root.alpha = slideOffset * 5
      }
    }
  }

  override fun showMatchedMovieList() {
    viewDataBinding?.let {
      it.includeMatchedMovieList.root.alpha = 0F
      it.includeMatchedMovieList.root.visible()
      it.includeMatchedMovieList.root.post {
        // باتم شیت قابل درگ کردن نباشد
        bottomSheetBehavior.isDraggable = false
        // نمایش لایه ی نتایج جستجو
        toggleSearchAndResultLayoutVisibility(
          it.includeMatchedMovieList.root,
          it.includeFindMovie.root
        )
      }
    }
  }

  override fun hideMatchedMovieList() {
    viewDataBinding?.let {
      it.includeMatchedMovieList.root.gone()
      it.includeFindMovie.root.visible()
      it.includeFindMovie.root.post {
        // باتم شیت قابل درگ مردن باشد
        bottomSheetBehavior.isDraggable = true
        bottomSheetBehavior.peekHeight = it.includeFindMovie.root.height
      }
    }
  }

  override fun showSearchLayout() {
    viewDataBinding?.let {
      it.includeFindMovie.root.alpha = 0F
      it.includeFindMovie.root.visible()
      it.includeFindMovie.root.post {
        // باتم شیت قابل درگ مردن باشد
        bottomSheetBehavior.isDraggable = true
        // نمایش لایه ی فیلد های جستجو
        toggleSearchAndResultLayoutVisibility(
          it.includeFindMovie.root,
          it.includeMatchedMovieList.root
        )
      }
    }
  }

  /**
   * تغییر وضعیت نمایش لایه های موردنظر همراه با انیمیشن
   *
   * @param layoutToShow لایه ای که میخواهیم نمایش داده شود
   * @param layoutToHide لایه ای که میخواهیم محو شود
   */
  private fun toggleSearchAndResultLayoutVisibility(layoutToShow: View, layoutToHide: View) {
    // تغییر ارتفاع باتم شیت به اندازه ی لایه ای که میخواهیم نمایش دهیم (همراه با انیمیشن)
    ValueAnimator.ofInt(
      bottomSheetBehavior.peekHeight,
      layoutToShow.height
    ).apply {
      duration = 300
      addUpdateListener { valueAnimator ->
        bottomSheetBehavior.peekHeight = valueAnimator.animatedValue as Int
      }
    }.start()

    // نمایش لایه ی موردنظر و محو شدن لایه ی دیگر (همراه با انیمیشن)
    ValueAnimator.ofFloat(0F, 1F).apply {
      duration = 300
      addUpdateListener { valueAnimator ->
        layoutToShow.alpha = valueAnimator.animatedValue as Float
        layoutToHide.alpha = 1 - (valueAnimator.animatedValue as Float)
      }
      addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(p0: Animator?) {
        }

        override fun onAnimationEnd(p0: Animator?) {
          layoutToHide.gone()
        }

        override fun onAnimationCancel(p0: Animator?) {
        }

        override fun onAnimationRepeat(p0: Animator?) {
        }
      })
    }.start()
  }

  /**
   * هندل کردن عملیات لازم هنگام فکوس شدن روی ویوهای موردنظر
   */
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