package com.mstf.pmdb.data.resource

import android.content.Context
import com.mstf.pmdb.utils.enums.MovieGenre
import com.mstf.pmdb.utils.enums.TvGenre
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppResourceHelper @Inject constructor(private val context: Context) : ResourceHelper {

  override fun getString(resourceId: Int): String = context.getString(resourceId)

  override fun getMovieGenreList(): List<String> = MovieGenre.list()

  override fun getTvSeriesGenreList(): List<String> = TvGenre.list()

}