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

  // Bundle Keys
  const val BUNDLE_KEY_DISPLAY_TYPE = "bundle_key_display_type"
  const val BUNDLE_KEY_FILTER_TYPE = "bundle_key_filter_type"
  const val BUNDLE_KEY_MOVIE_TITLE = "bundle_key_movie_title"
  const val BUNDLE_KEY_FROM_YEAR = "bundle_key_from_year"
  const val BUNDLE_KEY_TO_YEAR = "bundle_key_to_year"
  const val BUNDLE_KEY_DIRECTOR = "bundle_key_director"
}