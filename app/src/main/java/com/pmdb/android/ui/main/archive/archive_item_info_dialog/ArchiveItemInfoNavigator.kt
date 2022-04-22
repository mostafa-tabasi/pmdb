package com.pmdb.android.ui.main.archive.archive_item_info_dialog

import com.pmdb.android.ui.base.BaseNavigator

interface ArchiveItemInfoNavigator : BaseNavigator {

  /**
   * عدم نمایش لایه ی تاییدیه ی
   *
   * @param animate همراه با انیمیشن انجام شود یا خیر
   */
  fun dismissConfirm(animate: Boolean = true)

  /**
   * هدایت کاربر به url موردنظر بوسیله ی browser
   */
  fun openUrl(url: String)
}