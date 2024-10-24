package com.example.playlist.data.playList.selectedTrack.convertor

import com.example.playlist.data.playList.selectedTrack.db.SelectedTrackEntity
import com.example.playlist.domain.playList.models.SelectTrack
import com.example.playlist.domain.search.models.Track

class SelectTrackDbConverter {
    fun mapModelToEntity(track: Track): SelectedTrackEntity = SelectedTrackEntity(
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

    fun mapEntityToModel(selectedTrackEntity: SelectedTrackEntity): Track = Track(
        selectedTrackEntity.id,
        selectedTrackEntity.playlistId,
        selectedTrackEntity.trackId,
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