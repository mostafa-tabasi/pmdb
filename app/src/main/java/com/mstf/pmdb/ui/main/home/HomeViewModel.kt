package com.mstf.pmdb.ui.main.home

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.data.model.db.MovieEntity
import com.mstf.pmdb.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<HomeNavigator>(dataManager) {

  val recentlyAddedMovies: LiveData<PagedList<MovieEntity>> =
    dataManager.allMoviesByDate().toLiveData(pageSize = 50)

  val recentlyAddedSeries: LiveData<PagedList<MovieEntity>> =
    dataManager.allTvSeriesByDate().toLiveData(pageSize = 50)

  val topRated: LiveData<PagedList<MovieEntity>> =
    dataManager.allMoviesAndTvSeriesByImdbRate().toLiveData(pageSize = 50)

  val recentlyWatched: LiveData<PagedList<MovieEntity>> =
    dataManager.allMoviesAndTvSeriesByWatchDate().toLiveData(pageSize = 50)
}