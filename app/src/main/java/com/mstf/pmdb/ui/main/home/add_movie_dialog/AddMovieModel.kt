package com.mstf.pmdb.ui.main.home.add_movie_dialog

import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.mstf.pmdb.data.model.api.MatchedMovie
import com.mstf.pmdb.utils.AppConstants.MEDIA_TYPE_TITLE_MOVIE
import com.mstf.pmdb.utils.AppConstants.MEDIA_TYPE_TITLE_SERIES
import com.mstf.pmdb.utils.AppConstants.RATING_SOURCE_IMDB
import com.mstf.pmdb.utils.AppConstants.RATING_SOURCE_METACRITIC
import com.mstf.pmdb.utils.AppConstants.RATING_SOURCE_ROTTEN_TOMATOES
import com.mstf.pmdb.utils.extensions.isNullOrEmptyAfterTrim

class AddMovieModel : BaseObservable() {

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
  val seen = ObservableField<Boolean>().apply { set(false) }
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
    title.set(movie.title)
    imdbID.set(movie.imdbID)
    poster.set(movie.poster)
    type.set(movie.type)
    runtime.set(movie.runtime?.replace("min", "")?.replace(" ", ""))
    country.set(movie.country)
    movie.ratings?.let {
      it.forEach { rate ->
        when (rate.source) {
          RATING_SOURCE_IMDB -> imdbRate.set(rate.value.replace("/10", ""))
          RATING_SOURCE_ROTTEN_TOMATOES -> rottenTomatoesRate.set(rate.value.replace("%", ""))
          RATING_SOURCE_METACRITIC -> metacriticRate.set(rate.value.replace("/100", ""))
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
    movie.year?.let {
      if (tv.get() == true && it.contains("–")) {
        yearStart.set(it.split("–")[0])
        yearEnd.set(it.split("–")[1])
      } else yearStart.set(it)
    }
    comment.set(null)
    seen.set(false)
    favorite.set(false)
  }

  fun clear() {
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
    seen.set(false)
    favorite.set(false)
  }
}