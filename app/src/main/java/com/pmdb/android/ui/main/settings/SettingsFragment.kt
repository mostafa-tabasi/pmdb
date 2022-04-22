package com.pmdb.android.ui.main.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.pmdb.android.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(),
  SharedPreferences.OnSharedPreferenceChangeListener {

  companion object {
    const val TAG = "SettingsFragment"
  }

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.app_settings, rootKey)
    handleTopRatedSortMethodEnable()
    handleChooseThemeEnable()
  }

  override fun onResume() {
    super.onResume()
    preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
  }

  override fun onPause() {
    preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    super.onPause()
  }

  override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
    when (key) {
      getString(R.string.key_top_rated) -> handleTopRatedSortMethodEnable()
      getString(R.string.key_theme_system_default) -> handleChooseThemeEnable(true)
      getString(R.string.key_theme_method) -> requireActivity().recreate()
      else -> {
      }
    }
  }

  /**
   * هندل کردن وضعیت فعال بودن یا نبودنِ تنظیمات مربوط به متودِ مرتب سازی براساس امتیاز
   * اگر تنظیمات مربوط به نمایش فیلم های با امتیاز برتر در صفحه ی اصلی خاموش بود، لازم نیست تنظیماتِ متودِ مرتب سازی فعال باشد
   */
  private fun handleTopRatedSortMethodEnable() {
    val topRatedMethodPref =
      findPreference<ListPreference>(getString(R.string.key_top_rated_method))
    val topRatedPref = findPreference<SwitchPreferenceCompat>(getString(R.string.key_top_rated))
    topRatedMethodPref?.isEnabled = topRatedPref?.isChecked ?: false
  }

  /**
   * هندل کردن وضعیت فعال یا غیرفعال بودنِ تنظیمات مربوط به انتخاب تم تم اپلیکیشن
   * اگر تنظیمات پیش فرض دیوایس برای تم انتخاب شده بود، لازم نیست تم قابل تغییر باشد
   *
   * @param recreateAct لازم است که اکتیویتی مجدد ساخته شود برای اعمال تم یا خیر
   */
  private fun handleChooseThemeEnable(recreateAct: Boolean = false) {
    val chooseThemePref = findPreference<ListPreference>(getString(R.string.key_theme_method))
    val themeSystemDefaultPref =
      findPreference<SwitchPreferenceCompat>(getString(R.string.key_theme_system_default))
    val isThemeSystemDefaultChecked = themeSystemDefaultPref?.isChecked ?: false
    chooseThemePref?.isEnabled = !isThemeSystemDefaultChecked

    if (recreateAct) requireActivity().recreate()
  }

  /**
   * نمایش دیالوگ اطلاعات اپلیکیشن
   */
  fun openAboutDialog() {
    val action = SettingsFragmentDirections.actionSettingsFragmentToAboutDialog()
    findNavController().navigate(action)
  }
}