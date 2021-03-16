package com.mstf.pmdb.ui.main.home.add_movie_dialog

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import com.mstf.pmdb.data.model.api.MatchedMovie
import com.mstf.pmdb.utils.AppConstants.MEDIA_TYPE_TITLE_EPISODE
import com.mstf.pmdb.utils.AppConstants.MEDIA_TYPE_TITLE_SERIES
import com.mstf.pmdb.utils.AppConstants.RATING_SOURCE_IMDB
import com.mstf.pmdb.utils.AppConstants.RATING_SOURCE_METACRITIC
import com.mstf.pmdb.utils.AppConstants.RATING_SOURCE_ROTTEN_TOMATOES

class AddMovieModel : BaseObservable() {

  val title = ObservableField<String>()
  val year = ObservableField<String>()
  val imdbID = ObservableField<String>()
  val poster = ObservableField<String>()
  val type = ObservableField<String>()
  val tv = ObservableField<Boolean>()
    .apply { set(type.get() == MEDIA_TYPE_TITLE_SERIES || type.get() == MEDIA_TYPE_TITLE_EPISODE) }
  val runtime = ObservableField<String>()
  val country = ObservableField<String>()
  val imdbRate = ObservableField<String>()
  val imdbVotes = ObservableField<String>()
  val rottenTomatoesRate = ObservableField<String>()
  val metacriticRate = ObservableField<String>()
  val genre = ObservableField<String>()
  val director = ObservableField<String>()
  val writer = ObservableField<String>()
  val actors = ObservableField<String>()
  val plot = ObservableField<String>()
  val awards = ObservableField<String>()
  val comment = ObservableField<String>()
  val seen = ObservableField<Boolean>().apply { set(false) }
  val favorite = ObservableField<Boolean>().apply { set(false) }

  fun update(movie: MatchedMovie) {
    title.set(movie.title)
    imdbID.set(movie.imdbID)
    poster.set(movie.poster)
    type.set(movie.type)
    tv.set(type.get() == MEDIA_TYPE_TITLE_SERIES || type.get() == MEDIA_TYPE_TITLE_EPISODE)
    runtime.set(movie.runtime)
    country.set(movie.country)
    movie.ratings?.let {
      it.forEach { rate ->
        when (rate.source) {
          RATING_SOURCE_IMDB -> imdbRate.set(rate.value)
          RATING_SOURCE_ROTTEN_TOMATOES -> rottenTomatoesRate.set(rate.value)
          RATING_SOURCE_METACRITIC -> metacriticRate.set(rate.value)
        }
      }
    }
    imdbVotes.set(movie.imdbVotes)
    genre.set(movie.genre)
    director.set(movie.director)
    writer.set(movie.writer)
    actors.set(movie.actors)
    plot.set(movie.plot)
    awards.set(movie.awards)
    year.set(movie.year)
    comment.set(null)
    seen.set(false)
    favorite.set(false)
  }

  fun clear() {
    title.set(null)
    imdbID.set(null)
    poster.set("N/A")
    type.set(null)
    tv.set(false)
    runtime.set(null)
    country.set(null)
    imdbRate.set(null)
    rottenTomatoesRate.set(null)
    metacriticRate.set(null)
    imdbVotes.set(null)
    genre.set(null)
    director.set(null)
    writer.set(null)
    actors.set(null)
    plot.set(null)
    awards.set(null)
    year.set(null)
    comment.set(null)
    seen.set(false)
    favorite.set(false)
  }
}