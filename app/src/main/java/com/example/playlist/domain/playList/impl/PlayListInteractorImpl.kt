package com.example.playlist.domain.playList.impl

import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.playList.api.PlayListInteractor
import com.example.playlist.domain.playList.api.PlayListRepository
import com.example.playlist.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(private val playListRepository: PlayListRepository): PlayListInteractor{
    override suspend fun insertPlaylist(playlist: PlayList) {
        playListRepository.insertPlayList(playlist)
    }

    override suspend fun getListPlaylist(): Flow<MutableList<PlayList>> {
        return playListRepository.getListPlayList()
    }

    override suspend fun insertTrackToPlaylist(track: Track): Flow<Long> {
        return playListRepository.insertTrackToPlaylist(track)
    }

}