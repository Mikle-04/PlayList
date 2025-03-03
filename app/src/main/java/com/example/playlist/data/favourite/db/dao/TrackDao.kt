package com.example.playlist.data.favourite.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist.data.favourite.db.FavouriteEntity

@Dao
interface TrackDao {
    @Insert(entity = FavouriteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: FavouriteEntity)

    @Delete(entity = FavouriteEntity::class)
    fun deleteTrack (track: FavouriteEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getFavouriteTrack(): List<FavouriteEntity>

    @Query("SELECT * FROM track_table WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Int): FavouriteEntity?

}