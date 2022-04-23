package com.pmdb.android.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.pmdb.android.data.DataManager
import com.pmdb.android.data.model.db.MovieEntity
import com.pmdb.android.ui.base.BaseViewModel
import com.pmdb.android.utils.enums.RatingSite
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<HomeNavigator>(dataManager) {

  private val _isRecentMoviesEnable = MutableLiveData(dataManager.isRecentMoviesEnable)
  val isRecentMoviesEnable: LiveData<Boolean> = _isRecentMoviesEnable

  val recentlyAddedMovies: LiveData<PagedList<MovieEntity>> =
    dataManager.allMoviesByDate().toLiveData(pageSize = 50)

  private val _isRecentSeriesEnable = MutableLiveData(dataManager.isRecentSeriesEnable)
  val isRecentSeriesEnable: LiveData<Boolean> = _isRecentSeriesEnable

  val recentlyAddedSeries: LiveData<PagedList<MovieEntity>> =
    dataManager.allTvSeriesByDate().toLiveData(pageSize = 50)

  private val _isTopRatedEnable = MutableLiveData(dataManager.isTopRatedEnable)
  val isTopRatedEnable: LiveData<Boolean> = _isTopRatedEnable

  private val _topRatedMethod =
    MutableLiveData(RatingSite.withId(dataManager.topRatedMethod.toInt()))
  val topRatedMethod: LiveData<RatingSite?> = _topRatedMethod

  val topRated: LiveData<PagedList<MovieEntity>> =
    when (RatingSite.withId(dataManager.topRatedMethod.toInt())) {
      RatingSite.IMDB ->
        dataManager.allMoviesAndTvSeriesByImdbRate().toLiveData(pageSize = 50)
      RatingSite.ROTTEN_TOMATOES ->
        dataManager.allMoviesAndTvSeriesByRottenTomatoesRate().toLiveData(pageSize = 50)
      RatingSite.METACRITIC ->
        dataManager.allMoviesAndTvSeriesByMetacriticRate().toLiveData(pageSize = 50)
      else -> MutableLiveData()
    }

  private val _isRecentlyWatchedEnable = MutableLiveData(dataManager.isRecentlyWatchedEnable)
  val isRecentlyWatchedEnable: LiveData<Boolean> = _isRecentlyWatchedEnable

  val recentlyWatched: LiveData<PagedList<MovieEntity>> =
    dataManager.allMoviesAndTvSeriesByWatchDate().toLiveData(pageSize = 50)

  /**
   * بروزرسانی متغیرها؛ اگر هر متغیری مقدارش تغییر کرده بود، صفحه مجدد بارگزاری باید شود
   */
  fun refresh() {
    dataManager.isRecentMoviesEnable.let {
      if (isRecentMoviesEnable.value != it) _isRecentMoviesEnable.value = it
    }
    dataManager.isRecentSeriesEnable.let {
      if (isRecentSeriesEnable.value != it) _isRecentSeriesEnable.value = it
    }
    dataManager.isTopRatedEnable.let {
      if (isTopRatedEnable.value != it) _isTopRatedEnable.value = it
    }
    dataManager.isRecentlyWatchedEnable.let {
      if (isRecentlyWatchedEnable.value != it) _isRecentlyWatchedEnable.value = it
    }
    RatingSite.withId(dataManager.topRatedMethod.toInt()).let {
      if (topRatedMethod.value != it) _topRatedMethod.value = it
    }
  }
}