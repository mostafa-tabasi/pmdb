package com.mstf.basekotlinmvvm.ui.main.archive

import com.mstf.basekotlinmvvm.data.DataManager
import com.mstf.basekotlinmvvm.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArchiveViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<ArchiveNavigator>(dataManager) {
}