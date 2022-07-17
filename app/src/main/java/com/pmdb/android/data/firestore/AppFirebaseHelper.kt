package com.pmdb.android.data.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.pmdb.android.data.model.firestore.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AppFirebaseHelper @Inject constructor(
  private val firebaseAuth: FirebaseAuth,
  private val firebaseFirestore: FirebaseFirestore,
) : FirebaseHelper {

  override val firebaseUser: FirebaseUser?
    get() = firebaseAuth.currentUser

  override suspend fun insertUser(
    id: String,
    displayName: String?,
    email: String?,
    photoUrl: String?
  ) {
    // ساخت کاربر جدید در دیتابیس براساس ID دریافت شده از لاگین
    val newUser = User(id, displayName, email, photoUrl)
    getUserDocument(id).set(newUser).await()
  }

  /**
   * دریافت دیتاهای مربوط به کاربر لاگین کرده
   */
  override suspend fun getUserData(id: String): User? =
    getUserDocument(id).get().await().toObject(User::class.java)

  override suspend fun addMovie(userId: String, movie: User.Movie) {
    getUserDocument(userId).update("movies", FieldValue.arrayUnion(movie)).await()
  }

  /**
   * دریافت داکیومنت مربوط به کاربر لاگین کرده
   */
  private fun getUserDocument(uid: String): DocumentReference =
    firebaseFirestore.collection("users").document(uid)
}