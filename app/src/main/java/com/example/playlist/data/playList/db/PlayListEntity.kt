package com.example.playlist.data.playList.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "playlist_table")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    val namePlaylist: String,
    val descriptionPlaylist: String?,
    val uriImageStorage: String?,
    var listTrackIds: String,
    var amountTracks: Int
) {
}