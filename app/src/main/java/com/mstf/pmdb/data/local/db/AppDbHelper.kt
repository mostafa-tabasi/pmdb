package com.mstf.pmdb.data.local.db

import com.mstf.pmdb.data.model.db.MovieEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDbHelper @Inject constructor(private val appDatabase: AppDatabase) : DbHelper {

  override suspend fun insertMovie(movie: MovieEntity) = appDatabase.movieDao().insert(movie)

  override suspend fun getAllMovies() = appDatabase.movieDao().getAllMovies()
}