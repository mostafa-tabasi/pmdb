package com.pmdb.android.ui.main.archive

import androidx.databinding.BaseObservable
import com.pmdb.android.utils.enums.MediaFilterType

class ArchiveFilterModel : BaseObservable() {

  var type: MediaFilterType = MediaFilterType.MOVIES
  var title: String? = null
  var fromYear: Int? = null
  var toYear: Int? = null
  var director: String? = null
}