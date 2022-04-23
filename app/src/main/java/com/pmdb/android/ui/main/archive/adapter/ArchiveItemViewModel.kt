package com.pmdb.android.ui.main.archive.adapter

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.pmdb.android.data.model.db.MovieEntity
import com.pmdb.android.utils.AppConstants.MEDIA_TYPE_TITLE_SERIES
import com.pmdb.android.utils.enums.RatingSite
import com.pmdb.android.utils.enums.RatingSite.*

class ArchiveItemViewModel(private val item: MovieEntity) {

  val poster = ObservableField<String?>().apply { set(item.poster) }
  val title = ObservableField<String>().apply { set(item.title.replace(" ", "\n")) }
  val isTv = ObservableField<Boolean>().apply { set(item.type == MEDIA_TYPE_TITLE_SERIES) }
  val year = ObservableField<String>().apply {
    // ست کردن مقدار نهایی جهت نمایش سال تولید فیلم یا سریال با توجه به شرایط متفاوت
    set(
      if ((item.yearStart == null || item.yearStart == -1) && item.yearEnd == null) ""
      else {
        val yearStart =
          if (item.yearStart == null || item.yearStart == -1) "" else "${item.yearStart}"
        val yearEnd = if (item.yearEnd == null) "" else "${item.yearEnd}"
        if (yearStart.isEmpty() && yearEnd.isEmpty()) ""
        else if (isTv.get() == true) "($yearStart - $yearEnd)"
        else if (isTv.get() != true && yearStart.isNotEmpty()) "($yearStart)"
        else ""
      }
    )
  }
  val runtime = ObservableField<Int>().apply { set(item.runtime) }
  val genre = ObservableField<String>().apply { set(item.genre) }
  val director = ObservableField<String>().apply { set(item.director) }
  val writer = ObservableField<String>().apply { set(item.writer) }
  val awards = ObservableField<String>().apply { set(item.awards) }
  val actors = ObservableField<String>().apply { set(item.actors) }
  val favorite = ObservableField<Boolean>().apply { set(item.favorite) }
  val watched = ObservableField<Boolean>().apply { set(item.watch) }
  val ratingSite = ObservableField<RatingSite?>().apply { set(whichSiteIsAvailable()) }
  val rate = ObservableField<String?>().apply { set(whicheverRateIsAvailable()) }
  val isRateVisible =
    ObservableBoolean().apply { set(item.imdbRate != null || item.rottenTomatoesRate != null || item.metacriticRate != null) }

  /**
   * ست کردن سایت که امتیاز فیلم بر اساس آن نمایش داده میشود
   */
  fun setRatingSite(site: RatingSite) {
    ratingSite.set(
      if (site == WHICHEVER_IS_AVAILABLE) {
        if (item.imdbRate != null) IMDB else if (item.rottenTomatoesRate != null) ROTTEN_TOMATOES else METACRITIC
      } else site
    )
    rate.set(when (site) {
      IMDB -> item.imdbRate?.toString()
      ROTTEN_TOMATOES -> item.rottenTomatoesRate?.let { "$it%" }
      METACRITIC -> item.metacriticRate?.toString()
      else -> whicheverRateIsAvailable()
    })
    isRateVisible.set(
      when (site) {
        IMDB -> item.imdbRate != null
        ROTTEN_TOMATOES -> item.rottenTomatoesRate != null
        METACRITIC -> item.metacriticRate != null
        else -> item.imdbRate != null || item.rottenTomatoesRate != null || item.metacriticRate != null
      }
    )
  }

  /**
   * امتیاز در این آیتم از کدام سایت موجود است
   */
  private fun whichSiteIsAvailable(): RatingSite {
    item.imdbRate?.let { return IMDB }
    item.rottenTomatoesRate?.let { return ROTTEN_TOMATOES }
    item.metacriticRate?.let { return METACRITIC }
    return WHICHEVER_IS_AVAILABLE
  }

  /**
   * امتیاز از هر سایتی که موجود است
   */
  private fun whicheverRateIsAvailable(): String? {
    item.imdbRate?.let { return it.toString() }
    item.rottenTomatoesRate?.let { return "$it%" }
    item.metacriticRate?.let { return it.toString() }
    return null
  }
}