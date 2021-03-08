package com.mstf.pmdb.ui.main.home.add_movie_dialog

import com.mstf.pmdb.ui.base.BaseNavigator

interface AddMovieNavigator : BaseNavigator {

  /**
   * آماده کردن فیلد جستجو براساس عنوان
   */
  fun prepareSearchTitleField()

  /**
   * آماده کردن فیلد جستجو براساس شناسه ی سایت imdb
   */
  fun prepareSearchIdField()

}