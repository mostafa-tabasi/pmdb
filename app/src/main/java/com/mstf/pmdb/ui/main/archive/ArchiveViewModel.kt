package com.mstf.pmdb.ui.main.archive

import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArchiveViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<ArchiveNavigator>(dataManager) {
}