package com.mstf.pmdb.ui.main.archive.archive_item_info_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.data.model.db.MovieEntity
import com.mstf.pmdb.ui.base.BaseViewModel
import com.mstf.pmdb.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveItemInfoViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<ArchiveItemInfoNavigator>(dataManager) {

  private val _archiveItem = MutableLiveData<MovieEntity>()
  val archiveItem: LiveData<MovieEntity> = _archiveItem

  /**
   * دریافت آیتم موردنظر از آرشیو
   *
   * @param id شناسه ی آیتم موردنظر
   */
  fun fetchArchiveItem(id: Long) {
    viewModelScope.launch {
      _archiveItem.postValue(dataManager.findMovieById(id))
    }
  }

  fun isTv() = _archiveItem.value?.type == AppConstants.MEDIA_TYPE_TITLE_SERIES
}