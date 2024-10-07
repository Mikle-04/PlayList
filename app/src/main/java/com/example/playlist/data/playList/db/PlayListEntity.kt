package com.example.playlist.data.playList.db

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "playlist_table")
data class PlayListEntity(
    @PrimaryKey
    val id: Int,
    val namePlaylist: String,
    val descriptionPlaylist: String?,
    val uriImageStorage: String?,
    var listTrackIds: String,
    var amountTracks: Int,
    var totalPlaylistTime: Int,
    var trackSpelling: String,
    var minutesSpelling: String,
) {
}