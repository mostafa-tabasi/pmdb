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
  var topRatedMethod: RatingSite? = RatingSite.withId(dataManager.topRatedMethod.toInt())
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
    if (isRecentMoviesEnable.get() != dataManager.isRecentMoviesEnable) {
      navigator?.refreshPage()
      return
    }
    if (isRecentSeriesEnable.get() != dataManager.isRecentSeriesEnable) {
      navigator?.refreshPage()
      return
    }
    if (isTopRatedEnable.get() != dataManager.isTopRatedEnable) {
      navigator?.refreshPage()
      return
    }
    if (isRecentlyWatchedEnable.get() != dataManager.isRecentlyWatchedEnable) {
      navigator?.refreshPage()
      return
    }
    if (topRatedMethod != RatingSite.withId(dataManager.topRatedMethod.toInt())) {
      navigator?.refreshPage()
      return
    }
  }
}