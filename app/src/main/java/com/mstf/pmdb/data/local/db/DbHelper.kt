package com.mstf.pmdb.data.local.db

import com.mstf.pmdb.data.model.db.MovieEntity

interface DbHelper {

  /**
   * ذخیره ی اطلاعات فیلم موردنظر در دیتابیس
   */
  suspend fun insertMovie(movie: MovieEntity): Long

  /**
   * دریافت تمام فیلم های موجود در دیتابیس
   */
  suspend fun getAllMovies(): List<MovieEntity>?

  /**
   * بروزرسانی وضعیت مشاهده شدن فیلم (فیلم دیده شده یا خیر)
   */
  suspend fun updateWatchState(id: Long, watched: Boolean)

  /**
   * بروزرسانی وضعیت مورد علاقه بودن فیلم
   */
  suspend fun updateFavoriteState(id: Long, favorite: Boolean)
}