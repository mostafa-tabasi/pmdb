package com.mstf.pmdb.ui.main.home.add_movie_dialog

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewTreeObserver
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mstf.pmdb.BR
import com.mstf.pmdb.R
import com.mstf.pmdb.data.model.api.MatchedMovieCompact
import com.mstf.pmdb.databinding.DialogAddMovieBinding
import com.mstf.pmdb.ui.base.BaseBottomSheetDialog
import com.mstf.pmdb.ui.main.home.add_movie_dialog.adapter.MatchedMoviesAdapter
import com.mstf.pmdb.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_find_movie.view.*


@AndroidEntryPoint
class AddMovieDialog :
  BaseBottomSheetDialog<DialogAddMovieBinding, AddMovieViewModel>(),
  AddMovieNavigator, View.OnFocusChangeListener {

  override val viewModel: AddMovieViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.dialog_add_movie

  // هدر موجود در فرم اطلاعات فیلم درحال نمایش است یا خیر
  private var isMovieFormHeaderToolbarVisible = false
  private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
  private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
    it?.let { viewModel.setMoviePoster(it.toString()) }
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
      // لایه ی فرم خالی بصورت پیش فرض دیده نمیشود
      it.includeMovieForm.root.gone()
      // لایه ی نتیجه ی فیلم جستجو شده بصورت پیش فرض دیده نمیشود
      it.includeMatchedMovieList.root.gone()

      it.includeFindMovie.edtSearchTitle.onFocusChangeListener = this
      it.includeFindMovie.edtSearchId.onFocusChangeListener = this

      it.includeMovieForm.imgMoviePoster.setOnClickListener { getContent.launch("image/*") }
    }

    setUpMatchedMovieList()
    setUpMovieFormHeaderToolbar()
    setUpBottomSheet()
    setUpMovieGenres(viewModel.getGenreItems())
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

  /**
   *هندل کردن تولبار موجود در هدرِ فرم مربوط به وارد کردن اطلاعات فیلم
   */
  private fun setUpMovieFormHeaderToolbar() {
    viewDataBinding?.let { it ->
      with(it.includeMovieForm) {
        layoutScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->

          val location = IntArray(2)
          edtTitle.getLocationInWindow(location)
          // قسمت پایینیِ فیلد مربوط به عنوان فیلم
          val y = location[1]
          // قسمت بالایی فیلد مربوط به عنوان فیلم
          val titleFieldTopLocation = y - (edtTitle.height / 1.35)

          // اگر قسمت بالایی عنوان فیلم از تصویر بیرون رفت و عنوان خالی نبود، هدر نمایش داده شود
          if (titleFieldTopLocation < 0 && !isMovieFormHeaderToolbarVisible &&
            !viewModel!!.movie.title.get().isNullOrEmptyAfterTrim()
          ) {
            isMovieFormHeaderToolbarVisible = true
            layoutHeaderToolbar.visible()
            //بعد از اتمام انیمیشن، عنوان فیلم به حالت selected در میاد
            // تا درصورتِ طولانی بودن، تمام محتوا همراه با انیمیشن نمایش داده شود
            txtTitle.isSelected = true
          } else if (titleFieldTopLocation > 0 && isMovieFormHeaderToolbarVisible) {
            layoutHeaderToolbar.gone()
            isMovieFormHeaderToolbarVisible = false
          }
        })
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
            viewModel.clearForm()
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
    val animDuration = 300L
    // تغییر ارتفاع باتم شیت به اندازه ی لایه ای که میخواهیم نمایش دهیم (همراه با انیمیشن)
    ValueAnimator.ofInt(
      bottomSheetBehavior.peekHeight,
      layoutToShow.height
    ).apply {
      duration = animDuration
      addUpdateListener { valueAnimator ->
        bottomSheetBehavior.peekHeight = valueAnimator.animatedValue as Int
      }
    }.start()

    // نمایش لایه ی موردنظر و محو شدن لایه ی دیگر (همراه با انیمیشن)
    layoutToShow.animateAlpha(0F, 1F, animDuration)
    layoutToHide.animateAlpha(1F, 0F, animDuration, afterAnimate = { layoutToHide.gone() })
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

  override fun setUpMovieGenres(items: List<String>) {
    viewDataBinding?.let {
      val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
        requireContext(),
        android.R.layout.simple_dropdown_item_1line,
        items
      )
      with(it.includeMovieForm.edtGenre) {
        setAdapter(adapter)
        setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        threshold = 1
        onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, pos, _ ->
          clear()
          // عنوان ژانر انتخاب شده
          val label: String = adapterView.getItemAtPosition(pos) as String
          viewModel.onGenreSelect(label)
        }
      }
    }
  }

  override fun addGenreChip(label: String, animate: Boolean) {
    viewDataBinding?.let {
      it.includeMovieForm.layoutGenreChips.addChip(label, true, animate) {
        viewModel.removeGenre("$it,")
      }
      // اگر انیمیشن فعال بود، بعد از اضافه شدن چیپِ جدید، به سمت راست اسکرول، تا چیپ دیده شود
      if (animate) Handler().postDelayed({
        it.includeMovieForm.layoutGenreChipsParent.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
      }, 200L)
    }
  }

  override fun removeGenreChip(label: String) {
    viewDataBinding?.includeMovieForm?.layoutGenreChips?.removeChip(label)
  }

  override fun removeAllGenreChips() {
    viewDataBinding?.let {
      it.includeMovieForm.layoutGenreChips.removeAllChips()
      it.includeMovieForm.layoutGenres.invalidate()
    }
  }

  override fun onDestroyView() {
    view?.viewTreeObserver?.addOnGlobalLayoutListener(null)
    super.onDestroyView()
  }
}