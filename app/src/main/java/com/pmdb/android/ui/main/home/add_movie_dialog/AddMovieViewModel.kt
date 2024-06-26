package com.pmdb.android.ui.main.home.add_movie_dialog

import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pmdb.android.R
import com.pmdb.android.data.DataManager
import com.pmdb.android.data.model.api.MatchedMovie
import com.pmdb.android.data.model.api.MatchedMovieCompact
import com.pmdb.android.data.model.api.MatchedMovieList
import com.pmdb.android.data.model.db.MovieEntity
import com.pmdb.android.ui.base.BaseViewModel
import com.pmdb.android.utils.AppConstants.MEDIA_TYPE_TITLE_EPISODE
import com.pmdb.android.utils.AppConstants.MEDIA_TYPE_TITLE_MOVIE
import com.pmdb.android.utils.AppConstants.MEDIA_TYPE_TITLE_SERIES
import com.pmdb.android.utils.CommonUtils
import com.pmdb.android.utils.NoInternetException
import com.pmdb.android.utils.enums.MovieGenre
import com.pmdb.android.utils.enums.TvGenre
import com.pmdb.android.utils.extensions.isNullOrEmptyAfterTrim
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddMovieViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<AddMovieNavigator>(dataManager) {

  private val _matchedMovieList: MutableLiveData<List<MatchedMovieCompact>> = MutableLiveData()
  val matchedMovieList: LiveData<List<MatchedMovieCompact>> = _matchedMovieList

  private var searchJob: Job? = null
  val searchTitle = MutableLiveData<String>()
  val searchID = MutableLiveData<String>()
  val genreField = MutableLiveData<String>()

  private val _searchLoading = MutableLiveData(false)
  val searchLoading: LiveData<Boolean> = _searchLoading

  private val _saveLoading = MutableLiveData(false)
  val saveLoading: LiveData<Boolean> = _saveLoading

  val movie = AddMovieModel()

  // درحال حاضر اطلاعات فیلم امکان ادیت شدن دارند یا خیر
  val isEditing = ObservableField<Boolean>().apply { set(true) }

  // درحال حاضر فیلم موردنظر در دیتابیس آرشیو شده است یا خیر
  val isArchived = ObservableField<Boolean>().apply { set(false) }

  // ژانرهای مربوط به فیلم موردنظر
  private val _genres: MutableLiveData<ArrayList<String>> = MutableLiveData(arrayListOf())
  val genres: LiveData<ArrayList<String>> = _genres

  /**
   *اگر جستجویی در حال انجام بود، کنسل شود
   */
  fun cancelSearching() {
    searchJob?.cancel()
    _searchLoading.value = false
  }

  fun searchMovie(v: View? = null) {
    if (searchTitle.value.isNullOrEmptyAfterTrim() && searchID.value.isNullOrEmptyAfterTrim()) {
      navigator?.showError(dataManager.getString(R.string.enter_movie_title_id))
      return
    }

    navigator?.hideKeyboard()
    _searchLoading.value = true
    searchJob = viewModelScope.launch {
      try {
        if (!searchTitle.value.isNullOrEmptyAfterTrim())
          dataManager.findMovieByTitle(searchTitle.value!!.trim())
            .run { checkMatchedMovieListResponse(this) }
        else dataManager.findMovieByImdbId(searchID.value!!.trim())
          .run { checkMatchedMovieResponse(this) }

      } catch (e: Exception) {
        navigator?.showError(dataManager.getString(R.string.check_internet_connection))
        _searchLoading.value = false
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
  fun toggleWatchState(v: View? = null) {
    if (isEditing.get() != true) return
    with(movie) {
      watched.set(!watched.get()!!)
      if (watched.get() == false) watchAt = null
    }
  }

  /**
   * تغییر وضعیت مورد علاقه بودن فیلم
   */
  fun toggleFavoriteState(v: View? = null) {
    if (isEditing.get() != true) return
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
    // اگر از قبل این ژانر اضافه شده بود، مجدد اضافه نشود
    if (_genres.value!!.contains(label)) return
    // افزودن ژانر به لیست ژانرهای انتخاب شده
    val currentGenres = _genres.value ?: arrayListOf()
    currentGenres.add(label)
    _genres.postValue(currentGenres)
  }

  /**
   * حذف ژانرهای بلااستفاده در لیست ژانرهای انتخاب شده متناسب با نوع مدیا (فیلم یا سریال)
   */
  private fun checkRedundantGenres() {
    when (movie.type.get()) {
      // اگر نوع فیلم سینمایی بود، تمام ژانرهای مربوط به سریال حذف شوند
      MEDIA_TYPE_TITLE_MOVIE -> {
        removeGenre(TvGenre.GAME_SHOW.label())
        removeGenre(TvGenre.NEWS.label())
        removeGenre(TvGenre.REALITY_TV.label())
        removeGenre(TvGenre.TALK_SHOW.label())
      }
      // اگر نوع فیلم سریال بود، تمام ژانرهای مربوط به سینمایی حذف شوند
      else -> {
        removeGenre(MovieGenre.FILM_NOIR.label())
        removeGenre(MovieGenre.SHORT.label())
      }
    }
  }

  /**
   * حذف ژانر موردنظر از لیست ژانرهای انتخاب شده
   *
   * @param label عنوان ژانر موردنظر
   */
  fun removeGenre(label: String) {
    if (!_genres.value!!.contains(label)) return

    val currentGenres = _genres.value!!
    currentGenres.remove(label)
    _genres.postValue(currentGenres)
  }

  /**
   * خالی کردن لیست نتایج جستجوی فعلی
   */
  private fun clearMatchedMovieList() {
    _matchedMovieList.value = arrayListOf()
  }

  /**
   * بررسی لیست فیلم های دریافت شده متناسب با عنوان جستجو شده
   */
  private fun checkMatchedMovieListResponse(response: Response<MatchedMovieList>) {
    _searchLoading.value = false
    with(response) {
      if (!isSuccessful || body() == null) {
        navigator?.showError(dataManager.getString(R.string.something_wrong_try_again))
        return
      }

      if (!body()!!.isSuccessful) {
        if (!body()!!.error.isNullOrEmpty()) navigator?.showError(body()!!.error!!)
        else navigator?.showError(dataManager.getString(R.string.something_wrong_try_again))
        return
      }

      if (body()!!.matchedList == null || body()!!.matchedList!!.isEmpty()) {
        navigator?.showError(dataManager.getString(R.string.movie_not_found))
        return
      }

      // فقط آیتم هایی که از نوع فیلم (سینمایی، سریال، اپیزود) هستند به لیست اضافه شوند
      arrayListOf<MatchedMovieCompact>().apply {
        body()!!.matchedList!!.forEach {
          when (it.type) {
            MEDIA_TYPE_TITLE_MOVIE, MEDIA_TYPE_TITLE_SERIES, MEDIA_TYPE_TITLE_EPISODE -> add(it)
          }
        }
      }.also { _matchedMovieList.value = it }
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
    else _searchLoading.value = false

    with(response) {
      if (!isSuccessful || body() == null) {
        navigator?.showError(dataManager.getString(R.string.something_wrong_try_again))
        return
      }

      if (!body()!!.isSuccessful) {
        if (!body()!!.error.isNullOrEmpty()) navigator?.showError(body()!!.error!!)
        else navigator?.showError(dataManager.getString(R.string.something_wrong_try_again))
        return
      }

      movie.update(body()!!)
      navigator?.hideMatchedMovieList()
      navigator?.showFormLayout()
      navigator?.setUpMovieGenres(getGenreItems())
      // نمایش چیپ های مربوط به ژانر فیلم
      movie.genre.get()?.let { it.split(", ").forEach { genre -> onGenreSelect(genre) } }
      clearMatchedMovieList()
    }
  }

  /**
   * خالی کردن شناسه ی مربوط به فیلم موردنظر برای جستجو
   */
  fun clearMovieId() {
    searchID.value = ""
  }

  /**
   * خالی کردن عنوان مربوط به فیلم موردنظر برای جستجو
   */
  fun clearMovieTitle() {
    searchTitle.value = ""
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
        navigator?.showError(dataManager.getString(R.string.item_has_problem_try_again))
        return
      }

      searchJob = viewModelScope.launch {
        try {
          navigator?.showItemLoadingAtPosition(itemPosition)
          dataManager.findMovieByImdbId(imdbID).run {
            checkMatchedMovieResponse(this, itemPosition)
          }

        } catch (e: NoInternetException) {
          navigator?.showError(dataManager.getString(R.string.check_internet_connection))
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

  /**
   * خالی کردن فرم مربوط به اطلاعات فیلم
   */
  fun clearForm(v: View? = null) {
    navigator?.removeAllGenreChips()
    movie.clear()
    genreField.value = ""
    _genres.postValue(arrayListOf())
    isEditing.set(true)
    isArchived.set(false)
  }

  /**
   * حذف فیلم موردنظر از دیتابیس
   */
  fun deleteMovie() {
    if (isArchived.get() != true || movie.dbId == -1L) return

    viewModelScope.launch {
      dataManager.deleteMovieById(movie.dbId).run {
        clearForm()
      }
    }
  }

  /**
   *بررسی شرایط جهت ذخیره ی فیلم در دیتابیس
   */
  fun saveProcess(v: View? = null) {
    if (movie.title.get().isNullOrEmptyAfterTrim()) {
      navigator?.showError(dataManager.getString(R.string.title_cant_be_empty))
      return
    }
    // اگر تایید پاک کردن فیلم یا فرم درحال نمایش بود، حذف شود
    navigator?.dismissConfirm(false)
    // اگر فیلم قبلا آرشیو شده بود، فقط باید بروزرسانی شود
    if (isArchived.get() == true) updateMovie()
    // در غیراینصورت باید آرشیو شود
    else {
      viewModelScope.launch {
        var existMovie: MovieEntity? = null
        // بررسی اینکه فیلمی قبلا با این شناسه آرشیو شده است یا خیر
        movie.imdbID.get()?.let {
          if (!TextUtils.isEmpty(it.trim())) existMovie = dataManager.getMovieByImdbId(it)
        }
        // اگر فیلمی با این شناسه ی imdb در آرشیو موجود بود،
        // تاییدیه جهت ذخیره روی فیلم موجود گرفته شود
        if (existMovie != null) {
          // شناسه و اطلاعاتِ لازمِ فعلیِ فیلم در دیتابیس که لازم به نگهداری است را ست میکنیم،
          // در صورتیکه کاربر بخواهد اطلاعات را بروز کند
          movie.dbId = existMovie!!.id!!
          movie.dbCreatedAt = existMovie!!.createdAt
          navigator?.showOverwriteConfirm()
        }
        // در غیراینصورت در آرشیو ذخیره شود
        else saveMovie()
      }
    }
  }

  /**
   *بروزرسانی فیلم در دیتابیس
   */
  fun updateMovie() {
    viewModelScope.launch {
      _saveLoading.value = true
      isEditing.set(false)
      movie.genre.set(_genres.value!!.joinToString(", "))
      dataManager.updateMovie(MovieEntity.from(movie)).run {
        _saveLoading.value = false
        isArchived.set(true)
      }
    }
  }

  /**
   *ذخیره ی فیلم در دیتابیس
   */
  fun saveMovie(v: View? = null) {
    viewModelScope.launch {
      _saveLoading.value = true
      isEditing.set(false)
      movie.genre.set(_genres.value!!.joinToString(", "))
      dataManager.insertMovie(MovieEntity.from(movie)).run {
        _saveLoading.value = false
        isArchived.set(true)
        movie.dbId = this
      }
    }
  }

  /**
   *فعال کردن امکان ویرایش اطلاعات فیلم
   */
  fun editMovie(v: View? = null) {
    isEditing.set(true)
  }

  /**
   * آیا اطلاعاتی وارد شده است درون فیلد ها یا خیر
   */
  fun doesAnyInfoEntered(): Boolean {
    // اگر آبجکت فیلم فعلی با آبجکت خام آن برابر نباشد، یعنی تغییراتی اعمال شده است
    return movie != AddMovieModel()
  }

  /**
   *کلیک روی امتیاز مربوط به سایت Imdb
   */
  fun onImdbRateClick(v: View? = null) {
    with(movie.imdbID.get()) {
      if (isNullOrEmptyAfterTrim()) return

      // هدایت کاربر به سایت Imdb مربوط به فیلم یا سریال موردنظر
      navigator?.openUrl(CommonUtils.imdbUrl(this!!))
    }
  }
}