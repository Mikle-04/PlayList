package com.example.playlist.data.favourite.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlist.data.favourite.db.dao.TrackDao

@Database(version = 2, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}