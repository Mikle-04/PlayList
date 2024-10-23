package com.example.playlist.data.search.dto

import com.google.gson.annotations.SerializedName

data class TrackDto(
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    @SerializedName("trackTimeMillis")val trackTime: Long,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val previewUrl:String?,
    val country: String?

)