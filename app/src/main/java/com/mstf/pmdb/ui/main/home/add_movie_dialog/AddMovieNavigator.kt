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

  /**
   * نمایش لیست فیلم های پیدا شده متناسب با جستجوی کاربر
   */
  fun showMatchedMovieList()

  /**
   * عدم نمایش لیست فیلم های پیدا شده متناسب با جستجوی کاربر
   */
  fun hideMatchedMovieList()

  /**
   * نمایش لایه ی جستجوی فیلم
   */
  fun showSearchLayout()

  /**
   * نمایش لودینگ مربوط به آیتم موردنظر در لیست
   *
   * @param itemPosition شماره ردیف آیتم موردنظر
   */
  fun showItemLoadingAtPosition(itemPosition: Int)

  /**
   * حذف لودینگ مربوط به آیتم موردنظر در لیست
   *
   * @param itemPosition شماره ردیف آیتم موردنظر
   */
  fun hideItemLoadingAtPosition(itemPosition: Int)

  /**
   * نمایش لایه ی فرم مربوط به وارد کردن اطلاعات فیلم
   */
  fun showFormLayout()

}