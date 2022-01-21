package com.pmdb.android.ui.main

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.pmdb.android.R
import com.pmdb.android.data.DataManager
import com.pmdb.android.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<MainNavigator>(dataManager), NavController.OnDestinationChangedListener {

  private val _currentPage = MutableLiveData(dataManager.getString(R.string.home_label))
  val currentPage: LiveData<String> = _currentPage

  private val _isBottomToolbarVisible = MutableLiveData(true)
  val isBottomToolbarVisible: LiveData<Boolean> = _isBottomToolbarVisible

  override fun onDestinationChanged(
    controller: NavController,
    destination: NavDestination,
    arguments: Bundle?
  ) {
    navigator?.showBottomBar()
    _currentPage.value = destination.label.toString()
    _isBottomToolbarVisible.value = when (destination.label) {
      dataManager.getString(R.string.home_label),
      dataManager.getString(R.string.archive_label),
      dataManager.getString(R.string.settings_label) -> true
      else -> false
    }
  }
}