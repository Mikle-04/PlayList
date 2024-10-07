package com.example.playlist.domain.search.models

import com.google.gson.annotations.SerializedName

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis")val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val previewUrl:String,
    val country: String,
    var isFavourite:Boolean = false
){
    override fun equals(other: Any?): Boolean {
        return if (other is Track){
            (trackId == other.trackId)
        }else{
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = trackId
        result = 31 * result + trackName.hashCode()
        result = 31 * result + artistName.hashCode()
        result = 31 * result + trackTime.hashCode()
        result = 31 * result + artworkUrl100.hashCode()
        result = 31 * result + collectionName.hashCode()
        result = 31 * result + releaseDate.hashCode()
        result = 31 * result + primaryGenreName.hashCode()
        result = 31 * result + previewUrl.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + isFavourite.hashCode()
        return result
    }
}

