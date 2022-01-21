package com.pmdb.android.ui.base

interface BaseNavigator {

  fun back()

  fun hideKeyboard()

  fun showError(message: String)
}