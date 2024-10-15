package com.example.playlist.data.selectedTrack.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist.data.selectedTrack.db.SelectedTrackEntity

@Dao
interface SelectedTrackDao {
    @Insert(
        entity = SelectedTrackEntity::class,
        onConflict = OnConflictStrategy.IGNORE
    )

    @Query("SELECT trackId FROM select_track_table WHERE playlistId LIKE :playlistId")
    suspend fun getListTrackIdOfPlaylistByPlaylistId(playlistId: Int): MutableList<Int>

    suspend fun insertTrackToPlaylist(selectedTrackEntity: SelectedTrackEntity): Long

//    @Query("SELECT trackTime FROM select_track_table WHERE playlistId LIKE :playlistId")  //Repo
//    suspend fun getListTrackTimeOfPlaylistById(playlistId: Int): MutableList<Int>
}