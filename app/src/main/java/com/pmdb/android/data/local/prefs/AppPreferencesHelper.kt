package com.pmdb.android.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import com.pmdb.android.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppPreferencesHelper @Inject constructor(
  private val mPrefs: SharedPreferences,
  @ApplicationContext private val context: Context,
) : PreferencesHelper {

  override var isRecentMoviesEnable: Boolean
    get() = mPrefs.getBoolean(context.getString(R.string.key_recently_added_movies), true)
    set(value) {
      mPrefs.edit().putBoolean(context.getString(R.string.key_recently_added_movies), value).apply()
    }

  override var isRecentSeriesEnable: Boolean
    get() = mPrefs.getBoolean(context.getString(R.string.key_recently_added_series), true)
    set(value) {
      mPrefs.edit().putBoolean(context.getString(R.string.key_recently_added_series), value).apply()
    }

  override var isTopRatedEnable: Boolean
    get() = mPrefs.getBoolean(context.getString(R.string.key_top_rated), true)
    set(value) {
      mPrefs.edit().putBoolean(context.getString(R.string.key_top_rated), value).apply()
    }

  override var topRatedMethod: String
    get() = mPrefs.getString(
      context.getString(R.string.key_top_rated_method),
      context.resources.getStringArray(R.array.top_rated_values)[0]
    )!!
    set(value) {
      mPrefs.edit().putString(context.getString(R.string.key_top_rated_method), value).apply()
    }

  override var isRecentlyWatchedEnable: Boolean
    get() = mPrefs.getBoolean(context.getString(R.string.key_recently_watched), true)
    set(value) {
      mPrefs.edit().putBoolean(context.getString(R.string.key_recently_watched), value).apply()
    }

  override var archiveDefaultItemRate: String
    get() = mPrefs.getString(
      context.getString(R.string.key_archive_default_item_rate),
      context.resources.getStringArray(R.array.default_rate_values)[0]
    )!!
    set(value) {
      mPrefs.edit().putString(context.getString(R.string.key_archive_default_item_rate), value)
        .apply()
    }

  override var archiveDefaultItemViewType: String
    get() = mPrefs.getString(
      context.getString(R.string.key_display_type),
      context.resources.getStringArray(R.array.display_values)[0]
    )!!
    set(value) {
      mPrefs.edit().putString(context.getString(R.string.key_display_type), value)
        .apply()
    }

  override var isSystemDefaultThemeEnable: Boolean
    get() = mPrefs.getBoolean(context.getString(R.string.key_theme_system_default), true)
    set(value) {
      mPrefs.edit().putBoolean(context.getString(R.string.key_theme_system_default), value).apply()
    }

  override var appTheme: String
    get() = mPrefs.getString(
      context.getString(R.string.key_theme_method),
      context.resources.getStringArray(R.array.theme_values)[0]
    )!!
    set(value) {
      mPrefs.edit().putString(context.getString(R.string.key_theme_method), value).apply()
    }
}