package com.mstf.pmdb.ui.main.settings

import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<SettingsNavigator>(dataManager) {
  // TODO: Implement the ViewModel
}