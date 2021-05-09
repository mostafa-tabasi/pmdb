package com.mstf.pmdb.ui.main.archive

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<ArchiveNavigator>(dataManager) {

  fun getMovies() {
    viewModelScope.launch {
      val movies = dataManager.getAllMovies()
      navigator?.showError(if (movies != null) Gson().toJson(movies[movies.size - 1]) else "EMPTY ARCHIVE")
    }
  }
}