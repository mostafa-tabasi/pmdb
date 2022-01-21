package com.pmdb.android.ui.main.settings.about_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pmdb.android.BuildConfig
import com.pmdb.android.data.DataManager
import com.pmdb.android.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<AboutNavigator>(dataManager) {

  private val _version = MutableLiveData(BuildConfig.VERSION_NAME)
  val version: LiveData<String> = _version

}