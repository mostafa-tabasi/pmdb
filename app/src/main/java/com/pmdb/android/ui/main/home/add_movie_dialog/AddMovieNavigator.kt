package com.pmdb.android.ui.main.home.add_movie_dialog

import com.pmdb.android.ui.base.BaseNavigator

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

  /**
   * حذف تمام چیپ های مربوط به ژانر فیلم
   */
  fun removeAllGenreChips()

  /**
   * ست کردن ژانرهای موجود برای فیلم متناسب با نوع آن
   *
   * @param items لیست ژانرهای موجود
   */
  fun setUpMovieGenres(items: List<String>)

  /**
   * عدم نمایش لایه ی تاییدیه ی
   *
   * @param animate همراه با انیمیشن انجام شود یا خیر
   */
  fun dismissConfirm(animate: Boolean = true)

  /**
   * نمایش تاییدیه جهت ذخیره ی فیلم
   */
  fun showOverwriteConfirm()

  /**
   * هدایت کاربر به url موردنظر بوسیله ی browser
   */
  fun openUrl(url: String)

}