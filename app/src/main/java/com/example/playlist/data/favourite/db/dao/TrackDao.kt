package com.example.playlist.data.favourite.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist.data.favourite.db.TrackEntity

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    fun deleteTrack (track: TrackEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getTrack(): List<TrackEntity>

    @Query("SELECT trackId FROM track_table")
    fun getTrackId(): List<Int>

}