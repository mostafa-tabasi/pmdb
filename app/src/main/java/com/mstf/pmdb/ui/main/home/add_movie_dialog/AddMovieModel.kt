package com.mstf.pmdb.ui.main.home.add_movie_dialog

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.mstf.pmdb.BR
import com.mstf.pmdb.utils.AppConstants.MEDIA_TYPE_TITLE_EPISODE
import com.mstf.pmdb.utils.AppConstants.MEDIA_TYPE_TITLE_MOVIE
import com.mstf.pmdb.utils.AppConstants.MEDIA_TYPE_TITLE_SERIES

class AddMovieModel : BaseObservable() {

  @get:Bindable
  var title: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.title)
    }

  @get:Bindable
  var year: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.year)
    }

  @get:Bindable
  var imdbID: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.imdbID)
    }

  @get:Bindable
  var poster: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.poster)
    }

  @get:Bindable
  var type: String? = MEDIA_TYPE_TITLE_MOVIE
    set(value) {
      field = value
      notifyPropertyChanged(BR.type)
      tv = value == MEDIA_TYPE_TITLE_SERIES || value == MEDIA_TYPE_TITLE_EPISODE
    }

  @get:Bindable
  var tv: Boolean = false
    set(value) {
      field = value
      notifyPropertyChanged(BR.tv)
    }

  @get:Bindable
  var runtime: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.runtime)
    }

  @get:Bindable
  var country: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.country)
    }

  @get:Bindable
  var imdbRate: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.imdbRate)
    }

  @get:Bindable
  var imdbVotes: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.imdbVotes)
    }

  @get:Bindable
  var rottenTomatoesRate: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.rottenTomatoesRate)
    }

  @get:Bindable
  var metacriticRate: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.metacriticRate)
    }

  @get:Bindable
  var genre: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.genre)
    }

  @get:Bindable
  var director: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.director)
    }

  @get:Bindable
  var writer: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.writer)
    }

  @get:Bindable
  var actors: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.actors)
    }

  @get:Bindable
  var plot: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.plot)
    }

  @get:Bindable
  var awards: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.awards)
    }

  @get:Bindable
  var comment: String? = null
    set(value) {
      field = value
      notifyPropertyChanged(BR.comment)
    }

  @get:Bindable
  var seen: Boolean = false
    set(value) {
      field = value
      notifyPropertyChanged(BR.seen)
    }

  @get:Bindable
  var favorite: Boolean = false
    set(value) {
      field = value
      notifyPropertyChanged(BR.favorite)
    }

  /*
  fun update(movieResponse: MovieResponse) {
    title = movieResponse.title
    imdbID = movieResponse.imdbID
    poster = movieResponse.poster
    type = movieResponse.type
    runtime = movieResponse.runtime
    country = movieResponse.country
    movieResponse.ratings?.let {
      it.forEach { rate ->
        when (rate.source) {
          RATING_SOURCE_IMDB -> imdbRate = rate.value
          RATING_SOURCE_ROTTEN_TOMATOES -> rottenTomatoesRate = rate.value
          RATING_SOURCE_METACRITIC -> metacriticRate = rate.value
        }
      }
    }
    imdbVotes = movieResponse.imdbVotes
    genre = movieResponse.genre
    director = movieResponse.director
    writer = movieResponse.writer
    actors = movieResponse.actors
    plot = movieResponse.plot
    awards = movieResponse.awards
    year = movieResponse.year
    comment = null
    seen = false
    favorite = false
  }
  */

  fun clear() {
    title = null
    imdbID = null
    poster = "N/A"
    type = MEDIA_TYPE_TITLE_MOVIE
    runtime = null
    country = null
    imdbRate = null
    rottenTomatoesRate = null
    metacriticRate = null
    imdbVotes = null
    genre = null
    director = null
    writer = null
    actors = null
    plot = null
    awards = null
    year = null
    comment = null
    seen = false
    favorite = false
  }
}