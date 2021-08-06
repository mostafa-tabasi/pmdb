package com.mstf.pmdb.ui.main.archive

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mstf.pmdb.BR
import com.mstf.pmdb.R
import com.mstf.pmdb.databinding.FragmentArchiveBinding
import com.mstf.pmdb.ui.base.BaseFragment
import com.mstf.pmdb.ui.main.MainActivity
import com.mstf.pmdb.ui.main.archive.adapter.ListArchiveAdapter
import com.mstf.pmdb.ui.main.archive.adapter.TilesArchiveAdapter
import com.mstf.pmdb.ui.main.archive.search_in_archive_dialog.SearchInArchiveDialog
import com.mstf.pmdb.utils.AppConstants.BUNDLE_KEY_DIRECTOR
import com.mstf.pmdb.utils.AppConstants.BUNDLE_KEY_DISPLAY_TYPE
import com.mstf.pmdb.utils.AppConstants.BUNDLE_KEY_FILTER_TYPE
import com.mstf.pmdb.utils.AppConstants.BUNDLE_KEY_FROM_YEAR
import com.mstf.pmdb.utils.AppConstants.BUNDLE_KEY_MOVIE_TITLE
import com.mstf.pmdb.utils.AppConstants.BUNDLE_KEY_TO_YEAR
import com.mstf.pmdb.utils.enums.ArchiveDisplayType
import com.mstf.pmdb.utils.enums.MediaFilterType
import com.mstf.pmdb.utils.extensions.actionBarSize
import com.mstf.pmdb.utils.extensions.isTablet
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

  override fun onMovieTapped(movieId: Long): Boolean {
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