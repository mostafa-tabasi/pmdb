package com.pmdb.android.ui.main.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.pmdb.android.data.DataManager
import com.pmdb.android.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(dataManager: DataManager) :
  BaseViewModel<SettingsNavigator>(dataManager) {

  private val _currentUser = MutableLiveData<FirebaseUser?>()
  val currentUser: LiveData<FirebaseUser?> = _currentUser

  fun setFirebaseUser(user: FirebaseUser?) {
    _currentUser.value = user
  }
}