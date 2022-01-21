package com.pmdb.android.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pmdb.android.data.local.db.dao.MovieDao
import com.pmdb.android.data.model.db.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

  abstract fun movieDao(): MovieDao
}