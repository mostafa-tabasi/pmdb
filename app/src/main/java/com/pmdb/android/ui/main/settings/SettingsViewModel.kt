package com.pmdb.android.ui.main.settings

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pmdb.android.data.DataManager
import com.pmdb.android.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  dataManager: DataManager,
) : BaseViewModel<SettingsNavigator>(dataManager) {

  private val _currentUser = MutableLiveData(dataManager.firebaseUser)
  val currentUser: LiveData<FirebaseUser?> = _currentUser

  private val _signInLoading = MutableLiveData(false)
  val signInLoading: LiveData<Boolean> = _signInLoading

  fun setFirebaseUser(user: FirebaseUser?) {
    _currentUser.postValue(user)
  }

  fun onGoogleSignInResult(result: FirebaseAuthUIAuthenticationResult?) {
    val response = result?.idpResponse ?: return
    val user = FirebaseAuth.getInstance().currentUser
    if (result.resultCode == Activity.RESULT_OK && user != null) {
      viewModelScope.launch(Dispatchers.IO) {
        _signInLoading.postValue(true)
        if (response.isNewUser)
          dataManager.insertUser(user.uid, user.displayName, user.email, user.photoUrl.toString())
        setFirebaseUser(user)
        syncArchive(user.uid)
        _signInLoading.postValue(false)
      }
    }
  }

  /**
   * سینک کردن آرشیو موجود در حافظه ی داخلی  گوشی با آرشیو موجود در حساب گوگل کاربر
   */
  private suspend fun syncArchive(userId: String) {
    val localArchive = dataManager.getAllMovies() ?: return
    if (localArchive.isEmpty()) return
    localArchive.forEach { dataManager.addMovie(userId, it.toFirestoreEntity()) }
  }
}