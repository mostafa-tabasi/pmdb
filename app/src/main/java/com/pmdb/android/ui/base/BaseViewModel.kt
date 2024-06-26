package com.pmdb.android.ui.base

import androidx.lifecycle.ViewModel
import com.pmdb.android.data.DataManager
import java.lang.ref.WeakReference

abstract class BaseViewModel<N : BaseNavigator>(val dataManager: DataManager) : ViewModel() {

  private var mNavigator: WeakReference<N>? = null

  val navigator: N? get() = mNavigator!!.get()

  fun setNavigator(navigator: N) {
    mNavigator = WeakReference(navigator)
  }
}