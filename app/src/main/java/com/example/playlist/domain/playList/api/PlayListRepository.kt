package com.example.playlist.domain.playList.api

import com.example.playlist.domain.playList.models.PlayList

interface PlayListRepository {
    suspend fun insertPlayList(playList: PlayList)
}