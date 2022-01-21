package com.pmdb.android.ui.main.home

import com.pmdb.android.ui.base.BaseNavigator

interface HomeNavigator : BaseNavigator {

  /**
   * بارگزاری مجدد صفحه
   */
  fun refreshPage()
}