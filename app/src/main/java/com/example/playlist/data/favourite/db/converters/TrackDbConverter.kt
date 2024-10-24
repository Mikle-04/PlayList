package com.example.playlist.data.favourite.db.converters

import com.example.playlist.data.favourite.db.FavouriteEntity
import com.example.playlist.domain.search.models.Track

class TrackDbConverter {

    fun map(track: Track): FavouriteEntity {
        return FavouriteEntity(
            track.id,
            track.playlistId,
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.previewUrl,
            track.country,
            true

        )
    }

    fun map(track: FavouriteEntity): Track {
        return Track(
            track.id,
            track.playlistId,
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.previewUrl,
            track.country,
            track.isFavourite
        )
    }


}