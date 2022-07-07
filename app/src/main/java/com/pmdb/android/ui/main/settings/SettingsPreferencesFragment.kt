package com.pmdb.android.ui.main.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pmdb.android.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsPreferencesFragment : PreferenceFragmentCompat(),
  SharedPreferences.OnSharedPreferenceChangeListener {

  companion object {
    const val TAG = "SettingsPreferencesFragment"
  }

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.app_settings, rootKey)
    handleTopRatedSortMethodEnable()
    handleChooseThemeEnable()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    listView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val firstCompletelyVisibleItemPosition =
          (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        onFirstItemVisibilityChange?.invoke(firstCompletelyVisibleItemPosition == 0)
      }
    })
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

  private var onFirstItemVisibilityChange: ((isVisible: Boolean) -> Unit)? = null
  fun setSettingsScrollListener(onFirstItemVisibilityChange: (isVisible: Boolean) -> Unit) {
    this.onFirstItemVisibilityChange = onFirstItemVisibilityChange
  }
}