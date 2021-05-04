package com.mstf.pmdb.data.local.db

import com.mstf.pmdb.data.model.db.MovieEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDbHelper @Inject constructor(private val appDatabase: AppDatabase) : DbHelper {

  override suspend fun insertMovie(movie: MovieEntity) = appDatabase.movieDao().insert(movie)

  override suspend fun getAllMovies() = appDatabase.movieDao().getAllMovies()

  override suspend fun getMovieByImdbId(id: String): MovieEntity? =
    appDatabase.movieDao().getMovieByImdbId(id)

  override suspend fun updateWatchState(id: Long, watched: Boolean) =
    // اگر فیلم دیده شده بود، تاریخ فعلی بعنوان تاریخی که فیلم در آن دیده شده است ثبت میشود
    appDatabase.movieDao().updateWatchState(id, watched, if (watched) System.currentTimeMillis() else 0L)

  override suspend fun updateFavoriteState(id: Long, favorite: Boolean) =
    appDatabase.movieDao().updateFavoriteState(id, favorite)

}