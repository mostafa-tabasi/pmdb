package com.mstf.pmdb.ui.main.archive

import android.os.Bundle
import android.view.View
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
import com.mstf.pmdb.utils.AppConstants.DISPLAY_TYPE_LIST
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
          DISPLAY_TYPE_LIST -> {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = listAdapter
            setPadding(0, 0, 0, 10)
          }
          // نمایش بصورت tile
          else -> {
            layoutManager = StaggeredGridLayoutManager(
              if (requireActivity().isTablet()) 6 else 4,
              StaggeredGridLayoutManager.HORIZONTAL
            )
            adapter = tilesAdapter
            setPadding(0, 0, 0, requireActivity().actionBarSize())
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
    val action = ArchiveFragmentDirections.actionArchiveFragmentToSearchDialog()
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
    return viewModel.addOrRemoveMovieFromSelectedList(movieId)
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