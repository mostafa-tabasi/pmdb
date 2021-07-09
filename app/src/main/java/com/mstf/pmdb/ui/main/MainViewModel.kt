package com.mstf.pmdb.ui.main

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.mstf.pmdb.R
import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<MainNavigator>(dataManager), NavController.OnDestinationChangedListener {

  val currentPage = MutableLiveData(dataManager.getString(R.string.home_label))
  val isBottomToolbarVisible = ObservableField<Boolean>().apply { set(true) }

  override fun onDestinationChanged(
    controller: NavController,
    destination: NavDestination,
    arguments: Bundle?
  ) {
    navigator?.showBottomBar()
    currentPage.postValue(destination.label.toString())
    isBottomToolbarVisible.set(
      when (destination.label) {
        dataManager.getString(R.string.home_label),
        dataManager.getString(R.string.archive_label),
        dataManager.getString(R.string.settings_label) -> true
        else -> false
      }
    )
  }
}