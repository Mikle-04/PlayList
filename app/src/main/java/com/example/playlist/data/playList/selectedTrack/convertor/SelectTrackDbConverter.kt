package com.example.playlist.data.playList.selectedTrack.convertor

import com.example.playlist.data.playList.selectedTrack.db.SelectedTrackEntity
import com.example.playlist.domain.playList.models.SelectTrack

class SelectTrackDbConverter {
    fun mapModelToEntity(selectTrack: SelectTrack): SelectedTrackEntity = SelectedTrackEntity(
        selectTrack.id,
        selectTrack.trackId,
        selectTrack.playlistId,
        selectTrack.trackName,
        selectTrack.artistName,
        selectTrack.trackTime,
        selectTrack.artworkUrl100,
        selectTrack.collectionName,
        selectTrack.releaseDate,
        selectTrack.primaryGenreName,
        selectTrack.previewUrl,
        selectTrack.country,
        selectTrack.isFavorite
    )

    fun mapEntityToModel(selectedTrackEntity: SelectedTrackEntity): SelectTrack = SelectTrack(
        selectedTrackEntity.id,
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