package com.mstf.pmdb.ui.main.settings.about_dialog

import androidx.databinding.ObservableField
import com.mstf.pmdb.BuildConfig
import com.mstf.pmdb.data.DataManager
import com.mstf.pmdb.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<AboutNavigator>(dataManager) {

  val version = ObservableField(BuildConfig.VERSION_NAME)

}