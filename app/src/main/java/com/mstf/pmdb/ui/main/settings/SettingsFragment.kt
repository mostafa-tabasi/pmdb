package com.mstf.pmdb.ui.main.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.mstf.pmdb.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(),
  SharedPreferences.OnSharedPreferenceChangeListener {

  companion object {
    const val TAG = "SettingsFragment"
    fun newInstance(): SettingsFragment {
      val args = Bundle()
      val fragment = SettingsFragment()
      fragment.arguments = args
      return fragment
    }
  }

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.app_settings, rootKey)
    handleTopRatedSortMethodEnable()
  }

  override fun onResume() {
    super.onResume()
    preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
  }

  override fun onPause() {
    preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    super.onPause()
  }

  override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
    when (key) {
      getString(R.string.key_top_rated) -> handleTopRatedSortMethodEnable()
      else -> {
      }
    }
  }

  private fun handleTopRatedSortMethodEnable() {
    val topRatedMethodPref =
      findPreference<ListPreference>(getString(R.string.key_top_rated_method))
    val topRatedPref = findPreference<SwitchPreferenceCompat>(getString(R.string.key_top_rated))
    topRatedMethodPref?.isEnabled = topRatedPref?.isChecked ?: false
  }
}