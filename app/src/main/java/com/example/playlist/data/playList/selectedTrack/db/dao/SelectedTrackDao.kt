package com.example.playlist.data.playList.selectedTrack.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist.data.playList.selectedTrack.db.SelectedTrackEntity

@Dao
interface SelectedTrackDao {

    @Insert(entity = SelectedTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackToPlaylist(selectedTrackEntity: SelectedTrackEntity): Long

    @Query("SELECT trackId FROM select_track_table WHERE playlistId LIKE :playlistId")  //Repo
    suspend fun getTrackIdOfPlaylistByPlaylistId(playlistId: Int): MutableList<Int>

}