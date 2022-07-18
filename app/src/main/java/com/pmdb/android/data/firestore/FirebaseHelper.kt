package com.pmdb.android.data.firestore

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Source
import com.pmdb.android.data.model.firestore.User

interface FirebaseHelper {
  val firebaseUser: FirebaseUser?

  suspend fun insertUser(id: String, displayName: String?, email: String?, photoUrl: String?)

  suspend fun getUserData(id: String, source: Source = Source.DEFAULT): User?

  suspend fun addMovie(userId: String, movie: User.Movie)
}
