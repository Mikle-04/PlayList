package com.example.playlist.domain.search.models

import android.os.Parcel
import com.google.gson.annotations.SerializedName
import android.os.Parcelable

data class Track(
    var id: Int,
    var playlistId: Int,
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val previewUrl: String?,
    val country: String?,
    var isFavourite: Boolean = false
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        return if (other is Track) {
            (trackId == other.trackId)
        } else {
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

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readBoolean()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(playlistId)
        parcel.writeInt(trackId)
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeLong(trackTime ?: 0L)
        parcel.writeString(artworkUrl100)
        parcel.writeString(collectionName)
        parcel.writeString(releaseDate)
        parcel.writeString(primaryGenreName)
        parcel.writeString(previewUrl)
        parcel.writeString(country)
        parcel.writeBoolean(isFavourite)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}

