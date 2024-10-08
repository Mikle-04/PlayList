package com.example.playlist.data.favourite.db.converters

import com.example.playlist.data.favourite.db.TrackEntity
import com.example.playlist.data.search.dto.TrackDto
import com.example.playlist.domain.search.models.Track

class TrackDbConverter {

    fun map(track: Track): TrackEntity {
        return TrackEntity(
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
            System.currentTimeMillis()

        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.previewUrl,
            track.country
        )
    }



}