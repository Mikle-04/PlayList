package com.example.playlist.ui.searchActivity.models

import android.os.Parcel
import android.os.Parcelable

data class TrackInfo(
   val trackName: String?,
   val artistName: String?,
   val artworkUrl100: String?,
   val trackTime: Long,
   val collectionName: String?,
   val releaseDate: String?,
   val primaryGenreName: String?,
   val country: String?,
   val previewUrl: String?
) : Parcelable {
   constructor(parcel: Parcel) : this(
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readLong(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString()
   ) {
   }

   override fun writeToParcel(parcel: Parcel, flags: Int) {
      parcel.writeString(trackName)
      parcel.writeString(artistName)
      parcel.writeString(artworkUrl100)
      parcel.writeLong(trackTime)
      parcel.writeString(collectionName)
      parcel.writeString(releaseDate)
      parcel.writeString(primaryGenreName)
      parcel.writeString(country)
      parcel.writeString(previewUrl)
   }

   override fun describeContents(): Int {
      return 0
   }

   companion object CREATOR : Parcelable.Creator<TrackInfo> {
      override fun createFromParcel(parcel: Parcel): TrackInfo {
         return TrackInfo(parcel)
      }

      override fun newArray(size: Int): Array<TrackInfo?> {
         return arrayOfNulls(size)
      }
   }
}