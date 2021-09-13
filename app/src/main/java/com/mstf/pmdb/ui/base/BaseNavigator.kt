package com.mstf.pmdb.ui.base

interface BaseNavigator {

  fun back()

  fun hideKeyboard()

  fun showError(message: String)
}