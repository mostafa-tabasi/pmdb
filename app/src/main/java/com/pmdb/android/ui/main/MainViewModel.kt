package com.pmdb.android.ui.main

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.pmdb.android.R
import com.pmdb.android.data.DataManager
import com.pmdb.android.ui.base.BaseViewModel
import com.pmdb.android.utils.AppConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  dataManager: DataManager,
  private val appConfig: AppConfig,
) : BaseViewModel<MainNavigator>(dataManager), NavController.OnDestinationChangedListener {

  private val _currentPage = MutableLiveData(dataManager.getString(R.string.home_label))
  val currentPage: LiveData<String> = _currentPage

  private val _isBottomToolbarVisible = MutableLiveData(true)
  val isBottomToolbarVisible: LiveData<Boolean> = _isBottomToolbarVisible

  private val _configRefreshLoading = MutableLiveData(false)
  val configRefreshLoading: LiveData<Boolean> = _configRefreshLoading

  // ادغام سه شرط مختلف جهت نمایش متن خطای اتصال اینترنت
  val showConnectionError = combine(
    // کاربر با حساب گوگل لاگین کرده است یا خیر
    appConfig.isUserLoggedIn.asFlow(),
    // فایربیس در دسترس است یا خیر
    appConfig.isFirebaseReachable.asFlow(),
    // شرایط بالا در حال بررسی است یا خیر
    configRefreshLoading.asFlow(),
  ) { isUserLoggedIn, isFirebaseReachable, configRefreshLoading ->
    isUserLoggedIn && !isFirebaseReachable && !configRefreshLoading
  }

  init {
    onConfigRefreshClick()
  }

  override fun onDestinationChanged(
    controller: NavController,
    destination: NavDestination,
    arguments: Bundle?
  ) {
    _currentPage.value = destination.label.toString()
    _isBottomToolbarVisible.value = when (destination.label) {
      dataManager.getString(R.string.home_label),
      dataManager.getString(R.string.archive_label),
      dataManager.getString(R.string.settings_label) -> true
      else -> false
    }
  }

  /**
   * بررسی وضعیتِ اینترنت و دسترسی به Firestore
   */
  fun onConfigRefreshClick(withLoading: Boolean = false) {
    if (_configRefreshLoading.value == true) return

    viewModelScope.launch(Dispatchers.IO) {
      if (withLoading) _configRefreshLoading.postValue(true)
      delay(500)
      appConfig.refresh()
      _configRefreshLoading.postValue(false)
    }
  }
}