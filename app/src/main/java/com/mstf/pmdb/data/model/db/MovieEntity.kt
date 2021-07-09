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

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as MovieEntity

    if (id != other.id) return false
    if (title != other.title) return false
    if (yearStart != other.yearStart) return false
    if (yearEnd != other.yearEnd) return false
    if (imdbID != other.imdbID) return false
    if (poster != other.poster) return false
    if (type != other.type) return false
    if (runtime != other.runtime) return false
    if (country != other.country) return false
    if (imdbRate != other.imdbRate) return false
    if (imdbVotes != other.imdbVotes) return false
    if (rottenTomatoesRate != other.rottenTomatoesRate) return false
    if (metacriticRate != other.metacriticRate) return false
    if (genre != other.genre) return false
    if (director != other.director) return false
    if (writer != other.writer) return false
    if (actors != other.actors) return false
    if (plot != other.plot) return false
    if (awards != other.awards) return false
    if (comment != other.comment) return false
    if (favorite != other.favorite) return false
    if (watch != other.watch) return false
    if (watchedAt != other.watchedAt) return false
    if (createdAt != other.createdAt) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id?.hashCode() ?: 0
    result = 31 * result + title.hashCode()
    result = 31 * result + (yearStart ?: 0)
    result = 31 * result + (yearEnd ?: 0)
    result = 31 * result + (imdbID?.hashCode() ?: 0)
    result = 31 * result + (poster?.hashCode() ?: 0)
    result = 31 * result + (type?.hashCode() ?: 0)
    result = 31 * result + (runtime ?: 0)
    result = 31 * result + (country?.hashCode() ?: 0)
    result = 31 * result + (imdbRate?.hashCode() ?: 0)
    result = 31 * result + (imdbVotes?.hashCode() ?: 0)
    result = 31 * result + (rottenTomatoesRate ?: 0)
    result = 31 * result + (metacriticRate ?: 0)
    result = 31 * result + (genre?.hashCode() ?: 0)
    result = 31 * result + (director?.hashCode() ?: 0)
    result = 31 * result + (writer?.hashCode() ?: 0)
    result = 31 * result + (actors?.hashCode() ?: 0)
    result = 31 * result + (plot?.hashCode() ?: 0)
    result = 31 * result + (awards?.hashCode() ?: 0)
    result = 31 * result + (comment?.hashCode() ?: 0)
    result = 31 * result + favorite.hashCode()
    result = 31 * result + watch.hashCode()
    result = 31 * result + (watchedAt?.hashCode() ?: 0)
    result = 31 * result + createdAt.hashCode()
    return result
  }
}