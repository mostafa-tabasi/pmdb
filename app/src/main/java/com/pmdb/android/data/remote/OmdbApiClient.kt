package com.pmdb.android.data.remote

import com.pmdb.android.data.model.api.MatchedMovie
import com.pmdb.android.data.model.api.MatchedMovieList
import com.pmdb.android.utils.AppConstants.API_PARAM_IMDB_ID
import com.pmdb.android.utils.AppConstants.API_PARAM_KEY_OMDB
import com.pmdb.android.utils.AppConstants.API_PARAM_SEARCH
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiClient {

  @GET(".")
  suspend fun findMovieByTitle(
    @Query(API_PARAM_KEY_OMDB) apiKey: String,
    @Query(API_PARAM_SEARCH) title: String
  ): Response<MatchedMovieList>

  @GET(".")
  suspend fun findMovieByImdbId(
    @Query(API_PARAM_KEY_OMDB) apiKey: String,
    @Query(API_PARAM_IMDB_ID) id: String
  ): Response<MatchedMovie>
}