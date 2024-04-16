package com.pmdb.android.ui.main.archive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.pmdb.android.data.DataManager
import com.pmdb.android.data.model.db.MovieEntity
import com.pmdb.android.ui.base.BaseViewModel
import com.pmdb.android.utils.enums.ArchiveDisplayType
import com.pmdb.android.utils.enums.MediaFilterType
import com.pmdb.android.utils.enums.RatingSite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<ArchiveNavigator>(dataManager) {

  private val _displayType =
    MutableLiveData(ArchiveDisplayType.withId(dataManager.archiveDefaultItemViewType.toInt())!!)
  val displayType: LiveData<ArchiveDisplayType> = _displayType

  lateinit var movies: LiveData<PagedList<MovieEntity>>

  private val _filter = MutableLiveData(ArchiveFilterModel())
  val filter: LiveData<ArchiveFilterModel> = _filter

  private val _defaultItemRate =
    MutableLiveData(RatingSite.withId(dataManager.archiveDefaultItemRate.toInt()))
  val defaultItemRate: LiveData<RatingSite?> = _defaultItemRate

  init {
    viewModelScope.launch {
      initSearchAndFilter()
    }
  }

  /**
   * راه اندازی ساختار جستجو و فیلتر مربوط به لیست فیلم ها
   */
  private fun initSearchAndFilter() {
    movies = filter.switchMap {input ->
       when (input.type) {
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

    /*
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
    */

    filter.value!!.type = MediaFilterType.BOTH
  }

  /**
   * تغییر نوع نمایش آیتم ها در لیست
   *
   * @param type شناسه ی نوع نمایش موردنظر (1-لیست  2-تایل)
   */
  fun setDisplayType(type: ArchiveDisplayType?) {
    if (type != null && type != _displayType.value) _displayType.postValue(type)
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

  /**
   * بروزرسانی متغیرهای تاثیرگذار در نمایش محتوا
   */
  fun refresh() {
    ArchiveDisplayType.withId(dataManager.archiveDefaultItemViewType.toInt())?.let {
      if (displayType.value != it) _displayType.value = it
    }
    RatingSite.withId(dataManager.archiveDefaultItemRate.toInt())?.let {
      if (defaultItemRate.value != it) _defaultItemRate.value = it
    }
  }
}