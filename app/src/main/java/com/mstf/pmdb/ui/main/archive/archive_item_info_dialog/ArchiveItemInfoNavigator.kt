package com.mstf.pmdb.ui.main.archive.archive_item_info_dialog

import com.mstf.pmdb.ui.base.BaseNavigator

interface ArchiveItemInfoNavigator : BaseNavigator {

  /**
   * عدم نمایش لایه ی تاییدیه ی
   *
   * @param animate همراه با انیمیشن انجام شود یا خیر
   */
  fun dismissConfirm(animate: Boolean = true)
}