package com.pmdb.android.ui.main.settings

import com.pmdb.android.data.DataManager
import com.pmdb.android.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<SettingsNavigator>(dataManager) {
  // TODO: Implement the ViewModel
}