package com.pmdb.android

import android.app.Application
import com.pmdb.android.utils.AppLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

  override fun onCreate() {
    super.onCreate()
    AppLogger.init()
  }
}