package com.mstf.pmdb.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mstf.pmdb.data.local.db.dao.SampleDao
import com.mstf.pmdb.data.model.db.SampleEntity

@Database(entities = [SampleEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

  abstract fun sampleDao(): SampleDao
}