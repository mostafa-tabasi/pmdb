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
  val searchTitle = ObservableField<String?>()
  val searchID = ObservableField<String>()
  val searchLoading = ObservableField<Boolean>().apply { set(false) }
  val movie = AddMovieModel()

  /**
   *اگر جستجویی در حال انجام بود، کنسل شود
   */
  fun cancelSearching() {
    searchJob?.cancel()
    searchLoading.set(false)
  }

  fun searchMovie(v: View?) {
    if (searchTitle.get().isNullOrEmptyAfterTrim() && searchID.get().isNullOrEmptyAfterTrim()) {
      navigator?.showError("Enter movie title or id")
      return
    }

    navigator?.hideKeyboard()
    searchLoading.set(true)
    searchJob = viewModelScope.launch {
      try {
        if (!searchTitle.get().isNullOrEmptyAfterTrim())
          dataManager.findMovieByTitle(searchTitle.get()!!.trim())
            .run { checkMatchedMovieListResponse(this) }
        else dataManager.findMovieByImdbId(searchID.get()!!.trim())
          .run { checkMatchedMovieResponse(this) }

      } catch (e: NoInternetException) {
        navigator?.showError("Check Internet Connection")
        searchLoading.set(false)
      }
    }
  }

  /**
   * عملکرد کلیک خالی کردن فرم مربوط به اطلاعات فیلم
   */
  fun onFormClear(v: View? = null) {
    movie.clear()
  }

  /**
   * عملکرد کلیک بازگشت به عقب در لیست فیلم های جستجو شده
   */
  fun onMatchedMovieListBackClick(v: View?) {
    cancelSearching()
    clearMatchedMovieList()
    // نمایش لایه ی جستجو
    navigator?.showSearchLayout()
  }

  /**
   * خالی کردن لیست نتایج جستجوی فعلی
   */
  private fun clearMatchedMovieList() {
    matchedMovieList.postValue(arrayListOf())
  }

  /**
   * بررسی لیست فیلم های دریافت شده متناسب با عنوان جستجو شده
   */
  private fun checkMatchedMovieListResponse(response: Response<MatchedMovieList>) {
    searchLoading.set(false)
    with(response) {
      if (!isSuccessful || body() == null) {
        navigator?.showError("Something wrong! try again")
        return
      }

      if (!body()!!.isSuccessful) {
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
   *
   * @param response نتیجه ی دریافت شده جهت بررسی
   * @param itemPosition شماره ردیف فیلمی که جزئیات دریافت شده ی آن بررسی میشود
   * (در صورتیکه فیلم از لیست جستجو انتخاب شده باشد)
   */
  private fun checkMatchedMovieResponse(
    response: Response<MatchedMovie>,
    itemPosition: Int? = null
  ) {
    //اگر فیلم از لیست جستجو انتخاب شده بود، لودینگ مربوط به آیتمِ لیست متوقف شود
    if (itemPosition != null) navigator?.hideItemLoadingAtPosition(itemPosition)
    // در غیراینصورت لودینگ مربوط به فیلد جستجو متوقف شود
    else searchLoading.set(false)

    with(response) {
      if (!isSuccessful || body() == null) {
        navigator?.showError("Something wrong! try again")
        return
      }

      if (!body()!!.isSuccessful) {
        if (!body()!!.error.isNullOrEmpty()) navigator?.showError(body()!!.error!!)
        else navigator?.showError("Something wrong! try again")
        return
      }

      movie.update(body()!!)
      navigator?.hideMatchedMovieList()
      navigator?.showFormLayout()
      clearMatchedMovieList()
    }
  }

  /**
   * خالی کردن شناسه ی مربوط به فیلم موردنظر برای جستجو
   */
  fun clearMovieId() {
    searchID.set(null)
  }

  /**
   * خالی کردن عنوان مربوط به فیلم موردنظر برای جستجو
   */
  fun clearMovieTitle() {
    searchTitle.set(null)
  }

  /**
   * دریافت اطلاعات مربوط به فیلم موردنظر
   *
   * @param movie فیلم انتخاب شده
   * @param itemPosition شماره ردیف فیلم انتخاب شده از لیست
   */
  fun getMovieDetails(movie: MatchedMovieCompact, itemPosition: Int) {
    with(movie) {
      if (imdbID.isNullOrEmpty() || imdbID == "N/A") {
        navigator?.showError("Item has problem right now, try again later")
        return
      }

      searchJob = viewModelScope.launch {
        try {
          navigator?.showItemLoadingAtPosition(itemPosition)
          dataManager.findMovieByImdbId(imdbID).run {
            checkMatchedMovieResponse(this, itemPosition)
          }

        } catch (e: NoInternetException) {
          navigator?.showError("Check Internet Connection")
          navigator?.hideItemLoadingAtPosition(itemPosition)
        }
      }
    }
  }
}