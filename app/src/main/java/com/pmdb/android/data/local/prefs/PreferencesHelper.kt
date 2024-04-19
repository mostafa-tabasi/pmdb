package com.pmdb.android.data.local.prefs

interface PreferencesHelper {

  // نمایش لیست فیلم های اخیرا اضافه شده فعال است یا خیر
  var isRecentMoviesEnable: Boolean

  // نمایش لیست سریال های اخیرا اضافه شده فعال است یا خیر
  var isRecentSeriesEnable: Boolean

  // نمایش لیست فیلم های با امتیاز بالا فعال است یا خیر
  var isTopRatedEnable: Boolean

  // متود مرتب سازی فیلم های با امتیاز بالا
  var topRatedMethod: String

  // نمایش لیست فیلم های اخیرا دیده شده فعال است یا خیر
  var isRecentlyWatchedEnable: Boolean

  // بصورت پیش فرض امتیاز فیلم ها از کدام سایت نمایش داده شود
  var archiveDefaultItemRate: String

  // حالت پیش فرض نمایش آیتم های موجود در آرشیو
  var archiveDefaultItemViewType: String

  // آیا تم از  حالت پیش فرض دیوایس پیروی میکند
  var isSystemDefaultThemeEnable: Boolean

  // تم انتخاب شده ی اپلیکیشن
  var appTheme: String
}