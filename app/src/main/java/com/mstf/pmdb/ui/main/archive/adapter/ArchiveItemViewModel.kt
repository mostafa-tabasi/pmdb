package com.mstf.pmdb.ui.main.archive.adapter

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mstf.pmdb.data.model.db.MovieEntity
import com.mstf.pmdb.utils.AppConstants.MEDIA_TYPE_TITLE_SERIES

class ArchiveItemViewModel(item: MovieEntity) {

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
  private val _isSelected = MutableLiveData(false)
  val isSelected: LiveData<Boolean> = _isSelected

  fun changeSelected(isSelected: Boolean?) {
    isSelected?.let { if (_isSelected.value != it) _isSelected.value = it }
  }
}