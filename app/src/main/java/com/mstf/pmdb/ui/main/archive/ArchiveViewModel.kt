package com.mstf.pmdb.ui.main.archive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.data.model.db.MovieEntity
import com.mstf.pmdb.ui.base.BaseViewModel
import com.mstf.pmdb.utils.AppConstants.DISPLAY_TYPE_LIST
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<ArchiveNavigator>(dataManager) {

  private val _selectedMoviesId = MutableLiveData<ArrayList<Long>>(arrayListOf())
  val selectedMoviesId: LiveData<ArrayList<Long>> = _selectedMoviesId

  private val _isInSelectMode = MutableLiveData(false)
  val isInSelectMode: LiveData<Boolean> = _isInSelectMode

  private val _displayType = MutableLiveData(DISPLAY_TYPE_LIST)
  val displayType: LiveData<Int> = _displayType

  lateinit var movies: LiveData<PagedList<MovieEntity>>

  init {
    viewModelScope.launch {
      movies = dataManager.allMoviesByDate().toLiveData(pageSize = 50)
    }
  }

  fun setDisplayType(type: Int) {
    _displayType.postValue(type)
    clearSelectedMovies()
  }

  fun toggleSelectMode(active: Boolean) {
    _isInSelectMode.value = active
  }

  /**
   * شناسه ی فیلم انتخاب شده را از لیست موردنظر حذف یا به آن اضافه کند
   */
  fun addOrRemoveMovieFromSelectedList(movieId: Long): Boolean {
    with(_selectedMoviesId.value!!) {
      // اگر از قبل در لیست انتخاب شده ها موجود بود، حذف شود
      if (contains(movieId)) remove(movieId)
      // در غیر اینصورت اضافه شود
      else add(movieId)

      // اگر محتوای لیست خالی نبود، یعنی در حالت انتخاب فیلم هستیم
      _isInSelectMode.value = isNotEmpty()
      return contains(movieId)
    }
  }

  /**
   * خالی کردنِ لیست فیلم های انتخاب شده
   */
  fun clearSelectedMovies() {
    _selectedMoviesId.value?.clear()
    _isInSelectMode.value = false
  }
}