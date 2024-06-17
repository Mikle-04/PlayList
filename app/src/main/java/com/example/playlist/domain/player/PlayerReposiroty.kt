package com.example.playlist.domain.player

import com.example.playlist.ui.playActivity.models.PlayerState

interface PlayerReposiroty {

    fun startPlayer()

    fun pausePlayer()

    fun preparePlayer(url: String)

    fun releasePlayer()

    fun getPlayerState(): PlayerState

    fun getCurrentTime(): String
}