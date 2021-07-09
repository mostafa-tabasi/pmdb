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
   * دریافت تمام فیلم های موجود در دیتابیس براساس تاریخ ثبت
   */
  fun allMoviesByDate(): DataSource.Factory<Int, MovieEntity>

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