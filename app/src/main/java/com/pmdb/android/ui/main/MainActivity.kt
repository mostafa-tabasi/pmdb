package com.pmdb.android.ui.main

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.pmdb.android.BR
import com.pmdb.android.R
import com.pmdb.android.data.local.prefs.PreferencesHelper
import com.pmdb.android.databinding.ActivityMainBinding
import com.pmdb.android.ui.base.BaseActivity
import com.pmdb.android.ui.main.archive.ArchiveFragment
import com.pmdb.android.ui.main.home.HomeFragment
import com.pmdb.android.ui.main.settings.SettingsFragment
import com.pmdb.android.utils.BindingUtils.setAnimatedVisibility
import com.pmdb.android.utils.enums.AppTheme
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator {

  override val viewModel: MainViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.activity_main

  @EntryPoint
  @InstallIn(SingletonComponent::class)
  interface MainActivityEntryPoint {
    fun providePreferencesHelper(): PreferencesHelper
  }

  @Inject
  lateinit var appUpdateManager: AppUpdateManager

  companion object {
    private const val REQUEST_CODE_APP_UPDATE = 100
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    handleAppTheme()
    super.onCreate(savedInstanceState)
    viewModel.setNavigator(this)
    setUp()
  }

  override fun onResume() {
    super.onResume()
    resumeUpdateManager()
  }

  private fun handleAppTheme() {
    val hiltEntryPoint =
      EntryPointAccessors.fromApplication(this, MainActivityEntryPoint::class.java)
    val prefsHelper = hiltEntryPoint.providePreferencesHelper()

    when {
      prefsHelper.isSystemDefaultThemeEnable ->
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
      AppTheme.withId(prefsHelper.appTheme.toInt()) == AppTheme.LIGHT -> {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setTheme(R.style.Theme_PMDb)
      }
      else -> {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setTheme(R.style.Theme_PMDb_dark)
      }
    }
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    recreate()
    super.onConfigurationChanged(newConfig)
  }

  private fun setUp() {
    viewDataBinding?.let {
      setupUpdateManager()
      // راه اندازی کامپوننت navigation
      val navHostFragment =
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
      with(navHostFragment.navController) {
        NavigationUI.setupWithNavController(it.bottomNavigation, this)
        addOnDestinationChangedListener(viewModel)
      }
      // هندل کردن وضعیت نمایش bottomBar و fab همراه با انیمیشن
      viewModel.isBottomToolbarVisible.observe(this) { visible ->
        it.fabMain.setAnimatedVisibility(visible)
        it.bottomBar.setAnimatedVisibility(visible)
      }
      setupFab()
    }
  }

  private fun setupUpdateManager() {
    appUpdateManager.appUpdateInfo.addOnSuccessListener {
      if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
        && it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
      ) {
        appUpdateManager.startUpdateFlowForResult(
          it, AppUpdateType.IMMEDIATE, this,
          REQUEST_CODE_APP_UPDATE
        )
      }
    }
  }

  private fun resumeUpdateManager() {
    appUpdateManager.appUpdateInfo.addOnSuccessListener {
      if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
        appUpdateManager.startUpdateFlowForResult(
          it, AppUpdateType.IMMEDIATE, this,
          REQUEST_CODE_APP_UPDATE
        )
      }
    }
  }

  private fun setupFab() {
    viewModel.currentPage.observe(this) {
      viewDataBinding?.fabMain?.setImageResource(
        when (it) {
          getString(R.string.home_label) -> R.drawable.ic_add
          getString(R.string.archive_label) -> R.drawable.ic_search
          getString(R.string.settings_label) -> R.drawable.ic_info
          else -> 0
        }
      )
    }

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