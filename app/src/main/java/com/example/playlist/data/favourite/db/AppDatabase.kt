package com.example.playlist.data.favourite.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlist.data.favourite.db.dao.TrackDao
import com.example.playlist.data.playList.db.PlayListEntity
import com.example.playlist.data.playList.db.dao.PlayListDao

@Database(version = 4, entities = [TrackEntity::class, PlayListEntity::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playListDao(): PlayListDao
}