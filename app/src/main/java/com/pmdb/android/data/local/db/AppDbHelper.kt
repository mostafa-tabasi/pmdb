package com.pmdb.android.data.local.db

import com.pmdb.android.data.model.db.MovieEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDbHelper @Inject constructor(private val appDatabase: AppDatabase) : DbHelper {

  override suspend fun insertMovie(movie: MovieEntity) = appDatabase.movieDao().insert(movie)

  override suspend fun deleteMovieById(id: Long): Int? = appDatabase.movieDao().delete(id)

  override suspend fun getAllMovies() = appDatabase.movieDao().getAllMovies()

  override fun allMoviesAndTvSeriesByDate() = appDatabase.movieDao().allMoviesAndTvSeriesByDate()

  override fun allMoviesAndTvSeriesByImdbRate() =
    appDatabase.movieDao().allMoviesAndTvSeriesByImdbRate()

  override fun allMoviesAndTvSeriesByRottenTomatoesRate() =
    appDatabase.movieDao().allMoviesAndTvSeriesByRottenTomatoesRate()

  override fun allMoviesAndTvSeriesByMetacriticRate() =
    appDatabase.movieDao().allMoviesAndTvSeriesByMetacriticRate()

  override fun allMoviesAndTvSeriesByWatchDate() =
    appDatabase.movieDao().allMoviesAndTvSeriesByWatchDate()

  override fun filterAllArchive(title: String?, fromYear: Int?, toYear: Int?, director: String?) =
    appDatabase.movieDao()
      .filterAllArchive(title ?: "", fromYear ?: -1, toYear ?: 9999, director ?: "")

  override fun allMoviesByDate() = appDatabase.movieDao().allMoviesByDate()

  override fun filterMovies(title: String?, fromYear: Int?, toYear: Int?, director: String?) =
    appDatabase.movieDao()
      .filterMovies(title ?: "", fromYear ?: 0, toYear ?: 9999, director ?: "")

  override fun allTvSeriesByDate() = appDatabase.movieDao().allTvSeriesByDate()

  override fun filterTvSeries(title: String?, fromYear: Int?, toYear: Int?, director: String?) =
    appDatabase.movieDao()
      .filterTvSeries(title ?: "", fromYear ?: 0, toYear ?: 9999, director ?: "")

  override suspend fun getMovieByImdbId(id: String): MovieEntity? =
    appDatabase.movieDao().getMovieByImdbId(id)

  override suspend fun updateWatchState(id: Long, watched: Boolean) =
    // اگر فیلم دیده شده بود، تاریخ فعلی بعنوان تاریخی که فیلم در آن دیده شده است ثبت میشود
    appDatabase.movieDao()
      .updateWatchState(id, watched, if (watched) System.currentTimeMillis() else 0L)

  override suspend fun updateFavoriteState(id: Long, favorite: Boolean) =
    appDatabase.movieDao().updateFavoriteState(id, favorite)

  override suspend fun updateMovie(movie: MovieEntity) = appDatabase.movieDao().update(movie)

  override suspend fun findMovieById(movieId: Long) = appDatabase.movieDao().findById(movieId)

}