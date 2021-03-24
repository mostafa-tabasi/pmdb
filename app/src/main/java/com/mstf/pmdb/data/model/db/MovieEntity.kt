package com.mstf.pmdb.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mstf.pmdb.ui.main.home.add_movie_dialog.AddMovieModel

@Entity(tableName = "movie")
data class MovieEntity(
  @PrimaryKey(autoGenerate = true) var id: Long? = null,
  var title: String,
  @ColumnInfo(name = "year_start") var yearStart: Int? = null,
  @ColumnInfo(name = "year_end") var yearEnd: Int? = null,
  @ColumnInfo(name = "imdb_id") var imdbID: String? = null,
  var poster: String? = null,
  var type: String? = null,
  var runtime: Int? = null,
  var country: String? = null,
  @ColumnInfo(name = "imdb_rate") var imdbRate: Float? = null,
  @ColumnInfo(name = "imdb_votes") var imdbVotes: Long? = null,
  @ColumnInfo(name = "rotten_tomatoes_rate") var rottenTomatoesRate: Int? = null,
  @ColumnInfo(name = "metacritic_rate") var metacriticRate: Int? = null,
  var genre: String? = null,
  var director: String? = null,
  var writer: String? = null,
  var actors: String? = null,
  var plot: String? = null,
  var awards: String? = null,
  var comment: String? = null,
  var favorite: Boolean = false,
  var watch: Boolean = false,
  @ColumnInfo(name = "watched_at") var watchedAt: Long? = null,
  @ColumnInfo(name = "created_at") var createdAt: Long,
) {
  companion object {

    /**
     * ساخت مدل فیلم متناسب با دیتابیس براساس مدل موردنظر
     */
    fun from(movie: AddMovieModel): MovieEntity {
      val currentTime = System.currentTimeMillis()
      with(movie) {
        return MovieEntity(
          title = title.get()!!,
          yearStart = yearStart.get()?.toIntOrNull(),
          yearEnd = yearEnd.get()?.toIntOrNull(),
          imdbID = imdbID.get(),
          poster = poster.get(),
          type = type.get(),
          runtime = runtime.get()?.toIntOrNull(),
          country = country.get(),
          imdbRate = imdbRate.get()?.toFloatOrNull(),
          imdbVotes = imdbVotes.get()?.toLongOrNull(),
          rottenTomatoesRate = rottenTomatoesRate.get()?.toIntOrNull(),
          metacriticRate = metacriticRate.get()?.toIntOrNull(),
          genre = genre.get(),
          director = director.get(),
          writer = writer.get(),
          actors = actors.get(),
          plot = plot.get(),
          awards = awards.get(),
          comment = comment.get(),
          favorite = favorite.get() ?: false,
          watch = seen.get() ?: false,
          watchedAt = if (seen.get() == true) currentTime else null,
          createdAt = currentTime
        )
      }
    }
  }
}