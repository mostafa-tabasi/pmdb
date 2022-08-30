package com.pmdb.android.data.model.firestore

import androidx.annotation.Keep
import com.pmdb.android.data.model.db.MovieEntity
import com.pmdb.android.utils.AppConstants
import com.pmdb.android.utils.AppUtils

@Keep
data class User(
  val uid: String,
  val name: String? = null,
  val email: String? = null,
  val photo_url: String? = null,
) {
  constructor() : this("")

  @Keep
  data class Movie(
    val uuid: String,
    val title: String,
    val year_start: Int? = null,
    val year_end: Int? = null,
    val imdb_id: String? = null,
    val poster: String? = null,
    val type: String? = AppConstants.MEDIA_TYPE_TITLE_MOVIE,
    val runtime: Int? = null,
    val country: String? = null,
    val imdb_rate: Float? = null,
    val imdb_votes: Long? = null,
    val rotten_tomatoes_rate: Int? = null,
    val metacritic_rate: Int? = null,
    val genre: String? = null,
    val director: String? = null,
    val writer: String? = null,
    val actors: String? = null,
    val plot: String? = null,
    val awards: String? = null,
    val comment: String? = null,
    val favorite: Boolean = false,
    val watch: Boolean = false,
    val watched_at: Long? = null,
    val created_at: Long = 0,
  ) {
    constructor() : this("", "")

    companion object {
      fun build(movie: MovieEntity) = with(movie) {
        Movie(
          AppUtils.getUUID(),
          title,
          yearStart,
          yearEnd,
          imdbID,
          poster,
          type,
          runtime,
          country,
          imdbRate,
          imdbVotes,
          rottenTomatoesRate,
          metacriticRate,
          genre,
          director,
          writer,
          actors,
          plot,
          awards,
          comment,
          favorite,
          watch,
          watchedAt,
          createdAt
        )
      }
    }
  }
}
