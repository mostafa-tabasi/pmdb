package com.pmdb.android.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.animation.AlphaAnimation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
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
import com.pmdb.android.utils.extensions.gone
import com.pmdb.android.utils.extensions.visible
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
    registerNetworkStateReceiver()
  }

  private val networkStateChangeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      viewModel.onConfigRefreshClick()
    }
  }

  /**
   * شروع به رصدِ تغییرات مربوط به اتصال اینترنت
   */
  private fun registerNetworkStateReceiver() {
    val intentFilter = IntentFilter()
    intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
    registerReceiver(networkStateChangeReceiver, intentFilter)
  }

  override fun onPause() {
    super.onPause()
    unregisterReceiver(networkStateChangeReceiver)
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
      setupNetworkState()
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

  private var isConnectionErrorShowing = false
  fun isConnectionErrorShowing() =
    isConnectionErrorShowing || viewModel.configRefreshLoading.value == true

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
          is HomeFragment -> {
            // اگر خطای مربوط به کانکشن درحال نمایش بود، کاربر امکان اضافه کردن فیلم ندارد
            if (isConnectionErrorShowing()) blinkConnectionError()
            else openAddMovieDialog()
          }
          is ArchiveFragment -> openSearchInArchiveDialog()
          is SettingsFragment -> openAboutDialog()
        }
      }
    }
  }

  private fun setupNetworkState() {
    viewDataBinding?.let { view ->
      lifecycleScope.launch {
        viewModel.showConnectionError.collectLatest {
          isConnectionErrorShowing = it
          if (it) view.connectionErrorText.visible() else view.connectionErrorText.gone()
        }
      }
      viewModel.configRefreshLoading.observe(this) {
        if (it) view.connectionCheckLoading.visible() else view.connectionCheckLoading.gone()
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

  /**
   * چشمک زدن لایه ی مربوط به خطای اینترنت جهت تاکید
   */
  fun blinkConnectionError() {
    viewDataBinding?.connectionErrorLayout?.let {
      val animation = AlphaAnimation(0.3f, 1f)
      animation.duration = 350
      it.startAnimation(animation)
    }
  }
}