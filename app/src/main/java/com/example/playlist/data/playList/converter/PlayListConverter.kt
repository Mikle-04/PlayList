package com.example.playlist.data.playList.converter

import androidx.core.net.toUri
import com.example.playlist.data.playList.db.PlayListEntity
import com.example.playlist.domain.playList.models.PlayList
import com.google.gson.Gson

class PlayListConverter (private val gson: Gson) {

    fun mapPlayListToEntity(playlist: PlayList): PlayListEntity = PlayListEntity(
        id = playlist.id,
        namePlaylist = playlist.namePlaylist,
        descriptionPlaylist = playlist.descriptionPlaylist,
        uriImageStorage = playlist.imgStorage.toString(),
        listTrackIds = gson.toJson(playlist.listTrackIds),
        amountTracks = playlist.listTrackIds.size,

    )

    fun mapEntityToPlayList(playlistEntity: PlayListEntity): PlayList = PlayList(
        id = playlistEntity.id,
        namePlaylist = playlistEntity.namePlaylist,
        descriptionPlaylist = playlistEntity.descriptionPlaylist,
        imgStorage = playlistEntity.uriImageStorage?.toUri(),
        listTrackIds = gson.fromJson(playlistEntity.listTrackIds, Array<Int>::class.java).toMutableList(),
        amountTracks = playlistEntity.amountTracks,

    )
}