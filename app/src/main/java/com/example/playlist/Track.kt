package com.example.playlist

import com.google.gson.annotations.SerializedName

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis")val trackTime: Long,
    val artworkUrl100: String
)