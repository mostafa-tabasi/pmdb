package com.mstf.pmdb.ui.main.home

import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<HomeNavigator>(dataManager) {
  // TODO: Implement the ViewModel
}