package com.example.playlist.data.playList.selectedTrack.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "select_track_table")
data class SelectedTrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val playlistId: Int,
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    @SerializedName("trackTimeMillis")val trackTime: Long,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val previewUrl:String?,
    val country: String?,
    var isFavorite: Boolean
)