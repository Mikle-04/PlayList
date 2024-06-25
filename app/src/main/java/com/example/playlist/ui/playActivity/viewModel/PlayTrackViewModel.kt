package com.example.playlist.ui.playActivity.viewModel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist.domain.player.PlayerReposiroty
import com.example.playlist.ui.playActivity.models.PlayerState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
class PlayTrackViewModel(private val handler: Handler, private val url: String) : ViewModel(), KoinComponent {



    private val reposiroty: PlayerReposiroty by inject()

    private var stateRunable = Runnable {getStateRunable()}
    private val stateLiveData = MutableLiveData<PlayerState>(PlayerState.Default)


    private var timeRunable = Runnable { getTimeRunable() }
    private val timeLivedata = MutableLiveData<String>("00:00")


    init {
        handler.post(stateRunable)
        preparePlayer(url)
    }

    fun observeState(): LiveData<PlayerState> = stateLiveData

    fun observeTime(): LiveData<String> = timeLivedata


    fun startPlayer() {
        reposiroty.startPlayer()
        handler.post(timeRunable)

    }
    fun preparePlayer(url: String?) {
        if (url != null) {
            reposiroty.preparePlayer(url)
            handler.removeCallbacks(timeRunable)
        }
    }
    fun pausePlayer() {
       reposiroty.pausePlayer()


    }

    fun playbackControl() {
        when (stateLiveData.value) {
            PlayerState.Play -> {
                pausePlayer()

            }

            PlayerState.Prepare -> {
                handler.removeCallbacks(timeRunable)
                startPlayer()


            }
            PlayerState.Pause -> {
                startPlayer()


            }

            else -> {}
        }
    }

    private fun getStateRunable() {
        stateLiveData.postValue(reposiroty.getPlayerState())
        handler.postDelayed(stateRunable, 300)
    }

    private fun getTimeRunable() {
        timeLivedata.postValue(reposiroty.getCurrentTime())
        handler.postDelayed(timeRunable, 300)
    }

    override fun onCleared() {
        reposiroty.releasePlayer()
        handler.removeCallbacks(timeRunable)
        handler.removeCallbacks(stateRunable)

    }



}