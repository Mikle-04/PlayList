package com.example.playlist.domain.playList.api

import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.playList.models.SelectTrack
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {
    suspend fun insertPlayList(playList: PlayList)

    suspend fun insertTrackToPlaylist(selectedTrack: SelectTrack): Flow<Long>

    suspend fun getPlaylist(): Flow<MutableList<PlayList>>

}