package com.pmdb.android.ui.main.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.pmdb.android.BR
import com.pmdb.android.R
import com.pmdb.android.databinding.FragmentHomeBinding
import com.pmdb.android.ui.base.BaseFragment
import com.pmdb.android.ui.main.MainActivity
import com.pmdb.android.ui.main.archive.ArchiveListener
import com.pmdb.android.ui.main.archive.adapter.TilesArchiveAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(), HomeNavigator,
  ArchiveListener {

  companion object {
    const val TAG = "HomeFragment"
  }

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
    setUpBottomBarVisibility()
    viewDataBinding?.let {
      it.addFirstMovie.setOnClickListener { onAddFirstMovieClick() }
      it.changeSettings.setOnClickListener { goToSettings() }
    }
  }

  /**
   * راه اندازی لیست فیلم های اخیرا اضافه شده
   */
  private fun setUpRecentMovies() {
    viewDataBinding?.let {
      recentlyAddedMoviesAdapter.setListener(this)
      it.recentlyAddedMovies.adapter = recentlyAddedMoviesAdapter
      GravitySnapHelper(Gravity.START).apply { attachToRecyclerView(it.recentlyAddedMovies) }
      viewModel.recentlyAddedMovies.observe(viewLifecycleOwner) { items ->
        recentlyAddedMoviesAdapter.submitList(items)
      }
    }
  }

  /**
   * راه اندازی لیست سریال های اخیرا اضافه شده
   */
  private fun setUpRecentSeries() {
    viewDataBinding?.let {
      recentlyAddedSeriesAdapter.setListener(this)
      it.recentlyAddedSeries.adapter = recentlyAddedSeriesAdapter
      GravitySnapHelper(Gravity.START).apply { attachToRecyclerView(it.recentlyAddedSeries) }
      viewModel.recentlyAddedSeries.observe(viewLifecycleOwner) { items ->
        recentlyAddedSeriesAdapter.submitList(items)
      }
    }
  }

  /**
   * راه اندازی لیست فیلم و سریال های با ترتیب امتیاز (نزولی)
   */
  private fun setUpTopRated() {
    viewDataBinding?.let {
      topRatedAdapter.setListener(this)
      // سایتی که معیار امتیاز فیلم ها قرار است باشد
      viewModel.topRatedMethod.observe(
        viewLifecycleOwner
      ) { site -> topRatedAdapter.ratingSite = site }
      it.topRated.adapter = topRatedAdapter
      GravitySnapHelper(Gravity.START).apply { attachToRecyclerView(it.topRated) }
      viewModel.topRated.observe(viewLifecycleOwner) { items ->
        topRatedAdapter.submitList(items)
      }
    }
  }

  /**
   * راه اندازی لیست فیلم و سریال های اخیرا دیده شده
   */
  private fun setUpRecentWatch() {
    viewDataBinding?.let {
      recentlyWatchedAdapter.setListener(this)
      it.recentlyWatched.adapter = recentlyWatchedAdapter
      GravitySnapHelper(Gravity.START).apply { attachToRecyclerView(it.recentlyWatched) }
      viewModel.recentlyWatched.observe(viewLifecycleOwner) { items ->
        recentlyWatchedAdapter.submitList(items)
      }
    }
  }

  /**
   * هندل کردن وضعیت نمایش نوار پایین صفحه متناسب با اسکرول لیست
   */
  private fun setUpBottomBarVisibility() {
    viewDataBinding?.scrollRoot?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
      if (scrollY > oldScrollY) (requireActivity() as MainActivity).hideBottomBar()
      else (requireActivity() as MainActivity).showBottomBar()
    })
  }

  private fun onAddFirstMovieClick() {
    // اگر مشکلی در اتصال اینترنت وجود داشت، خطای آن چشمک بزند و فرایند ادامه پیدا نکند
    with((requireActivity() as MainActivity)) {
      if (isConnectionErrorShowing()) {
        blinkConnectionError()
        return
      }
    }
    openAddMovieDialog()
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

  // متغیری جهت جلوگیری از باز شدن همزمان چند باتم شیت
  private var preventMultiOpen = false

  override fun onMovieTapped(movieId: Long): Boolean {
    if (preventMultiOpen) return false
    preventMultiOpen = true
    // 500 میلی ثانیه بعد از نمایش باتم شیت، امکان نمایش مجدد فراهم شود
    Handler(Looper.getMainLooper()).postDelayed({ preventMultiOpen = false }, 500)
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

}