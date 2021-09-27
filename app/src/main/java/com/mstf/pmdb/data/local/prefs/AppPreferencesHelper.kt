package com.mstf.pmdb.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.mstf.pmdb.R
import com.mstf.pmdb.di.annotation.PreferenceInfo
import javax.inject.Inject

class AppPreferencesHelper @Inject constructor(
  val context: Context,
  @PreferenceInfo prefFileName: String?
) : PreferencesHelper {

  private val mPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

  override var currentUserId: String?
    get() = mPrefs.getString(context.getString(R.string.key_current_user_id), null)
    set(value) {
      mPrefs.edit().putString(context.getString(R.string.key_current_user_id), value).apply()
    }

  override var accessToken: String?
    get() = mPrefs.getString(context.getString(R.string.key_access_token), null)
    set(value) {
      mPrefs.edit().putString(context.getString(R.string.key_access_token), value).apply()
    }

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
}