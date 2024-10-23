package com.example.playlist.domain.playList.api

import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.playList.models.SelectTrack
import com.example.playlist.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {
    suspend fun insertPlayList(playList: PlayList)

    suspend fun insertTrackToPlaylist(track: Track): Flow<Long>

    suspend fun getListPlaylist(): Flow<MutableList<PlayList>>

    suspend fun getPlaylist(playlistId: Int): Flow<PlayList>
    suspend fun getTracksByPlaylistId(playlistId: Int): Flow<MutableList<Track>>
    suspend fun deleteSelectedTrackFromPlaylist(track: Track)

    suspend fun deletePlaylistById(playlistId: Int) : Flow<Int>

}