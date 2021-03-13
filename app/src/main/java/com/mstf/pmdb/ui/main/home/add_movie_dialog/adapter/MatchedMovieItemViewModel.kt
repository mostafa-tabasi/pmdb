package com.mstf.pmdb.ui.main.home.add_movie_dialog.adapter

import androidx.databinding.ObservableField
import com.mstf.pmdb.data.model.api.MatchedMovieCompact
import com.mstf.pmdb.utils.AppConstants.MEDIA_TYPE_TITLE_EPISODE
import com.mstf.pmdb.utils.AppConstants.MEDIA_TYPE_TITLE_SERIES

class MatchedMovieItemViewModel(item: MatchedMovieCompact) {

  val title = ObservableField<String>().apply { set(item.title) }
  val year = ObservableField<String>().apply { set(item.year) }
  val poster = ObservableField<String?>().apply { set(item.poster) }
  val tv = ObservableField<Boolean>()
    .apply { set(item.type == MEDIA_TYPE_TITLE_SERIES || item.type == MEDIA_TYPE_TITLE_EPISODE) }
  val loading = ObservableField<Boolean>().apply { set(item.loading) }
}