package com.mstf.pmdb.data.remote

import com.mstf.pmdb.data.model.api.MatchedMovie
import com.mstf.pmdb.data.model.api.MatchedMovieList
import retrofit2.Response

interface ApiHelper {

  fun getApiHeader(): ApiHeader

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