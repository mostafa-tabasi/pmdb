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
  val title = ObservableField("")
  val year = ObservableField("")
  val yearStart = ObservableField("")
  val yearEnd = ObservableField("")
  val imdbID = ObservableField("")
  val poster = ObservableField("")
  val type = ObservableField<String>().apply { set(MEDIA_TYPE_TITLE_MOVIE) }
  val tv = ObservableField<Boolean>().apply { set(false) }
  val runtime = ObservableField("")
  val country = ObservableField("")
  val imdbRate = ObservableField("")
  val imdbVotes = ObservableField("")
  val rottenTomatoesRate = ObservableField("")
  val metacriticRate = ObservableField("")
  val genre = ObservableField<String>().apply { set("") }
  val director = ObservableField("")
  val writer = ObservableField("")
  val actors = ObservableField("")
  val plot = ObservableField("")
  val awards = ObservableField("")
  val comment = ObservableField("")
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
        setMovieYear()
      }
    })
    yearStart.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
      override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
        setMovieYear()
      }
    })
    yearEnd.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
      override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
        setMovieYear()
      }
    })
  }

  /**
   * ست کردن مقدار نهایی جهت نمایش سال تولید فیلم یا سریال با توجه به شرایط متفاوت
   */
  private fun setMovieYear() {
    year.set(
      if ((yearStart.get() == null || yearStart.get() == "-1") && yearEnd.get() == null) ""
      else {
        val yearStart =
          if (yearStart.get() == null || yearStart.get() == "-1") "" else yearStart.get()
        val yearEnd = yearEnd.get() ?: ""

        if (yearStart.isNullOrEmpty() && yearEnd.isEmpty()) ""
        else if (tv.get() == true) "($yearStart - $yearEnd)"
        else if (tv.get() != true && !yearStart.isNullOrEmpty()) "($yearStart)"
        else ""
      }
    )
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
    movie.yearStart?.let { if (it != -1) yearStart.set(it.toString()) }
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

  /**
   * بررسی امتیاز وارد شده در فیلد امتیاز IMDb (عدد وارد شده بیشتر از 10 نباید باشد)
   */
  fun onImdbRateChange(s: CharSequence, start: Int, before: Int, count: Int) {
    if (s.isEmpty()) return
    val rate = s.toString().toFloatOrNull() ?: return
    if (rate > 10) setImdbRateToMaximum()
    else imdbRate.set(s.toString())
  }

  private fun setImdbRateToMaximum() = imdbRate.set("10")

  /**
   * بررسی امتیاز وارد شده در فیلد امتیاز Rotten Tomatoes (عدد وارد شده بیشتر از 100 نباید باشد)
   */
  fun onRottenTomatoesRateChange(s: CharSequence, start: Int, before: Int, count: Int) {
    if (s.isEmpty()) return
    val rate = s.toString().toIntOrNull() ?: return
    if (rate > 100) setRottenTomatoesRateToMaximum()
    else rottenTomatoesRate.set(s.toString())
  }

  private fun setRottenTomatoesRateToMaximum() = rottenTomatoesRate.set("100")

  /**
   * بررسی امتیاز وارد شده در فیلد امتیاز Metacritic (عدد وارد شده بیشتر از 100 نباید باشد)
   */
  fun onMetacriticRateChange(s: CharSequence, start: Int, before: Int, count: Int) {
    if (s.isEmpty()) return
    val rate = s.toString().toIntOrNull() ?: return
    if (rate > 100) setMetacriticRateToMaximum()
    else metacriticRate.set(s.toString())
  }

  private fun setMetacriticRateToMaximum() = metacriticRate.set("100")

  fun clear() {
    dbId = -1L
    dbCreatedAt = -1L
    title.set("")
    imdbID.set("")
    poster.set("")
    type.set(MEDIA_TYPE_TITLE_MOVIE)
    runtime.set("")
    country.set("")
    imdbRate.set("")
    rottenTomatoesRate.set("")
    metacriticRate.set("")
    imdbVotes.set("")
    genre.set("")
    director.set("")
    writer.set("")
    actors.set("")
    plot.set("")
    awards.set("")
    yearStart.set("")
    yearEnd.set("")
    comment.set("")
    watched.set(false)
    favorite.set(false)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as AddMovieModel

    if (dbId != other.dbId) return false
    if (dbCreatedAt != other.dbCreatedAt) return false
    if (title.get() != other.title.get()) return false
    if (yearStart.get() != other.yearStart.get()) return false
    if (yearEnd.get() != other.yearEnd.get()) return false
    if (imdbID.get() != other.imdbID.get()) return false
    if (poster.get() != other.poster.get()) return false
    if (type.get() != other.type.get()) return false
    if (tv.get() != other.tv.get()) return false
    if (runtime.get() != other.runtime.get()) return false
    if (country.get() != other.country.get()) return false
    if (imdbRate.get() != other.imdbRate.get()) return false
    if (imdbVotes.get() != other.imdbVotes.get()) return false
    if (rottenTomatoesRate.get() != other.rottenTomatoesRate.get()) return false
    if (metacriticRate.get() != other.metacriticRate.get()) return false
    if (genre.get() != other.genre.get()) return false
    if (director.get() != other.director.get()) return false
    if (writer.get() != other.writer.get()) return false
    if (actors.get() != other.actors.get()) return false
    if (plot.get() != other.plot.get()) return false
    if (awards.get() != other.awards.get()) return false
    if (comment.get() != other.comment.get()) return false
    if (watched.get() != other.watched.get()) return false
    if (watchAt != other.watchAt) return false
    if (favorite.get() != other.favorite.get()) return false
    if (canSave.get() != other.canSave.get()) return false

    return true
  }

  override fun hashCode(): Int {
    var result = dbId.hashCode()
    result = 31 * result + dbCreatedAt.hashCode()
    result = 31 * result + title.hashCode()
    result = 31 * result + yearStart.hashCode()
    result = 31 * result + yearEnd.hashCode()
    result = 31 * result + imdbID.hashCode()
    result = 31 * result + poster.hashCode()
    result = 31 * result + type.hashCode()
    result = 31 * result + tv.hashCode()
    result = 31 * result + runtime.hashCode()
    result = 31 * result + country.hashCode()
    result = 31 * result + imdbRate.hashCode()
    result = 31 * result + imdbVotes.hashCode()
    result = 31 * result + rottenTomatoesRate.hashCode()
    result = 31 * result + metacriticRate.hashCode()
    result = 31 * result + genre.hashCode()
    result = 31 * result + director.hashCode()
    result = 31 * result + writer.hashCode()
    result = 31 * result + actors.hashCode()
    result = 31 * result + plot.hashCode()
    result = 31 * result + awards.hashCode()
    result = 31 * result + comment.hashCode()
    result = 31 * result + watched.hashCode()
    result = 31 * result + (watchAt?.hashCode() ?: 0)
    result = 31 * result + favorite.hashCode()
    result = 31 * result + canSave.hashCode()
    return result
  }
}