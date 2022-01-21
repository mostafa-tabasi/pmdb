package com.pmdb.android.ui.main.archive

interface ArchiveListener {

  fun onMovieTapped(movieId: Long): Boolean

  fun onMovieLongTouch(movieId: Long): Boolean

}