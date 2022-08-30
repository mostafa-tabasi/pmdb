package com.pmdb.android.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pmdb.android.ui.main.home.add_movie_dialog.AddMovieModel
import com.pmdb.android.utils.AppConstants.MEDIA_TYPE_TITLE_MOVIE

@Entity(tableName = "movie")
data class MovieEntity(
  @PrimaryKey(autoGenerate = true) var id: Long? = null,
  var title: String,
  @ColumnInfo(name = "year_start") var yearStart: Int? = null,
  @ColumnInfo(name = "year_end") var yearEnd: Int? = null,
  @ColumnInfo(name = "imdb_id") var imdbID: String? = null,
  var poster: String? = null,
  var type: String? = MEDIA_TYPE_TITLE_MOVIE,
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
          yearStart = if (yearStart.get().isNullOrEmpty()) -1 else yearStart.get()!!.toInt(),
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
          watch = watched.get() ?: false,
          watchedAt = if (watched.get() == true) currentTime else null,
          createdAt = currentTime
        ).also {
          // ست کردن اطلاعاتی که برای بروزرسانی نیاز است (در صورت موجود بودن)
          if (movie.dbId != -1L) it.id = movie.dbId
          if (movie.dbCreatedAt != -1L) it.createdAt = movie.dbCreatedAt
          if (movie.watchAt != null) it.watchedAt = movie.watchAt
        }
      }
    }
  }
}