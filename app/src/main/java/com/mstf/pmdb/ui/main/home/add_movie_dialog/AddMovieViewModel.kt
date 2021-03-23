package com.mstf.pmdb.ui.main.home.add_movie_dialog

import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.data.model.api.MatchedMovie
import com.mstf.pmdb.data.model.api.MatchedMovieCompact
import com.mstf.pmdb.data.model.api.MatchedMovieList
import com.mstf.pmdb.ui.base.BaseViewModel
import com.mstf.pmdb.utils.AppConstants.MEDIA_TYPE_TITLE_MOVIE
import com.mstf.pmdb.utils.AppConstants.MEDIA_TYPE_TITLE_SERIES
import com.mstf.pmdb.utils.NoInternetException
import com.mstf.pmdb.utils.enums.MovieGenre
import com.mstf.pmdb.utils.enums.TvGenre
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
  val searchTitle = ObservableField<String>()
  val searchID = ObservableField<String>()
  val searchLoading = ObservableField<Boolean>().apply { set(false) }
  val movie = AddMovieModel()

  // درحال حاضر اطلاعات فیلم امکان ادیت شدن دارند یا خیر
  val isEditingEnable = ObservableField<Boolean>().apply { set(true) }

  /**
   *اگر جستجویی در حال انجام بود، کنسل شود
   */
  fun cancelSearching() {
    searchJob?.cancel()
    searchLoading.set(false)
  }

  fun searchMovie(v: View? = null) {
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
   * عملیات لازم زمان انتخاب یک عملگر از روی کیبورد
   */
  fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
      searchMovie()
      return true
    }
    return false
  }

  /**
   * خالی کردن فرم مربوط به اطلاعات فیلم
   */
  fun clearForm(v: View? = null) {
    navigator?.removeAllGenreChips()
    movie.clear()
  }

  /**
   *ذخیره ی فیلم در دیتابیس
   */
  fun saveMovie(v: View? = null) {
    if (movie.title.get().isNullOrEmptyAfterTrim()) {
      navigator?.showError("Title can't be empty")
      return
    }
    //TODO: save
    //isEditingEnable.set(false)

    // temp
    isEditingEnable.set(!isEditingEnable.get()!!)
  }

  /**
   * عملکرد کلیک بازگشت به عقب در لیست فیلم های جستجو شده
   */
  fun onMatchedMovieListBackClick(v: View? = null) {
    cancelSearching()
    clearMatchedMovieList()
    // نمایش لایه ی جستجو
    navigator?.showSearchLayout()
  }

  /**
   * تغییر نوع فیلم
   */
  fun toggleMovieType(v: View? = null) {
    with(movie) {
      type.set(if (tv.get() == true) MEDIA_TYPE_TITLE_MOVIE else MEDIA_TYPE_TITLE_SERIES)
      navigator?.setUpMovieGenres(getGenreItems())
      checkRedundantGenres()
    }
  }

  /**
   * تغییر وضعیت دیدن فیلم (کاربر فیلم را دیده است یا خیر)
   */
  fun toggleSeenState(v: View? = null) {
    with(movie) { seen.set(!seen.get()!!) }
  }

  /**
   * تغییر وضعیت مورد علاقه بودن فیلم
   */
  fun toggleFavoriteState(v: View? = null) {
    with(movie) { favorite.set(!favorite.get()!!) }
  }

  /**
   * لیست ژانرهای موجود برای فیلم موردنظر متناسب با نوع آن
   */
  fun getGenreItems() =
    if (movie.type.get() == MEDIA_TYPE_TITLE_MOVIE) dataManager.getMovieGenreList() else dataManager.getTvSeriesGenreList()

  /**
   * انتخاب کردن یک ژانر برای افزودن به لیست ژانرهای انتخاب شده ی فیلم
   *
   * @param label عنوان ژانر انتخاب شده
   */
  fun onGenreSelect(label: String) {
    // اگر از قبل ای ژانر اضافه شده بود، مجدد اضافه نشود
    if (movie.genre.get()!!.contains(label)) return
    // افزودن ژانر به لیست ژانرهای انتخاب شده
    movie.genre.set(movie.genre.get() + "$label,")
    // نمایش چیپ مربوط به ژانر انتخاب شده
    navigator?.addGenreChip(label, true)
  }

  /**
   * حذف ژانرهای بلااستفاده در لیست ژانرهای انتخاب شده متناسب با نوع مدیا (فیلم یا سریال)
   */
  private fun checkRedundantGenres() {
    movie.genre.get()?.let {
      when (movie.type.get()) {
        // اگر نوع فیلم سینمایی بود، تمام ژانرهای مربوط به سریال حذف شوند
        MEDIA_TYPE_TITLE_MOVIE -> {
          removeGenre(TvGenre.GAME_SHOW.labelWithSeparator())
          { navigator?.removeGenreChip(TvGenre.GAME_SHOW.label()) }
          removeGenre(TvGenre.NEWS.labelWithSeparator())
          { navigator?.removeGenreChip(TvGenre.NEWS.label()) }
          removeGenre(TvGenre.REALITY_TV.labelWithSeparator())
          { navigator?.removeGenreChip(TvGenre.REALITY_TV.label()) }
          removeGenre(TvGenre.TALK_SHOW.labelWithSeparator())
          { navigator?.removeGenreChip(TvGenre.TALK_SHOW.label()) }
        }
        // اگر نوع فیلم سریال بود، تمام ژانرهای مربوط به سینمایی حذف شوند
        else -> {
          removeGenre(MovieGenre.FILM_NOIR.labelWithSeparator())
          { navigator?.removeGenreChip(MovieGenre.FILM_NOIR.label()) }
          removeGenre(MovieGenre.SHORT.labelWithSeparator())
          { navigator?.removeGenreChip(MovieGenre.SHORT.label()) }
        }
      }
    }
  }

  /**
   * حذف ژانر موردنظر از لیست ژانرهای انتخاب شده
   *
   * @param label عنوان ژانر موردنظر
   * @param afterRemove عملیاتی که بعد از حذف شدن لازم است انحام شود
   */
  fun removeGenre(label: String, afterRemove: (() -> Unit)? = null) {
    movie.genre.get()?.let {
      if (!it.contains(label)) return
      movie.genre.set(it.replace(label, ""))
      afterRemove?.invoke()
    }
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

      if (body()!!.matchedList == null || body()!!.matchedList!!.isEmpty()) {
        navigator?.showError("Movie not found")
        return
      }

      matchedMovieList.postValue(body()!!.matchedList!!)
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
      navigator?.setUpMovieGenres(getGenreItems())
      // نمایش چیپ های مربوط به ژانر فیلم
      movie.genre.get()?.let {
        it.split(", ").forEach { genre -> navigator?.addGenreChip(genre, false) }
      }
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

  /**
   * ست کردن مسیر پوستر انتخابی کاربر از حافظه ی دیوایس
   */
  fun setMoviePoster(posterPath: String) {
    if (!posterPath.isNullOrEmptyAfterTrim()) movie.poster.set(posterPath)
  }
}