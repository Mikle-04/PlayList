package com.example.playlist.data.player.impl

import android.media.MediaPlayer
import android.util.Log
import com.example.playlist.domain.player.PlayerReposiroty
import com.example.playlist.ui.playActivity.models.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl() : PlayerReposiroty {
    private var statePlayer: PlayerState = PlayerState.Default
    private val mediaPlayer: MediaPlayer = MediaPlayer()


    override fun startPlayer() {
        mediaPlayer.start()
        statePlayer = PlayerState.Play
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        statePlayer = PlayerState.Pause
    }

    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener{
            statePlayer = PlayerState.Prepare
        }
        mediaPlayer.setOnCompletionListener {
            statePlayer = PlayerState.Prepare
        }

    }


    override fun releasePlayer() {
        mediaPlayer.release()
        statePlayer = PlayerState.Default

    }

    override fun getPlayerState(): PlayerState {
        return statePlayer
    }

    override fun getCurrentTime(): String {
        val timeFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        return timeFormat.format(mediaPlayer.currentPosition + 500L)
    }
}