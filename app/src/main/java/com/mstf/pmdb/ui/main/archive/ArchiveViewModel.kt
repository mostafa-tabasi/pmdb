package com.mstf.pmdb.ui.main.archive

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.data.model.db.MovieEntity
import com.mstf.pmdb.ui.base.BaseViewModel
import com.mstf.pmdb.utils.enums.ArchiveDisplayType
import com.mstf.pmdb.utils.enums.MediaFilterType
import com.mstf.pmdb.utils.enums.RatingSite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<ArchiveNavigator>(dataManager) {

  /*
  private val _selectedMoviesId = MutableLiveData<ArrayList<Long>>(arrayListOf())
  val selectedMoviesId: LiveData<ArrayList<Long>> = _selectedMoviesId

  private val _isInSelectMode = MutableLiveData(false)
  val isInSelectMode: LiveData<Boolean> = _isInSelectMode
  */

  private val _displayType =
    MutableLiveData(ArchiveDisplayType.withId(dataManager.archiveDefaultItemViewType.toInt())!!)
  val displayType: LiveData<ArchiveDisplayType> = _displayType

  lateinit var movies: LiveData<PagedList<MovieEntity>>

  private val _filter = MutableLiveData(ArchiveFilterModel())
  val filter: LiveData<ArchiveFilterModel> = _filter

  val defaultItemRate: RatingSite? = RatingSite.withId(dataManager.archiveDefaultItemRate.toInt())

  init {
    viewModelScope.launch {
      initSearchAndFilter()
    }
  }

  /**
   * راه اندازی ساختار جستجو و فیلتر مربوط به لیست فیلم ها
   */
  private fun initSearchAndFilter() {
    movies = Transformations.switchMap(filter,
      object : Function<ArchiveFilterModel, LiveData<PagedList<MovieEntity>>> {
        override fun apply(input: ArchiveFilterModel?): LiveData<PagedList<MovieEntity>> {
          if (input == null) return dataManager.allMoviesAndTvSeriesByDate()
            .toLiveData(pageSize = 50)

          return when (input.type) {
            MediaFilterType.MOVIES -> dataManager.filterMovies(
              input.title,
              input.fromYear,
              input.toYear,
              input.director
            ).toLiveData(pageSize = 50)
            MediaFilterType.SERIES -> dataManager.filterTvSeries(
              input.title,
              input.fromYear,
              input.toYear,
              input.director
            ).toLiveData(pageSize = 50)
            MediaFilterType.BOTH -> dataManager.filterAllArchive(
              input.title,
              input.fromYear,
              input.toYear,
              input.director
            ).toLiveData(pageSize = 50)
          }
        }
      })
    filter.value!!.type = MediaFilterType.BOTH
  }

  /**
   * تغییر نوع نمایش آیتم ها در لیست
   *
   * @param type شناسه ی نوع نمایش موردنظر (1-لیست  2-تایل)
   */
  fun setDisplayType(type: ArchiveDisplayType?) {
    if (type != null && type != _displayType.value) _displayType.postValue(type)
    //clearSelectedMovies()
  }

  /**
   * فیلتر کردن لیست درحالِ نمایش با توجه به نوع فیلتر انتخابی
   *
   * @param type فیلتر موردنظر جهت اعمال شدن (1-فیلم  2-سریال  3-هردو)
   * @param movieTitle عنوان فیلم جهت جستجو
   * @param fromYear سال تولید فیلم بیشتر از این سال
   * @param toYear سال تولید فیلم کمتر از این سال
   * @param director کارگردان فیلم جهت جستجو
   */
  fun filterList(
    type: MediaFilterType?,
    movieTitle: String?,
    fromYear: String?,
    toYear: String?,
    director: String?
  ) {
    with(_filter.value ?: ArchiveFilterModel()) {
      type?.let { this.type = it }
      this.title = movieTitle
      this.fromYear = fromYear?.toIntOrNull()
      this.toYear = toYear?.toIntOrNull()
      this.director = director
      _filter.postValue(this)
    }
  }

  /*
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
  */
}