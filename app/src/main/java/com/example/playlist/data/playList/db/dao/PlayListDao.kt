package com.example.playlist.data.playList.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlist.data.playList.db.PlayListEntity
@Dao
interface PlayListDao {
    @Insert(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlayListEntity)
}