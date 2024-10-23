package com.example.playlist.domain.playList.api

import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.playList.models.SelectTrack
import com.example.playlist.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {
    suspend fun insertPlaylist(playlist: PlayList)
    suspend fun insertTrackToPlaylist(selectTrack: SelectTrack): Flow<Long>
    suspend fun getPlaylist(): Flow<MutableList<PlayList>>

}