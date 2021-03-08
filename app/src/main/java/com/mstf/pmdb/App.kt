package com.mstf.pmdb

import android.app.Application
import com.mstf.pmdb.utils.AppLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

  override fun onCreate() {
    super.onCreate()
    AppLogger.init()
  }
}