package com.mstf.pmdb.ui.sample.activity

import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SampleActivityViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<SampleActivityNavigator>(dataManager) {
}