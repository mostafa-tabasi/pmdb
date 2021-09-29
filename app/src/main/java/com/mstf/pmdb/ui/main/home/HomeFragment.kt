package com.mstf.pmdb.ui.main.home

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.mstf.pmdb.BR
import com.mstf.pmdb.R
import com.mstf.pmdb.databinding.FragmentHomeBinding
import com.mstf.pmdb.ui.base.BaseFragment
import com.mstf.pmdb.ui.main.MainActivity
import com.mstf.pmdb.ui.main.archive.ArchiveListener
import com.mstf.pmdb.ui.main.archive.adapter.TilesArchiveAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(), HomeNavigator,
  ArchiveListener {

  override val viewModel: HomeViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.fragment_home

  @Inject
  lateinit var recentlyAddedMoviesAdapter: TilesArchiveAdapter

  @Inject
  lateinit var recentlyAddedSeriesAdapter: TilesArchiveAdapter

  @Inject
  lateinit var topRatedAdapter: TilesArchiveAdapter

  @Inject
  lateinit var recentlyWatchedAdapter: TilesArchiveAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.setNavigator(this)
    setUp()
  }

  private fun setUp() {
    setUpRecentMovies()
    setUpRecentSeries()
    setUpTopRated()
    setUpRecentWatch()
    viewDataBinding?.let {
      it.addFirstMovie.setOnClickListener { openAddMovieDialog() }
      it.changeSettings.setOnClickListener { goToSettings() }
      it.scrollRoot.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
        if (scrollY > oldScrollY) {
          (requireActivity() as MainActivity).hideBottomBar()
        } else {
          (requireActivity() as MainActivity).showBottomBar()
        }
      })
    }
  }

  /**
   * راه اندازی لیست فیلم های اخیرا اضافه شده
   */
  private fun setUpRecentMovies() {
    if (!viewModel.isRecentMoviesEnable.get()) return

    viewDataBinding?.let {
      recentlyAddedMoviesAdapter.setListener(this)
      it.recentlyAddedMovies.adapter = recentlyAddedMoviesAdapter
      GravitySnapHelper(Gravity.START).apply { attachToRecyclerView(it.recentlyAddedMovies) }
      viewModel.recentlyAddedMovies.observe(viewLifecycleOwner, { items ->
        recentlyAddedMoviesAdapter.submitList(items)
      })
    }
  }

  /**
   * راه اندازی لیست سریال های اخیرا اضافه شده
   */
  private fun setUpRecentSeries() {
    if (!viewModel.isRecentSeriesEnable.get()) return

    viewDataBinding?.let {
      recentlyAddedSeriesAdapter.setListener(this)
      it.recentlyAddedSeries.adapter = recentlyAddedSeriesAdapter
      GravitySnapHelper(Gravity.START).apply { attachToRecyclerView(it.recentlyAddedSeries) }
      viewModel.recentlyAddedSeries.observe(viewLifecycleOwner, { items ->
        recentlyAddedSeriesAdapter.submitList(items)
      })
    }
  }

  /**
   * راه اندازی لیست فیلم و سریال های با ترتیب امتیاز (نزولی)
   */
  private fun setUpTopRated() {
    if (!viewModel.isTopRatedEnable.get()) return

    viewDataBinding?.let {
      topRatedAdapter.setListener(this)
      viewModel.topRatedMethod?.let { site -> topRatedAdapter.ratingSite = site }
      it.topRated.adapter = topRatedAdapter
      GravitySnapHelper(Gravity.START).apply { attachToRecyclerView(it.topRated) }
      viewModel.topRated.observe(viewLifecycleOwner, { items ->
        topRatedAdapter.submitList(items)
      })
    }
  }

  /**
   * راه اندازی لیست فیلم و سریال های اخیرا دیده شده
   */
  private fun setUpRecentWatch() {
    if (!viewModel.isRecentlyWatchedEnable.get()) return

    viewDataBinding?.let {
      recentlyWatchedAdapter.setListener(this)
      it.recentlyWatched.adapter = recentlyWatchedAdapter
      GravitySnapHelper(Gravity.START).apply { attachToRecyclerView(it.recentlyWatched) }
      viewModel.recentlyWatched.observe(viewLifecycleOwner, { items ->
        recentlyWatchedAdapter.submitList(items)
      })
    }
  }

  /**
   * نمایش دیالوگ افزودن فیلم جدید
   */
  fun openAddMovieDialog() {
    val action = HomeFragmentDirections.actionHomeFragmentToAddMovieDialog()
    findNavController().navigate(action)
  }

  /**
   * نمایش صفحه ی تنظیمات
   */
  private fun goToSettings() {
    (requireActivity() as MainActivity).changeBottomNavigationTab(R.id.settingsFragment)
  }

  override fun onResume() {
    super.onResume()
    viewModel.refresh()
  }

  override fun refreshPage() {
    (requireActivity() as MainActivity).changeBottomNavigationTab(R.id.homeFragment)
  }

  // متغیری جهت جلوگیری از باز شدن همزمان چند باتم شیت
  private var preventMultiOpen = false

  override fun onMovieTapped(movieId: Long): Boolean {
    if (preventMultiOpen) return false
    preventMultiOpen = true
    // 500 میلی ثانیه بعد از نمایش باتم شیت، امکان نمایش مجدد فراهم شود
    Handler().postDelayed({ preventMultiOpen = false }, 500)
    openArchiveItemSummaryDialog(movieId)

    return false
  }

  override fun onMovieLongTouch(movieId: Long): Boolean {
    return false
  }

  /**
   * نمایش دیالوگ خلاصه ی اطلاعات آیتم موردنظر در آرشیو
   */
  private fun openArchiveItemSummaryDialog(movieId: Long) {
    val action = HomeFragmentDirections.actionHomeFragmentToArchiveItemSummaryDialog(
      archiveItemId = movieId
    )
    findNavController().navigate(action)
  }

  companion object {
    const val TAG = "HomeFragment"
    fun newInstance(): HomeFragment {
      val args = Bundle()
      val fragment = HomeFragment()
      fragment.arguments = args
      return fragment
    }
  }

}