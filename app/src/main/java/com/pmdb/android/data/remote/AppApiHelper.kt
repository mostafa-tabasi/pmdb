package com.pmdb.android.data.remote

import com.pmdb.android.BuildConfig
import com.pmdb.android.data.model.api.MatchedMovie
import com.pmdb.android.data.model.api.MatchedMovieList
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppApiHelper @Inject constructor(
  private val omdbApiClient: OmdbApiClient
) : ApiHelper {

  override suspend fun findMovieByTitle(title: String): Response<MatchedMovieList> =
    omdbApiClient.findMovieByTitle(BuildConfig.API_KEY_OMDB, title)

  override suspend fun findMovieByImdbId(id: String): Response<MatchedMovie> =
    omdbApiClient.findMovieByImdbId(BuildConfig.API_KEY_OMDB, id)
}