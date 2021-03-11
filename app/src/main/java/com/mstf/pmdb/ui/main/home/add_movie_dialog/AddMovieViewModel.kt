package com.mstf.pmdb.ui.main.home.add_movie_dialog

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.data.model.api.MatchedMovie
import com.mstf.pmdb.data.model.api.MatchedMovieCompact
import com.mstf.pmdb.data.model.api.MatchedMovieList
import com.mstf.pmdb.ui.base.BaseViewModel
import com.mstf.pmdb.utils.NoInternetException
import com.mstf.pmdb.utils.extensions.isNullOrEmptyAfterTrim
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddMovieViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<AddMovieNavigator>(dataManager) {

  val matchedMovieList: MutableLiveData<List<MatchedMovieCompact>> = MutableLiveData()
  private var searchJob: Job? = null
  val searchMovieLoading = ObservableField<Boolean>().apply { set(false) }
  val movie = AddMovieModel()

  /**
   *اگر جستجویی در حال انجام بود، کنسل شود
   */
  fun cancelSearching() {
    searchJob?.cancel()
    searchMovieLoading.set(false)
  }

  fun searchMovie(v: View?) {
    with(movie) {

      if (title.isNullOrEmptyAfterTrim() && imdbID.isNullOrEmptyAfterTrim()) {
        navigator?.showError("Enter movie title or id")
        return
      }

      navigator?.hideKeyboard()
      searchMovieLoading.set(true)
      searchJob = viewModelScope.launch {

        try {
          if (!title.isNullOrEmptyAfterTrim())
            dataManager.findMovieByTitle(title!!.trim()).run { checkMatchedMovieListResponse(this) }
          else
            dataManager.findMovieByImdbId(imdbID!!.trim()).run { checkMatchedMovieResponse(this) }

        } catch (e: NoInternetException) {
          navigator?.showError("Check Internet Connection")
          searchMovieLoading.set(false)
        }
      }
    }
  }

  /**
   * عملکرد کلیک بازگشت به عقب در لیست فیلم های جستجو شده
   */
  fun onMatchedMovieListBackClick(v: View?) {
    clearMatchedMovieList()
    // نمایش لایه ی جستجو
    navigator?.showSearchLayout()
  }

  /**
   * خالی کردن لیست نتایج جستجوی فعلی
   */
  fun clearMatchedMovieList() {
    matchedMovieList.postValue(arrayListOf())
  }

  /**
   * بررسی لیست فیلم های دریافت شده متناسب با عنوان جستجو شده
   */
  private fun checkMatchedMovieListResponse(response: Response<MatchedMovieList>) {
    searchMovieLoading.set(false)
    with(response) {
      if (!isSuccessful || body() == null) {
        navigator?.showError("Something wrong! try again")
        return
      }

      if (!body()!!.response) {
        if (!body()!!.error.isNullOrEmpty()) navigator?.showError(body()!!.error!!)
        else navigator?.showError("Something wrong! try again")
        return
      }

      matchedMovieList.postValue(body()!!.matchedList)
      navigator?.showMatchedMovieList()
    }
  }

  /**
   * بررسی فیلم دریافت شده متناسب با شناسه ی جستجو شده
   */
  private fun checkMatchedMovieResponse(response: Response<MatchedMovie>) {
    navigator?.showError(response.body().toString())
    //TODO: show movie info
    searchMovieLoading.set(false)
  }

  fun clearMovieId() {
    movie.imdbID = null
  }

  fun clearMovieTitle() {
    movie.title = null
  }
}