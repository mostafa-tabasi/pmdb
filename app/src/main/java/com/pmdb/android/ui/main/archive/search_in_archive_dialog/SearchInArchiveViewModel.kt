package com.pmdb.android.ui.main.archive.search_in_archive_dialog

import android.view.View
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pmdb.android.R
import com.pmdb.android.data.DataManager
import com.pmdb.android.ui.base.BaseViewModel
import com.pmdb.android.utils.enums.ArchiveDisplayType
import com.pmdb.android.utils.enums.MediaFilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchInArchiveViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<SearchInArchiveNavigator>(dataManager) {

  private val _displayType = MutableLiveData(ArchiveDisplayType.TILE)
  val displayType: LiveData<ArchiveDisplayType> = _displayType

  private val _filterType = MutableLiveData(MediaFilterType.BOTH)
  val filterType: LiveData<MediaFilterType> = _filterType

  val title = MutableLiveData<String>()

  val fromYear = MutableLiveData<String>()

  val toYear = MutableLiveData<String>()

  val director = MutableLiveData<String>()

  fun changeDisplayType(type: ArchiveDisplayType) {
    _displayType.value = type
  }

  fun onDisplayTypeChanged(radioGroup: RadioGroup, id: Int) {
    _displayType.value = when (id) {
      R.id.display_list -> ArchiveDisplayType.LIST
      else -> ArchiveDisplayType.TILE
    }
  }

  fun changeFilterType(type: MediaFilterType) {
    _filterType.value = type
  }

  fun onFilterTypeChanged(radioGroup: RadioGroup, id: Int) {
    _filterType.value = when (id) {
      R.id.filter_movies -> MediaFilterType.MOVIES
      R.id.filter_tv -> MediaFilterType.SERIES
      else -> MediaFilterType.BOTH
    }
  }

  fun setTitle(title: String?) {
    title?.let { this.title.value = it }
  }

  fun setFromYear(year: Int) {
    if (year != -1) this.fromYear.value = year.toString()
  }

  fun setToYear(year: Int) {
    if (year != -1) this.toYear.value = year.toString()
  }

  fun setDirector(director: String?) {
    director?.let { this.director.value = it }
  }

  fun clearTitle(v: View? = null) {
    this.title.value = ""
  }

  fun clearYear(v: View? = null) {
    this.fromYear.value = ""
    this.toYear.value = ""
  }

  fun clearDirector(v: View? = null) {
    this.director.value = ""
  }

  fun clearAll(v: View? = null) {
    clearTitle()
    clearYear()
    clearDirector()
  }
}