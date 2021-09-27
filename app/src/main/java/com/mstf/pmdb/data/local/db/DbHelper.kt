package com.mstf.pmdb.data.local.db

import androidx.paging.DataSource
import com.mstf.pmdb.data.model.db.MovieEntity

interface DbHelper {

  /**
   * ذخیره ی اطلاعات فیلم موردنظر در دیتابیس
   */
  suspend fun insertMovie(movie: MovieEntity): Long

  /**
   * حذف فیلم از دیتابیس
   */
  suspend fun deleteMovieById(id: Long): Int?

  /**
   * دریافت تمام فیلم های موجود در دیتابیس
   */
  suspend fun getAllMovies(): List<MovieEntity>?

  /**
   * دریافت تمام فیلم و سریال های موجود در دیتابیس براساس تاریخ ثبت
   */
  fun allMoviesAndTvSeriesByDate(): DataSource.Factory<Int, MovieEntity>

  /**
   * دریافت تمام فیلم و سریال های موجود در دیتابیس به ترتیب امتیاز imdb
   */
  fun allMoviesAndTvSeriesByImdbRate(): DataSource.Factory<Int, MovieEntity>

  /**
   * دریافت تمام فیلم و سریال های موجود در دیتابیس به ترتیب امتیاز Rotten Tomatoes
   */
  fun allMoviesAndTvSeriesByRottenTomatoesRate(): DataSource.Factory<Int, MovieEntity>

  /**
   * دریافت تمام فیلم و سریال های موجود در دیتابیس به ترتیب امتیاز Metacritic
   */
  fun allMoviesAndTvSeriesByMetacriticRate(): DataSource.Factory<Int, MovieEntity>

  /**
   * دریافت تمام فیلم و سریال های موجود در دیتابیس به ترتیب زمان دیده شدن
   */
  fun allMoviesAndTvSeriesByWatchDate(): DataSource.Factory<Int, MovieEntity>

  /**
   * دریافت لیست فیلم و سریال های فیلتر شده ی آرشیو
   *
   * @param title عنوان فیلم و سریال جهت فیلتر
   * @param fromYear حداقل سال تولید فیلم و سریال جهت فیلتر
   * @param toYear حداکثر سال تولید فیلم و سریال جهت فیلتر
   * @param director کارگردان فیلم و سریال جهت فیلتر
   */
  fun filterAllArchive(
    title: String?,
    fromYear: Int?,
    toYear: Int?,
    director: String?
  ): DataSource.Factory<Int, MovieEntity>

  /**
   * دریافت تمام فیلم های موجود در دیتابیس براساس تاریخ ثبت
   */
  fun allMoviesByDate(): DataSource.Factory<Int, MovieEntity>

  /**
   * دریافت لیست فیلم های فیلتر شده ی آرشیو
   *
   * @param title عنوان فیلم جهت فیلتر
   * @param fromYear حداقل سال تولید فیلم جهت فیلتر
   * @param toYear حداکثر سال تولید فیلم جهت فیلتر
   * @param director کارگردان فیلم جهت فیلتر
   */
  fun filterMovies(
    title: String?,
    fromYear: Int?,
    toYear: Int?,
    director: String?
  ): DataSource.Factory<Int, MovieEntity>

  /**
   * دریافت تمام سریال های موجود در دیتابیس براساس تاریخ ثبت
   */
  fun allTvSeriesByDate(): DataSource.Factory<Int, MovieEntity>

  /**
   * دریافت لیست سریال های فیلتر شده ی آرشیو
   *
   * @param title عنوان سریال جهت فیلتر
   * @param fromYear حداقل سال تولید سریال جهت فیلتر
   * @param toYear حداکثر سال تولید سریال جهت فیلتر
   * @param director کارگردان سریال جهت فیلتر
   */
  fun filterTvSeries(
    title: String?,
    fromYear: Int?,
    toYear: Int?,
    director: String?
  ): DataSource.Factory<Int, MovieEntity>

  /**
   * دریافت فیلم موردنظر براساس شناسه ی سایت imdb در صورت موجود بودن در دیتابیس
   */
  suspend fun getMovieByImdbId(id: String): MovieEntity?

  /**
   * بروزرسانی وضعیت مشاهده شدن فیلم (فیلم دیده شده یا خیر)
   */
  suspend fun updateWatchState(id: Long, watched: Boolean)

  /**
   * بروزرسانی وضعیت مورد علاقه بودن فیلم
   */
  suspend fun updateFavoriteState(id: Long, favorite: Boolean)

  /**
   * بروزرسانی فیلم در دیتابیس
   */
  suspend fun updateMovie(movie: MovieEntity)

  /**
   * دریافت فیلم از دیتابیس براساس شناسه ی آن
   *
   * @param movieId شناسه ی فیلم موردنظر
   */
  suspend fun findMovieById(movieId: Long): MovieEntity?
}