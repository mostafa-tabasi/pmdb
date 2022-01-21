package com.pmdb.android.ui.main.archive

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pmdb.android.BR
import com.pmdb.android.R
import com.pmdb.android.databinding.FragmentArchiveBinding
import com.pmdb.android.ui.base.BaseFragment
import com.pmdb.android.ui.main.MainActivity
import com.pmdb.android.ui.main.archive.adapter.ListArchiveAdapter
import com.pmdb.android.ui.main.archive.adapter.TilesArchiveAdapter
import com.pmdb.android.ui.main.archive.search_in_archive_dialog.SearchInArchiveDialog
import com.pmdb.android.utils.AppConstants.BUNDLE_KEY_DIRECTOR
import com.pmdb.android.utils.AppConstants.BUNDLE_KEY_DISPLAY_TYPE
import com.pmdb.android.utils.AppConstants.BUNDLE_KEY_FILTER_TYPE
import com.pmdb.android.utils.AppConstants.BUNDLE_KEY_FROM_YEAR
import com.pmdb.android.utils.AppConstants.BUNDLE_KEY_MOVIE_TITLE
import com.pmdb.android.utils.AppConstants.BUNDLE_KEY_TO_YEAR
import com.pmdb.android.utils.enums.ArchiveDisplayType
import com.pmdb.android.utils.enums.MediaFilterType
import com.pmdb.android.utils.extensions.actionBarSize
import com.pmdb.android.utils.extensions.isTablet
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArchiveFragment : BaseFragment<FragmentArchiveBinding, ArchiveViewModel>(), ArchiveNavigator,
  ArchiveListener {

  override val viewModel: ArchiveViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.fragment_archive

  @Inject
  lateinit var tilesAdapter: TilesArchiveAdapter

  @Inject
  lateinit var listAdapter: ListArchiveAdapter

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
      it.addFirstMovie.setOnClickListener { openAddMovieDialog() }
      it.changeFilter.setOnClickListener { openSearchInArchiveDialog() }
      /*  TODO: handle multi select for next release
      it.clearSelectedItems.setOnClickListener {
        viewModel.clearSelectedMovies()
        setItemElevationToDefault()
      }
      */
      setUpList(it)
    }
  }

  private fun setUpList(view: FragmentArchiveBinding) {
    tilesAdapter.setListener(this)
    listAdapter.setListener(this)
    // ست کردن امتیاز پیش فرضی که برای هر آیتم باید نمایش داده شود
    viewModel.defaultItemRate?.let { site ->
      tilesAdapter.ratingSite = site
      listAdapter.setRatingSite(site)
    }
    // دیتای موردنظر جهت نمایش در لیست
    viewModel.movies.observe(viewLifecycleOwner, {
      tilesAdapter.submitList(it)
      listAdapter.submitList(it)
    })
    // تغییر نوع نمایش لیست فیلم ها
    viewModel.displayType.observe(viewLifecycleOwner, { type ->
      (requireActivity() as MainActivity).showBottomBar()
      with(view.movies) {
        when (type) {
          // نمایش بصورت لیست
          ArchiveDisplayType.LIST -> {
            layoutManager =
              GridLayoutManager(requireContext(), if (requireActivity().isTablet()) 2 else 1)
            adapter = listAdapter
            setPadding(0, 0, 0, 10)
          }
          // نمایش بصورت tile
          ArchiveDisplayType.TILE -> {
            layoutManager = StaggeredGridLayoutManager(
              if (requireActivity().isTablet()) 6 else 4,
              StaggeredGridLayoutManager.HORIZONTAL
            )
            adapter = tilesAdapter
            setPadding(0, 0, 0, requireActivity().actionBarSize())
          }
          else -> {
          }
        }
      }
    })
  }

  /**
   * نمایش دیالوگ افزودن فیلم جدید
   */
  private fun openAddMovieDialog() {
    val action = ArchiveFragmentDirections.actionArchiveFragmentToAddMovieDialog()
    findNavController().navigate(action)
  }

  /**
   * برگرداندنِ مقدار elevation آیتم های انتخاب شده به حالی پیش فرض
   */
  fun setItemElevationToDefault() {
    viewDataBinding?.movies?.let {
      for (i in 0..it.adapter!!.itemCount)
        it.layoutManager?.getChildAt(i)?.let { itemView ->
          if (itemView.isAttachedToWindow) itemView.elevation = 0F
        }
    }
  }

  /**
   * نمایش دیالوگ جهت جستجو در آرشیو فیلم ها
   */
  fun openSearchInArchiveDialog() {
    // دریافت اطلاعات برگشت داده شده از باتم شیت جستجو
    setFragmentResultListener(SearchInArchiveDialog.REQUEST_KEY) { _, bundle ->
      bundle.get(BUNDLE_KEY_DISPLAY_TYPE)
        .let { viewModel.setDisplayType(it as ArchiveDisplayType?) }
      val filterType: MediaFilterType? = bundle.get(BUNDLE_KEY_FILTER_TYPE) as MediaFilterType?
      val movieTitle = bundle.getString(BUNDLE_KEY_MOVIE_TITLE)
      val fromYear = bundle.getString(BUNDLE_KEY_FROM_YEAR)
      val toYear = bundle.getString(BUNDLE_KEY_TO_YEAR)
      val director = bundle.getString(BUNDLE_KEY_DIRECTOR)
      viewModel.filterList(filterType, movieTitle, fromYear, toYear, director)
    }

    val action = ArchiveFragmentDirections.actionArchiveFragmentToSearchDialog(
      displayType = viewModel.displayType.value!!.id,
      filterType = viewModel.filter.value!!.type.id,
      title = viewModel.filter.value!!.title,
      fromYear = viewModel.filter.value!!.fromYear ?: -1,
      toYear = viewModel.filter.value!!.toYear ?: -1,
      director = viewModel.filter.value!!.director,
    )
    findNavController().navigate(action)
  }

  // متغیری جهت جلوگیری از باز شدن همزمان چند باتم شیت
  private var preventMultiOpen = false

  override fun onMovieTapped(movieId: Long): Boolean {
    if (preventMultiOpen) return false
    preventMultiOpen = true
    // 500 میلی ثانیه بعد از نمایش باتم شیت، امکان نمایش مجدد فراهم شود
    Handler().postDelayed({ preventMultiOpen = false }, 500)
    openArchiveItemSummaryDialog(movieId)

    /* TODO: handle multi select for next release
    // اگر در حالت انتخاب فیلم بودیم، با انتخاب هر فیلم، به لیست انتخاب شده ها اضافه یا از آن حذف میشود
    return if (viewModel.isInSelectMode.value == true)
      viewModel.addOrRemoveMovieFromSelectedList(movieId)
    // در غیر اینصورت، باتم شیتِ خلاصه ی اطلاعات فیلم را نمابش میدهیم
    else {
      // TODO: show movie summary bottom sheet
      false
    }
    */

    return false
  }

  override fun onMovieLongTouch(movieId: Long): Boolean {
    //return viewModel.addOrRemoveMovieFromSelectedList(movieId)
    return false
  }

  /**
   * نمایش دیالوگ خلاصه ی اطلاعات آیتم موردنظر در آرشیو
   */
  private fun openArchiveItemSummaryDialog(movieId: Long) {
    val action = ArchiveFragmentDirections.actionArchiveFragmentToArchiveItemSummaryDialog(
      archiveItemId = movieId
    )
    findNavController().navigate(action)
  }

  companion object {
    const val TAG = "ArchiveFragment"
    fun newInstance(): ArchiveFragment {
      val args = Bundle()
      val fragment = ArchiveFragment()
      fragment.arguments = args
      return fragment
    }
  }
}