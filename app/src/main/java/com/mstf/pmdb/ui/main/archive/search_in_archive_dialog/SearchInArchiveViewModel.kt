package com.mstf.pmdb.ui.main.archive.search_in_archive_dialog

import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchInArchiveViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<SearchInArchiveNavigator>(dataManager) {

}