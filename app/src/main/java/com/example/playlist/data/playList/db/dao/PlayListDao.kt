package com.example.playlist.data.playList.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlist.data.playList.db.PlayListEntity
@Dao
interface PlayListDao {
    @Insert(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlayListEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): MutableList<PlayListEntity>

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Int): PlayListEntity

    @Update(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE) //Repo
    suspend fun updatePlaylist(playlistEntity: PlayListEntity)
}