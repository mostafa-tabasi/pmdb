package com.mstf.pmdb.utils

object AppConstants {

  const val DB_NAME = "pmdb_db.db"
  const val NULL_INDEX = -1L
  const val PREF_NAME = "pmdb_pref"
  const val STATUS_CODE_FAILED = "failed"
  const val STATUS_CODE_SUCCESS = "success"
  const val TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss"

  // Media Type Constants
  const val MEDIA_TYPE_TITLE_MOVIE = "movie"
  const val MEDIA_TYPE_TITLE_SERIES = "series"
  const val MEDIA_TYPE_TITLE_EPISODE = "episode"

  // Rating Source Constants
  const val RATING_SOURCE_IMDB = "Internet Movie Database"
  const val RATING_SOURCE_ROTTEN_TOMATOES = "Rotten Tomatoes"
  const val RATING_SOURCE_METACRITIC = "Metacritic"

  // OMDb Api Constants
  const val API_PARAM_KEY_OMDB = "apikey"
  const val API_PARAM_TITLE = "t"
  const val API_PARAM_SEARCH = "s"
  const val API_PARAM_YEAR = "y"
  const val API_PARAM_IMDB_ID = "i"
  const val API_PARAM_TYPE = "type"
}