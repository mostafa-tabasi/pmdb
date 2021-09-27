package com.mstf.pmdb.ui.main.archive.adapter

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mstf.pmdb.data.model.db.MovieEntity
import com.mstf.pmdb.utils.AppConstants.MEDIA_TYPE_TITLE_SERIES
import com.mstf.pmdb.utils.enums.RatingSite
import com.mstf.pmdb.utils.enums.RatingSite.*

class ArchiveItemViewModel(private val item: MovieEntity) {

  val poster = ObservableField<String?>().apply { set(item.poster) }
  val title = ObservableField<String>().apply { set(item.title.replace(" ", "\n")) }
  val isTv = ObservableField<Boolean>().apply { set(item.type == MEDIA_TYPE_TITLE_SERIES) }
  val yearStart = ObservableField<String>().apply { item.yearStart?.let { set(it.toString()) } }
  val yearEnd = ObservableField<String>().apply { item.yearEnd?.let { set(it.toString()) } }
  val runtime = ObservableField<Int>().apply { set(item.runtime) }
  val genre = ObservableField<String>().apply { set(item.genre) }
  val imdbRate = ObservableField<Float?>().apply { set(item.imdbRate) }
  val rottenTomatoesRate = ObservableField<Int?>().apply { set(item.rottenTomatoesRate) }
  val metacriticRate = ObservableField<Int?>().apply { set(item.metacriticRate) }
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
  private val _isSelected = MutableLiveData(false)
  val isSelected: LiveData<Boolean> = _isSelected

  fun changeSelected(isSelected: Boolean?) {
    isSelected?.let { if (_isSelected.value != it) _isSelected.value = it }
  }

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