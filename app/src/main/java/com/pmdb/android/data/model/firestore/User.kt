package com.pmdb.android.data.model.firestore

data class User(
  val uid:String,
  val name:String?,
  val email:String?,
  val photo_url:String?,
)
