package com.mstf.pmdb.data.local.db

import com.mstf.pmdb.data.model.db.MovieEntity

interface DbHelper {

  /**
   * ذخیره ی اطلاعات فیلم موردنظر در دیتابیس
   */
  suspend fun insertMovie(movie: MovieEntity)

  /**
   * دریافت تمام فیلم های موجود در دیتابیس
   */
  suspend fun getAllMovies(): List<MovieEntity>?
}