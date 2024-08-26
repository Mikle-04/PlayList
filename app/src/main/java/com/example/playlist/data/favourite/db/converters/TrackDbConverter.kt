package com.example.playlist.data.favourite.db.converters

import com.example.playlist.data.favourite.db.TrackEntity
import com.example.playlist.data.search.dto.TrackDto
import com.example.playlist.domain.search.models.Track

class TrackDbConverter {

    fun mapTrackToTrackEntity(track: Track): TrackEntity {
        return TrackEntity(
            0L,
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

    fun mapTrackEntityToTrack(track: TrackEntity): Track{
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
            track.country)
    }



}