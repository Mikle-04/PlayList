package com.example.playlist.data.selectedTrack.convertor

import com.example.playlist.data.selectedTrack.db.SelectedTrackEntity
import com.example.playlist.domain.search.models.Track

class SelectTrackDbConverter {
    fun mapModelToEntity(track: Track): SelectedTrackEntity = SelectedTrackEntity(
        track.trackId,
        track.playlistId,
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

    fun mapEntityToModel(selectedTrackEntity: SelectedTrackEntity): Track = Track(
        selectedTrackEntity.trackId,
        selectedTrackEntity.playlistId,
        selectedTrackEntity.trackName,
        selectedTrackEntity.artistName,
        selectedTrackEntity.trackTime,
        selectedTrackEntity.artworkUrl100,
        selectedTrackEntity.collectionName,
        selectedTrackEntity.releaseDate,
        selectedTrackEntity.primaryGenreName,
        selectedTrackEntity.previewUrl,
        selectedTrackEntity.country,
        selectedTrackEntity.isFavorite
    )
}