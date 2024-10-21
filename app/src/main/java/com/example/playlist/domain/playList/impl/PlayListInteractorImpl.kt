package com.example.playlist.domain.playList.impl

import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.playList.api.PlayListInteractor
import com.example.playlist.domain.playList.api.PlayListRepository
import com.example.playlist.domain.playList.models.SelectTrack
import com.example.playlist.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(private val playListRepository: PlayListRepository): PlayListInteractor{
    override suspend fun insertPlaylist(playlist: PlayList) {
        playListRepository.insertPlayList(playlist)
    }

    override suspend fun insertTrackToPlaylist(selectTrack: SelectTrack): Flow<Long> {
        return playListRepository.insertTrackToPlaylist(selectTrack)
    }
    override suspend fun getPlaylist(): Flow<MutableList<PlayList>> {
        return playListRepository.getPlaylist()
    }

}