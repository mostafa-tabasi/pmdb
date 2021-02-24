package com.mstf.basekotlinmvvm.ui.main.settings

import com.mstf.basekotlinmvvm.data.DataManager
import com.mstf.basekotlinmvvm.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<SettingsNavigator>(dataManager) {
  // TODO: Implement the ViewModel
}