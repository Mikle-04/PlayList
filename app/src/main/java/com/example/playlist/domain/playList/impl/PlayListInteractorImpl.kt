package com.example.playlist.domain.playList.impl

import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.playList.api.PlayListInteractor
import com.example.playlist.domain.playList.api.PlayListRepository

class PlayListInteractorImpl(private val playListRepository: PlayListRepository): PlayListInteractor{
    override suspend fun insertPlaylist(playlist: PlayList) {
        playListRepository.insertPlayList(playlist)
    }
}