package com.mstf.pmdb.ui.main.home

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.data.model.db.MovieEntity
import com.mstf.pmdb.ui.base.BaseViewModel
import com.mstf.pmdb.utils.enums.RatingSite
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<HomeNavigator>(dataManager) {

  val isRecentMoviesEnable = ObservableBoolean(dataManager.isRecentMoviesEnable)
  val recentlyAddedMovies: LiveData<PagedList<MovieEntity>> =
    dataManager.allMoviesByDate().toLiveData(pageSize = 50)

  val isRecentSeriesEnable = ObservableBoolean(dataManager.isRecentSeriesEnable)
  val recentlyAddedSeries: LiveData<PagedList<MovieEntity>> =
    dataManager.allTvSeriesByDate().toLiveData(pageSize = 50)

  val isTopRatedEnable = ObservableBoolean(dataManager.isTopRatedEnable)
  private val _topRatedMethod =
    MutableLiveData(RatingSite.withId(dataManager.topRatedMethod.toInt()))
  var topRatedMethod: LiveData<RatingSite?> = _topRatedMethod
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

  val isRecentlyWatchedEnable = ObservableBoolean(dataManager.isRecentlyWatchedEnable)
  val recentlyWatched: LiveData<PagedList<MovieEntity>> =
    dataManager.allMoviesAndTvSeriesByWatchDate().toLiveData(pageSize = 50)

  /**
   * بروزرسانی متغیرها؛ اگر هر متغیری مقدارش تغییر کرده بود، صفحه مجدد بارگزاری باید شود
   */
  fun refresh() {
    dataManager.isRecentMoviesEnable.let {
      if (isRecentMoviesEnable.get() != it) isRecentMoviesEnable.set(it)
    }
    dataManager.isRecentSeriesEnable.let {
      if (isRecentSeriesEnable.get() != it) isRecentSeriesEnable.set(it)
    }
    dataManager.isTopRatedEnable.let {
      if (isTopRatedEnable.get() != it) isTopRatedEnable.set(it)
    }
    dataManager.isRecentlyWatchedEnable.let {
      if (isRecentlyWatchedEnable.get() != it) isRecentlyWatchedEnable.set(it)
    }
    RatingSite.withId(dataManager.topRatedMethod.toInt()).let {
      if (_topRatedMethod.value != it) _topRatedMethod.value = it
    }
  }
}