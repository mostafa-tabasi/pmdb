package com.pmdb.android.data.resource

interface ResourceHelper {

  fun getString(resourceId: Int): String

  fun getArrayString(resourceId: Int): Array<out String>

  /**
   * لیست ژانرهای موجود مربوط به فیلم های سینمایی
   */
  fun getMovieGenreList(): List<String>

  /**
   * لیست ژانرهای موجود مربوط به سریال های تلویزیونی
   */
  fun getTvSeriesGenreList(): List<String>

}