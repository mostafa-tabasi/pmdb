package com.mstf.pmdb.ui.main.archive.archive_item_info_dialog

import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mstf.pmdb.BR
import com.mstf.pmdb.R
import com.mstf.pmdb.databinding.DialogArchiveItemInfoBinding
import com.mstf.pmdb.databinding.LayoutMovieFormEditBinding
import com.mstf.pmdb.ui.base.BaseBottomSheetDialog
import com.mstf.pmdb.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ArchiveItemInfoDialog :
  BaseBottomSheetDialog<DialogArchiveItemInfoBinding, ArchiveItemInfoViewModel>(),
  ArchiveItemInfoNavigator {

  override val viewModel: ArchiveItemInfoViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.dialog_archive_item_info
  private val args: ArchiveItemInfoDialogArgs by navArgs()

  // هدر موجود در فرم اطلاعات فیلم درحال نمایش است یا خیر
  private var isMovieFormHeaderToolbarVisible = false
  private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
  private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
    it?.let { viewModel.setMoviePoster(it.toString()) }
  }

  // لایه ی تاییدیه مربوط به دکمه هایی که قبل از انجام عملکرد نیاز به گرفتن تایید دارند
  // مثل حذف فیلم از آرشیو یا پاک کردن اطلاعات ثبت شده در فرم
  private var confirmLayout: View? = null

  private lateinit var movieFormEditBinding: LayoutMovieFormEditBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.setNavigator(this)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.fetchArchiveItem(args.archiveItemId)
    viewDataBinding?.root?.post { setUp() }
  }

  private fun setUp() {
    viewDataBinding?.let {

      // اضافه کردن لایه ی فرم خالی فیلم
      movieFormEditBinding = DataBindingUtil.inflate(
        layoutInflater,
        R.layout.layout_movie_form_edit,
        it.root as FrameLayout,
        false
      )
      movieFormEditBinding.viewModel = viewModel
      movieFormEditBinding.lifecycleOwner = this
      (it.root as FrameLayout).addView(movieFormEditBinding.root)

      // لایه ی فرم خالی بصورت پیش فرض دیده نمیشود
      movieFormEditBinding.root.gone()

      movieFormEditBinding.imgMoviePoster.setOnClickListener {
        if (viewModel.isEditing.get() == true) getContent.launch("image/*")
      }
      movieFormEditBinding.btnDelete.setOnClickListener { showDeleteConfirm() }
    }

    setUpMovieFormHeaderToolbar()
    setUpBottomSheet()
    setUpMovieGenres()
  }

  /**
   *هندل کردن تولبار موجود در هدرِ فرم مربوط به وارد کردن اطلاعات فیلم
   */
  private fun setUpMovieFormHeaderToolbar() {
    viewDataBinding?.let { it ->
      with(movieFormEditBinding) {
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
            txtHeaderTitle.isSelected = true
          } else if (titleFieldTopLocation > 0 && isMovieFormHeaderToolbarVisible) {
            layoutHeaderToolbar.gone()
            isMovieFormHeaderToolbarVisible = false
          }
        })
      }
    }
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
          bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
          bottomSheetBehavior.peekHeight = viewDataBinding?.includeMovieSummary?.root!!.height
          bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
        }
      })
    }
  }

  private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

    override fun onStateChanged(bottomSheet: View, newState: Int) {
      viewDataBinding?.let {
        when (newState) {
          BottomSheetBehavior.STATE_EXPANDED -> {
            it.includeMovieSummary.root.gone()
            setMovieFormLoadingWidth()
          }
          BottomSheetBehavior.STATE_COLLAPSED -> {
            movieFormEditBinding.root.gone()
            dismissConfirm()
          }
          else -> {
          }
        }
      }
    }

    /**
     * محاسبه و ست کردن عرض مربوط به لودینگ موجود در فرم اطلاعات فیلم
     */
    private fun setMovieFormLoadingWidth() {
      viewDataBinding?.let {
        with(movieFormEditBinding.btnSave) {
          post { movieFormEditBinding.pbLoading.layoutParams.width = width }
        }
      }
    }

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
      // کیبورد در صورت باز بودن، با تغییر وضعیت باتم شیت، بسته شود
      requireContext().hideKeyboard(bottomSheet)
      calculateLayoutsAlpha(slideOffset)
    }

    /**
     * محاسبه ی مقدار نمایش لایه ها متناسب با میزان جابجایی باتم شیت
     *
     * @param slideOffset میزان جابجایی
     */
    private fun calculateLayoutsAlpha(slideOffset: Float) {
      viewDataBinding?.let {
        it.includeMovieSummary.root.visible()
        movieFormEditBinding.root.visible()
        it.includeMovieSummary.root.alpha = 1 - (slideOffset * 5)
        movieFormEditBinding.root.alpha = slideOffset * 5
      }
    }
  }

  private fun setUpMovieGenres() {
    viewDataBinding?.let {
      // رصد لیست ژانرهای مربوط به فیلم جهت نمایش
      viewModel.genres.observe(viewLifecycleOwner) {
        movieFormEditBinding.layoutGenreChips.removeAllViews()
        if (it.isEmpty()) return@observe
        for (genre in it) movieFormEditBinding.layoutGenreChips.addChip(
          genre, viewModel.isEditing.get() == true,
          animate = false
        ) { viewModel.removeGenre(genre) }
        // بعد از اضافه شدن چیپِ جدید، به سمت راست اسکرول، تا چیپ دیده شود
        Handler().postDelayed({
          movieFormEditBinding.layoutGenreChipsParent.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
        }, 200L)
      }

      viewModel.movie.tv.addOnPropertyChangedCallback(object :
        Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
          // فیلد مربوط به وارد کردن ژانرهای فیلم
          val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            viewModel.getGenreItems()
          )
          with(movieFormEditBinding.edtGenre) {
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
      })
    }
  }

  /**
   * نمایش دکمه ی تایید جهت پاک کردن فیلم از آرشیو
   */
  private fun showDeleteConfirm() {
    viewDataBinding?.let {
      showConfirmFor(
        movieFormEditBinding.btnConfirmClearDelete,
        "Delete?",
        R.drawable.bg_confirm_red_button
      ) { viewModel.deleteMovie(); dismissConfirm() }
    }
  }

  /**
   * نمایش عنوان تاییدیه مربوط به دکمه ی موردنظر
   *
   * @param confirmButton دکمه ی تاییدی که عنوان برای آن نمایش داده میشود
   * @param title عنوان تاییدیه
   * @param colorRes رنگ پس زمینه عنوان و دکمه ی تایید
   * @param onConfirm عملیاتی که بعد از تایید باید انجام شود
   */
  private fun showConfirmFor(
    confirmButton: ImageView,
    title: String,
    colorRes: Int,
    onConfirm: () -> Unit
  ) {
    viewDataBinding?.let {

      if (confirmLayout == null) {
        confirmLayout = layoutInflater.inflate(R.layout.layout_confirm_title, null)
        confirmLayout!!.findViewById<ImageView>(R.id.btn_cancel)
          .setOnClickListener { dismissConfirm() }

        // محاسبه ی مقدار margin از پایین مربوط به لایه ی المنت های تاییدیه
        val lp = FrameLayout.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
        )
        val tv = TypedValue()
        if (requireActivity().theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
          val actionBarHeight =
            TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
          lp.bottomMargin = actionBarHeight + 8
        }
        confirmLayout!!.layoutParams = lp
      }

      // اگر لایه ی تاییدیه دیگری درحال نمایش بود، ابتدا حذف شود
      dismissConfirm(false)

      // تنظیمات دکمه ی تایید
      confirmButton.apply {
        alpha = 0F
        background = ContextCompat.getDrawable(requireContext(), colorRes)
        // عملکرد دکمه ی تایید
        setOnClickListener { onConfirm.invoke() }
      }

      // تنظیمات لایه ی عنوان و کنسل تاییدیه
      confirmLayout!!.apply {
        alpha = 0F
        findViewById<TextView>(R.id.txt_title).apply {
          // عنوان تاییدیه
          text = title
          background = ContextCompat.getDrawable(requireContext(), colorRes)
        }
      }

      // افزودن لایه ی تاییدیه به صفحه
      movieFormEditBinding.layoutMovieForm.addView(confirmLayout)

      //نمایش لایه ی عنوان تاییدیه و دکمه ی کنسل همراه با انیمیشن
      with(confirmLayout!!) {
        animateAlpha(0F, 1F, 100, beforeAnimate = {
          visible()
          post { initConfirmLayoutLocation(confirmButton) }
        })
      }

      // نمایش دکمه ی تایید همراه با انیمیشن
      with(confirmButton) {
        animateAlpha(0F, 1F, 100, beforeAnimate = { visible() })
      }
    }
  }

  /**
   * محاسبه و ست کردن محل قرارگیری لایه ی مربوط به عنوان تاییدیه
   */
  private fun initConfirmLayoutLocation(confirmButton: ImageView) {
    // باید سمت راست عنوان در راستای سمت راست دکمه ی تایید نمایش داده شود و در بالای آن

    // مقدار عرض عنوان را محاسبه میکنیم تا به مقدار اختلافِ آن با دکمه ی تایید، عقب تر از دکمه ی تایید، لایه را نمایش دهیم
    val titleWidth = confirmLayout!!.findViewById<TextView>(R.id.txt_title).width
    val confirmButtonWidth = confirmButton.width

    // محاسبه محل نمایش عنوان تاییدیه
    // متغیری جهت نگهداری موقعیت لایه ای که دکمه ی تایید در آن نمایش داده میشود
    val location = IntArray(2)
    confirmButton.getLocationOnScreen(location)
    // محل قرارگیری دکمه ی تایید (محور x)
    val x = location[0]
    // محل قرارگیری لایه ی عنوان و دکمه ی کنسل
    confirmLayout!!.x = (x - (titleWidth - confirmButtonWidth)).toFloat()
  }

  override fun dismissConfirm(animate: Boolean) {
    viewDataBinding?.let {
      with(movieFormEditBinding.btnConfirmClearDelete) {
        if (!animate) gone()
        else animateAlpha(1F, 0F, 100, afterAnimate = { gone() })
      }
      confirmLayout?.let { layout ->
        if (!animate) movieFormEditBinding.layoutMovieForm.removeView(layout)
        else layout.animateAlpha(1F, 0F, 100, afterAnimate = {
          // بعد از اتمام انیمیشن، لایه ی مربوط به عنوان تاییدیه حذف شود
          movieFormEditBinding.layoutMovieForm.removeView(layout)
        })
      }
    }
  }
}