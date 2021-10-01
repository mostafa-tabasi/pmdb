package com.mstf.pmdb.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.mstf.pmdb.BR
import com.mstf.pmdb.R
import com.mstf.pmdb.databinding.ActivityMainBinding
import com.mstf.pmdb.ui.base.BaseActivity
import com.mstf.pmdb.ui.main.archive.ArchiveFragment
import com.mstf.pmdb.ui.main.home.HomeFragment
import com.mstf.pmdb.ui.main.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator {

  override val viewModel: MainViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.activity_main

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.setNavigator(this)
    setUp()
  }

  private fun setUp() {
    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    with(navHostFragment.navController) {
      NavigationUI.setupWithNavController(viewDataBinding!!.bottomNavigation, this)
      addOnDestinationChangedListener(viewModel)
    }
    setupFab()
  }

  private fun setupFab() {
    viewModel.currentPage.observe(this, {
      viewDataBinding?.fabMain?.setImageResource(
        when (it) {
          getString(R.string.home_label) -> R.drawable.ic_add
          getString(R.string.archive_label) -> R.drawable.ic_search
          getString(R.string.settings_label) -> R.drawable.ic_info
          else -> 0
        }
      )
    })

    viewDataBinding?.fabMain?.setOnClickListener {
      val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
      with(navHostFragment!!.childFragmentManager.fragments[0]) {
        when (this) {
          is HomeFragment -> openAddMovieDialog()
          is ArchiveFragment -> openSearchInArchiveDialog()
          is SettingsFragment -> openAboutDialog()
        }
      }
    }
  }

  override fun showBottomBar() {
    viewDataBinding?.bottomBar?.performShow()
  }

  override fun hideBottomBar() {
    viewDataBinding?.bottomBar?.performHide()
  }

  /**
   * تغییر تب انتخاب شده در صفحه ی اصلی
   *
   * @param tabId شناسه ی تب موردنظر جهت تغییر
   */
  fun changeBottomNavigationTab(tabId: Int) {
    viewDataBinding?.bottomNavigation?.selectedItemId = tabId
  }
}