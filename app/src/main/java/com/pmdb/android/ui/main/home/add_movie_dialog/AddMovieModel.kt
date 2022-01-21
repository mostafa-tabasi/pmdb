package com.pmdb.android.ui.main.home.add_movie_dialog

import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.pmdb.android.data.model.api.MatchedMovie
import com.pmdb.android.data.model.db.MovieEntity
import com.pmdb.android.utils.AppConstants.MEDIA_TYPE_TITLE_MOVIE
import com.pmdb.android.utils.AppConstants.MEDIA_TYPE_TITLE_SERIES
import com.pmdb.android.utils.AppConstants.RATING_SOURCE_IMDB
import com.pmdb.android.utils.AppConstants.RATING_SOURCE_METACRITIC
import com.pmdb.android.utils.AppConstants.RATING_SOURCE_ROTTEN_TOMATOES
import com.pmdb.android.utils.extensions.ifAvailable
import com.pmdb.android.utils.extensions.isNullOrEmptyAfterTrim

class AddMovieModel : BaseObservable() {

  var dbId: Long = -1L
  var dbCreatedAt: Long = -1L
  val title = ObservableField<String>()
  val yearStart = ObservableField<String>()
  val yearEnd = ObservableField<String>()
  val imdbID = ObservableField<String>()
  val poster = ObservableField<String>()
  val type = ObservableField<String>().apply { set(MEDIA_TYPE_TITLE_MOVIE) }
  val tv = ObservableField<Boolean>().apply { set(false) }
  val runtime = ObservableField<String>()
  val country = ObservableField<String>()
  val imdbRate = ObservableField<String>()
  val imdbVotes = ObservableField<String>()
  val rottenTomatoesRate = ObservableField<String>()
  val metacriticRate = ObservableField<String>()
  val genre = ObservableField<String>().apply { set("") }
  val director = ObservableField<String>()
  val writer = ObservableField<String>()
  val actors = ObservableField<String>()
  val plot = ObservableField<String>()
  val awards = ObservableField<String>()
  val comment = ObservableField<String>()
  val watched = ObservableField<Boolean>().apply { set(false) }
  var watchAt: Long? = null
  val favorite = ObservableField<Boolean>().apply { set(false) }
  val canSave = ObservableField<Boolean>().apply { set(false) }

  init {
    title.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
      override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
        canSave.set(!title.get().isNullOrEmptyAfterTrim())
      }
    })
    type.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
      override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
        tv.set(type.get() == MEDIA_TYPE_TITLE_SERIES)
      }
    })
  }

  fun update(movie: MatchedMovie) {
    title.set(movie.title.ifAvailable())
    imdbID.set(movie.imdbID.ifAvailable())
    poster.set(movie.poster)
    type.set(movie.type.ifAvailable())
    runtime.set(movie.runtime?.replace("min", "")?.replace(" ", "").ifAvailable())
    country.set(movie.country.ifAvailable())
    movie.ratings?.let {
      it.forEach { rate ->
        when (rate.source) {
          RATING_SOURCE_IMDB -> imdbRate.set(rate.value.replace("/10", "").ifAvailable())
          RATING_SOURCE_ROTTEN_TOMATOES -> rottenTomatoesRate.set(
            rate.value.replace("%", "").ifAvailable()
          )
          RATING_SOURCE_METACRITIC -> metacriticRate.set(
            rate.value.replace("/100", "").ifAvailable()
          )
        }
      }
    }
    imdbVotes.set(movie.imdbVotes.ifAvailable())
    genre.set(movie.genre.ifAvailable())
    director.set(movie.director.ifAvailable())
    writer.set(movie.writer.ifAvailable())
    actors.set(movie.actors.ifAvailable())
    plot.set(movie.plot.ifAvailable())
    awards.set(movie.awards.ifAvailable())
    movie.year?.let {
      if (tv.get() == true && it.contains("–")) {
        yearStart.set(it.split("–")[0].ifAvailable())
        yearEnd.set(it.split("–")[1].ifAvailable())
      } else yearStart.set(it.ifAvailable())
    }
    comment.set(null)
    watched.set(false)
    favorite.set(false)
  }

  fun update(movie: MovieEntity) {
    dbId = movie.id!!
    dbCreatedAt = movie.createdAt
    title.set(movie.title.ifAvailable())
    movie.yearStart?.let { yearStart.set(it.toString()) }
    movie.yearEnd?.let { yearEnd.set(it.toString()) }
    imdbID.set(movie.imdbID.ifAvailable())
    poster.set(movie.poster)
    type.set(movie.type.ifAvailable())
    tv.set(type.get() == MEDIA_TYPE_TITLE_SERIES)
    movie.runtime?.let { runtime.set(it.toString()) }
    country.set(movie.country.ifAvailable())
    movie.imdbRate?.let { imdbRate.set(it.toString()) }
    movie.imdbVotes?.let { imdbVotes.set(it.toString()) }
    movie.rottenTomatoesRate?.let { rottenTomatoesRate.set(it.toString()) }
    movie.metacriticRate?.let { metacriticRate.set(it.toString()) }
    genre.set(movie.genre.ifAvailable())
    director.set(movie.director.ifAvailable())
    writer.set(movie.writer.ifAvailable())
    actors.set(movie.actors.ifAvailable())
    plot.set(movie.plot.ifAvailable())
    awards.set(movie.awards.ifAvailable())
    comment.set(movie.comment)
    watched.set(movie.watch)
    watchAt = movie.watchedAt
    favorite.set(movie.favorite)

  }

  fun clear() {
    dbId = -1L
    dbCreatedAt = -1L
    title.set(null)
    imdbID.set(null)
    poster.set("N/A")
    type.set(MEDIA_TYPE_TITLE_MOVIE)
    runtime.set(null)
    country.set(null)
    imdbRate.set(null)
    rottenTomatoesRate.set(null)
    metacriticRate.set(null)
    imdbVotes.set(null)
    genre.set("")
    director.set(null)
    writer.set(null)
    actors.set(null)
    plot.set(null)
    awards.set(null)
    yearStart.set(null)
    yearEnd.set(null)
    comment.set(null)
    watched.set(false)
    favorite.set(false)
  }
}