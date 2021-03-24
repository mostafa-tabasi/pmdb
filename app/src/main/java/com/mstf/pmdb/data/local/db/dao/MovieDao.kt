package com.mstf.pmdb.data.local.db.dao

import androidx.room.*
import com.mstf.pmdb.data.model.db.MovieEntity

@Dao
interface MovieDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(movie: MovieEntity)

  @Update
  suspend fun update(movie: MovieEntity)

  @Delete
  suspend fun delete(movie: MovieEntity)

  @Query("SELECT * FROM movie WHERE title LIKE :title")
  suspend fun findByTitle(title: String): List<MovieEntity>?

  @Query("SELECT * FROM movie")
  suspend fun getAllMovies(): List<MovieEntity>?
}