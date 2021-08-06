package com.mstf.pmdb.ui.main.archive.search_in_archive_dialog

import android.widget.RadioGroup
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mstf.pmdb.R
import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.ui.base.BaseViewModel
import com.mstf.pmdb.utils.enums.ArchiveDisplayType
import com.mstf.pmdb.utils.enums.MediaFilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchInArchiveViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<SearchInArchiveNavigator>(dataManager) {

  private val _displayType = MutableLiveData(ArchiveDisplayType.TILE)
  val displayType: LiveData<ArchiveDisplayType> = _displayType

  private val _filterType = MutableLiveData(MediaFilterType.BOTH)
  val filterType: LiveData<MediaFilterType> = _filterType

  val title = ObservableField<String>()

  val fromYear = ObservableField<String>()

  val toYear = ObservableField<String>()

  val director = ObservableField<String>()

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
    title?.let { this.title.set(it) }
  }

  fun setFromYear(year: Int) {
    if (year != -1) this.fromYear.set(year.toString())
  }

  fun setToYear(year: Int) {
    if (year != -1) this.toYear.set(year.toString())
  }

  fun setDirector(director: String?) {
    director?.let { this.director.set(it) }
  }

  fun clearTitle() {
    this.title.set("")
  }

  fun clearYear() {
    this.fromYear.set("")
    this.toYear.set("")
  }

  fun clearDirector() {
    this.director.set("")
  }

  fun clearAll() {
    clearTitle()
    clearYear()
    clearDirector()
  }
}