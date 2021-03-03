package com.mstf.pmdb.ui.sample.dialog

import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SampleBottomSheetDialogViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<SampleBottomSheetDialogNavigator?>(dataManager) {

  fun onNavBackClick() {
    navigator?.goBack()
  }
}