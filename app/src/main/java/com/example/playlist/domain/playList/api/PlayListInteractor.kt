package com.example.playlist.domain.playList.api

import com.example.playlist.domain.playList.models.PlayList

interface PlayListInteractor {
    suspend fun insertPlaylist(playlist: PlayList)
}