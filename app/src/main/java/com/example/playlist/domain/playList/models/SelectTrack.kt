package com.example.playlist.domain.playList.models

import com.google.gson.annotations.SerializedName

data class SelectTrack (
    val id: Int,
    val trackId: Int,
    val playlistId: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val previewUrl:String,
    val country: String,
    var isFavorite: Boolean = true
)