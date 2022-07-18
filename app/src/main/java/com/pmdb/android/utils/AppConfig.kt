package com.pmdb.android.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Source
import com.pmdb.android.data.DataManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppConfig @Inject constructor(
  private val dataManager: DataManager,
) {
  private val _userId = MutableLiveData("")
  val userId: LiveData<String> = _userId

  private val _isUserLoggedIn = MutableLiveData(false)
  val isUserLoggedIn: LiveData<Boolean> = _isUserLoggedIn

  private val _isFirebaseReachable = MutableLiveData(false)
  val isFirebaseReachable: LiveData<Boolean> = _isFirebaseReachable

  suspend fun refresh() {
    dataManager.firebaseUser?.uid?.let {
      _isUserLoggedIn.postValue(true)
      _userId.postValue(it)

      try {
        dataManager.getUserData(it, Source.SERVER)
        _isFirebaseReachable.postValue(true)
      } catch (e: FirebaseFirestoreException) {
        _isFirebaseReachable.postValue(false)
      }
    }
  }
}