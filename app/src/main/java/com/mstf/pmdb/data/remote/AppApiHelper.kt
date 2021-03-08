package com.mstf.pmdb.data.remote

import com.mstf.pmdb.data.model.api.MatchedMovie
import com.mstf.pmdb.data.model.api.MatchedMovieList
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppApiHelper @Inject constructor(
  private val apiHeader: ApiHeader,
  private val omdbApiClient: OmdbApiClient
) : ApiHelper {

  override fun getApiHeader(): ApiHeader = apiHeader

  override suspend fun findMovieByTitle(title: String): Response<MatchedMovieList> =
    omdbApiClient.findMovieByTitle(apiHeader.publicApiHeader.apiKey, title)

  override suspend fun findMovieByImdbId(id: String): Response<MatchedMovie> =
    omdbApiClient.findMovieByImdbId(apiHeader.publicApiHeader.apiKey, id)
}