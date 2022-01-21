package com.pmdb.android.ui.main.archive.archive_item_info_dialog

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pmdb.android.R
import com.pmdb.android.data.DataManager
import com.pmdb.android.data.model.db.MovieEntity
import com.pmdb.android.ui.base.BaseViewModel
import com.pmdb.android.ui.main.home.add_movie_dialog.AddMovieModel
import com.pmdb.android.utils.AppConstants
import com.pmdb.android.utils.enums.MovieGenre
import com.pmdb.android.utils.enums.TvGenre
import com.pmdb.android.utils.extensions.isNullOrEmptyAfterTrim
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveItemInfoViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<ArchiveItemInfoNavigator>(dataManager) {

  val movie = AddMovieModel()

  private val _saveLoading = MutableLiveData(false)
  val saveLoading: LiveData<Boolean> = _saveLoading

  // درحال حاضر اطلاعات فیلم امکان ادیت شدن دارند یا خیر
  private val _isEditing = MutableLiveData(false)
  val isEditing: LiveData<Boolean> = _isEditing

  // ژانرهای مربوط به فیلم موردنظر
  private val _genres: MutableLiveData<ArrayList<String>> = MutableLiveData(arrayListOf())
  val genres: LiveData<ArrayList<String>> = _genres

  /**
   * دریافت آیتم موردنظر از آرشیو
   *
   * @param id شناسه ی آیتم موردنظر
   */
  fun fetchArchiveItem(id: Long) {
    viewModelScope.launch {
      dataManager.findMovieById(id)?.run {
        movie.update(this)
        movie.genre.get()?.let {
          val genreList = arrayListOf<String>()
          for (genre in it.split(", ")) genreList.add(genre)
          _genres.postValue(genreList)
        }
      }
    }
  }

  /**
   * تغییر نوع فیلم
   */
  fun toggleMovieType(v: View? = null) {
    with(movie) {
      type.set(if (tv.get() == true) AppConstants.MEDIA_TYPE_TITLE_MOVIE else AppConstants.MEDIA_TYPE_TITLE_SERIES)
      checkRedundantGenres()
    }
  }

  /**
   * تغییر وضعیت دیدن فیلم (کاربر فیلم را دیده است یا خیر)
   */
  fun toggleWatchState(v: View? = null) {
    if (isEditing.value != true) return
    with(movie) {
      watched.set(!watched.get()!!)
      if (watched.get() == false) watchAt = null
    }
  }

  /**
   * تغییر وضعیت مورد علاقه بودن فیلم
   */
  fun toggleFavoriteState(v: View? = null) {
    if (isEditing.value != true) return
    with(movie) { favorite.set(!favorite.get()!!) }
  }

  /**
   * لیست ژانرهای موجود برای فیلم موردنظر متناسب با نوع آن
   */
  fun getGenreItems() =
    if (movie.type.get() == AppConstants.MEDIA_TYPE_TITLE_MOVIE) dataManager.getMovieGenreList()
    else dataManager.getTvSeriesGenreList()

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
      AppConstants.MEDIA_TYPE_TITLE_MOVIE -> {
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
   * ست کردن مسیر پوستر انتخابی کاربر از حافظه ی دیوایس
   */
  fun setMoviePoster(posterPath: String) {
    if (!posterPath.isNullOrEmptyAfterTrim()) movie.poster.set(posterPath)
  }

  /**
   * حذف فیلم موردنظر از دیتابیس
   */
  fun deleteMovie() {
    viewModelScope.launch { dataManager.deleteMovieById(movie.dbId).run { navigator?.back() } }
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
    // بروزرسانی فیلم
    updateMovie()
  }

  /**
   *فعال کردن امکان ویرایش اطلاعات فیلم
   */
  fun editMovie(v: View? = null) {
    _isEditing.value = true
  }

  /**
   *بروزرسانی فیلم در دیتابیس
   */
  private fun updateMovie() {
    viewModelScope.launch {
      _saveLoading.value = true
      _isEditing.value = false
      movie.genre.set(_genres.value!!.joinToString(", "))
      dataManager.updateMovie(MovieEntity.from(movie)).run { _saveLoading.value = false }
    }
  }
}