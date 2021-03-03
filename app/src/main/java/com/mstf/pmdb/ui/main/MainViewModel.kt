package com.mstf.pmdb.ui.main

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<MainNavigator>(dataManager), NavController.OnDestinationChangedListener {

  val currentPage = ObservableField<Int>().apply { set(0) }
  val isBottomToolbarVisible = ObservableField<Boolean>().apply { set(true) }

  override fun onDestinationChanged(
    controller: NavController,
    destination: NavDestination,
    arguments: Bundle?
  ) {
    currentPage.set(
      when (destination.label) {
        "Home" -> 0
        "Archive" -> 1
        "Settings" -> 2
        else -> currentPage.get()
      }
    )

    isBottomToolbarVisible.set(
      when (destination.label) {
        "Home", "Archive", "Settings" -> true
        else -> false
      }
    )
  }
}