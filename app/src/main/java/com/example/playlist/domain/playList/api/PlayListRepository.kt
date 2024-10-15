package com.example.playlist.domain.playList.api

import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {
    suspend fun insertPlayList(playList: PlayList)

    suspend fun getListPlayList(): Flow<MutableList<PlayList>>

    suspend fun insertTrackToPlaylist(track: Track): Flow<Long>
}