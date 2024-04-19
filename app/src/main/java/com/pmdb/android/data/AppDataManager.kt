package com.pmdb.android.data

import android.content.Context
import androidx.paging.DataSource
import com.google.gson.Gson
import com.pmdb.android.data.local.db.DbHelper
import com.pmdb.android.data.local.prefs.PreferencesHelper
import com.pmdb.android.data.model.api.MatchedMovie
import com.pmdb.android.data.model.api.MatchedMovieList
import com.pmdb.android.data.model.db.MovieEntity
import com.pmdb.android.data.remote.ApiHelper
import com.pmdb.android.data.resource.ResourceHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager @Inject constructor(
  @ApplicationContext private val mContext: Context,
  private val dbHelper: DbHelper,
  private val preferencesHelper: PreferencesHelper,
  private val apiHelper: ApiHelper,
  private val resourceHelper: ResourceHelper,
  private val gson: Gson
) : DataManager {

  override var isRecentMoviesEnable: Boolean
    get() = preferencesHelper.isRecentMoviesEnable
    set(value) {
      preferencesHelper.isRecentMoviesEnable = value
    }

  override var isRecentSeriesEnable: Boolean
    get() = preferencesHelper.isRecentSeriesEnable
    set(value) {
      preferencesHelper.isRecentSeriesEnable = value
    }

  override var isTopRatedEnable: Boolean
    get() = preferencesHelper.isTopRatedEnable
    set(value) {
      preferencesHelper.isTopRatedEnable = value
    }

  override var topRatedMethod: String
    get() = preferencesHelper.topRatedMethod
    set(value) {
      preferencesHelper.topRatedMethod = value
    }

  override var isRecentlyWatchedEnable: Boolean
    get() = preferencesHelper.isRecentlyWatchedEnable
    set(value) {
      preferencesHelper.isRecentlyWatchedEnable = value
    }

  override var archiveDefaultItemRate: String
    get() = preferencesHelper.archiveDefaultItemRate
    set(value) {
      preferencesHelper.archiveDefaultItemRate = value
    }

  override var archiveDefaultItemViewType: String
    get() = preferencesHelper.archiveDefaultItemViewType
    set(value) {
      preferencesHelper.archiveDefaultItemViewType = value
    }

  override var isSystemDefaultThemeEnable: Boolean
    get() = preferencesHelper.isSystemDefaultThemeEnable
    set(value) {
      preferencesHelper.isSystemDefaultThemeEnable = value
    }

  override var appTheme: String
    get() = preferencesHelper.appTheme
    set(value) {
      preferencesHelper.appTheme = value
    }

  override suspend fun findMovieByTitle(title: String): Response<MatchedMovieList> =
    apiHelper.findMovieByTitle(title)

  override suspend fun findMovieByImdbId(id: String): Response<MatchedMovie> =
    apiHelper.findMovieByImdbId(id)

  override fun getString(resourceId: Int) = resourceHelper.getString(resourceId)

  override fun getArrayString(resourceId: Int) = resourceHelper.getArrayString(resourceId)

  override fun getMovieGenreList() = resourceHelper.getMovieGenreList()

  override fun getTvSeriesGenreList() = resourceHelper.getTvSeriesGenreList()

  override suspend fun insertMovie(movie: MovieEntity) = dbHelper.insertMovie(movie)

  override suspend fun deleteMovieById(id: Long): Int? = dbHelper.deleteMovieById(id)

  override suspend fun getAllMovies() = dbHelper.getAllMovies()

  override fun allMoviesAndTvSeriesByDate(): DataSource.Factory<Int, MovieEntity> =
    dbHelper.allMoviesAndTvSeriesByDate()

  override fun allMoviesAndTvSeriesByImdbRate(): DataSource.Factory<Int, MovieEntity> =
    dbHelper.allMoviesAndTvSeriesByImdbRate()

  override fun allMoviesAndTvSeriesByRottenTomatoesRate(): DataSource.Factory<Int, MovieEntity> =
    dbHelper.allMoviesAndTvSeriesByRottenTomatoesRate()

  override fun allMoviesAndTvSeriesByMetacriticRate(): DataSource.Factory<Int, MovieEntity> =
    dbHelper.allMoviesAndTvSeriesByMetacriticRate()

  override fun allMoviesAndTvSeriesByWatchDate(): DataSource.Factory<Int, MovieEntity> =
    dbHelper.allMoviesAndTvSeriesByWatchDate()

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