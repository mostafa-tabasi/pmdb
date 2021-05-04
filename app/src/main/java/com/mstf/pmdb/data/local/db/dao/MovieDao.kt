package com.mstf.pmdb.data.local.db.dao

import androidx.room.*
import com.mstf.pmdb.data.model.db.MovieEntity

@Dao
interface MovieDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(movie: MovieEntity): Long

  @Update
  suspend fun update(movie: MovieEntity)

  @Delete
  suspend fun delete(movie: MovieEntity)

  @Query("SELECT * FROM movie WHERE title LIKE :title")
  suspend fun findByTitle(title: String): List<MovieEntity>?

  @Query("SELECT * FROM movie")
  suspend fun getAllMovies(): List<MovieEntity>?

  @Query("SELECT * FROM movie WHERE imdb_id = :id LIMIT 1")
  suspend fun getMovieByImdbId(id: String): MovieEntity?

  @Query("UPDATE movie SET watch = :isWatched, watched_at = :watchedAt WHERE id = :id ")
  suspend fun updateWatchState(id: Long, isWatched: Boolean, watchedAt: Long)

  @Query("UPDATE movie SET favorite = :favorite WHERE id = :id ")
  suspend fun updateFavoriteState(id: Long, favorite: Boolean)
}