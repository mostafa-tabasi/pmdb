package com.mstf.pmdb.data

import android.content.Context
import androidx.paging.DataSource
import com.google.gson.Gson
import com.mstf.pmdb.data.local.db.DbHelper
import com.mstf.pmdb.data.local.prefs.PreferencesHelper
import com.mstf.pmdb.data.model.api.MatchedMovie
import com.mstf.pmdb.data.model.api.MatchedMovieList
import com.mstf.pmdb.data.model.db.MovieEntity
import com.mstf.pmdb.data.remote.ApiHeader
import com.mstf.pmdb.data.remote.ApiHelper
import com.mstf.pmdb.data.resource.ResourceHelper
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager @Inject constructor(
  private val mContext: Context,
  private val dbHelper: DbHelper,
  private val preferencesHelper: PreferencesHelper,
  private val apiHelper: ApiHelper,
  private val resourceHelper: ResourceHelper,
  private val gson: Gson
) : DataManager {

  override var currentUserId: String?
    get() = preferencesHelper.currentUserId
    set(value) {
      preferencesHelper.currentUserId = value
      getApiHeader().protectedApiHeader.userId = value!!
    }

  override var accessToken: String?
    get() = preferencesHelper.accessToken
    set(accessToken) {
      preferencesHelper.accessToken = accessToken
      getApiHeader().protectedApiHeader.accessToken = accessToken
    }

  override fun getApiHeader(): ApiHeader = apiHelper.getApiHeader()

  override suspend fun findMovieByTitle(title: String): Response<MatchedMovieList> =
    apiHelper.findMovieByTitle(title)

  override suspend fun findMovieByImdbId(id: String): Response<MatchedMovie> =
    apiHelper.findMovieByImdbId(id)

  override fun getString(resourceId: Int) = resourceHelper.getString(resourceId)

  override fun getMovieGenreList() = resourceHelper.getMovieGenreList()

  override fun getTvSeriesGenreList() = resourceHelper.getTvSeriesGenreList()

  override suspend fun insertMovie(movie: MovieEntity) = dbHelper.insertMovie(movie)

  override suspend fun deleteMovieById(id: Long): Int? = dbHelper.deleteMovieById(id)

  override suspend fun getAllMovies() = dbHelper.getAllMovies()

  override fun allMoviesAndTvSeriesByDate(): DataSource.Factory<Int, MovieEntity> =
    dbHelper.allMoviesAndTvSeriesByDate()

  override fun filterAllArchive(
    title: String?,
    fromYear: Int?,
    toYear: Int?,
    director: String?
  ): DataSource.Factory<Int, MovieEntity> =
    dbHelper.filterAllArchive(title, fromYear, toYear, director)

  override fun allMoviesByDate(): DataSource.Factory<Int, MovieEntity> = dbHelper.allMoviesByDate()

  override fun filterMovies(
    title: String?,
    fromYear: Int?,
    toYear: Int?,
    director: String?
  ): DataSource.Factory<Int, MovieEntity> = dbHelper.filterMovies(title, fromYear, toYear, director)

  override fun allTvSeriesByDate(): DataSource.Factory<Int, MovieEntity> =
    dbHelper.allTvSeriesByDate()

  override fun filterTvSeries(
    title: String?,
    fromYear: Int?,
    toYear: Int?,
    director: String?
  ): DataSource.Factory<Int, MovieEntity> =
    dbHelper.filterTvSeries(title, fromYear, toYear, director)

  override suspend fun getMovieByImdbId(id: String): MovieEntity? = dbHelper.getMovieByImdbId(id)

  override suspend fun updateWatchState(id: Long, watched: Boolean) =
    dbHelper.updateWatchState(id, watched)

  override suspend fun updateFavoriteState(id: Long, favorite: Boolean) =
    dbHelper.updateFavoriteState(id, favorite)

  override suspend fun updateMovie(movie: MovieEntity) = dbHelper.updateMovie(movie)

  override suspend fun findMovieById(movieId: Long) = dbHelper.findMovieById(movieId)
}