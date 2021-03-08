package com.mstf.pmdb.ui.sample.fragment

import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SampleFragmentViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<SampleFragmentNavigator>(dataManager) {

  fun onNavBackClick() {
    navigator?.goBack()
  }
}