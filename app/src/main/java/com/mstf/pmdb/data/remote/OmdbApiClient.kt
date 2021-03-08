package com.mstf.pmdb.data.remote

import com.mstf.pmdb.data.model.api.MatchedMovie
import com.mstf.pmdb.data.model.api.MatchedMovieList
import com.mstf.pmdb.utils.AppConstants.API_PARAM_IMDB_ID
import com.mstf.pmdb.utils.AppConstants.API_PARAM_KEY_OMDB
import com.mstf.pmdb.utils.AppConstants.API_PARAM_SEARCH
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