package com.pmdb.android.data.remote

import com.pmdb.android.data.model.api.MatchedMovie
import com.pmdb.android.data.model.api.MatchedMovieList
import retrofit2.Response

interface ApiHelper {

  /**
   * جستجوی فیلم براساس عنوان
   *
   * @param title عنوان فیلم موردنظر جهت جستجو
   */
  suspend fun findMovieByTitle(title: String): Response<MatchedMovieList>

  /**
   * جستجوی فیلم براساس شناسه ی سایت IMDb
   *
   * @param id شناسه ی فیلم موردنظر جهت جستجو
   */
  suspend fun findMovieByImdbId(id: String): Response<MatchedMovie>
}