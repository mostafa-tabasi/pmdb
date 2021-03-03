package com.mstf.pmdb.data.local.db

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDbHelper @Inject constructor(private val appDatabase: AppDatabase) : DbHelper {
}