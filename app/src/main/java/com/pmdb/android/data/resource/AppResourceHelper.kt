package com.pmdb.android.data.resource

import android.content.Context
import com.pmdb.android.utils.enums.MovieGenre
import com.pmdb.android.utils.enums.TvGenre
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppResourceHelper @Inject constructor(@ApplicationContext private val context: Context) :
  ResourceHelper {

  override fun getString(resourceId: Int): String = context.getString(resourceId)

  override fun getArrayString(resourceId: Int): Array<out String> =
    context.resources.getStringArray(resourceId)

  override fun getMovieGenreList(): List<String> = MovieGenre.list()

  override fun getTvSeriesGenreList(): List<String> = TvGenre.list()

}