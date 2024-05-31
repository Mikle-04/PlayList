package com.example.playlist.ui.playActivity.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist.creator.Creator
import com.example.playlist.ui.playActivity.models.PlayerState

class PlayTrackViewModel() : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private val playInteractor = Creator.providePlayerInteractor()

    private var stateRunable = Runnable {getStateRunable()}
    private val stateLiveData = MutableLiveData<PlayerState>(PlayerState.Default)


    private var timeRunable = Runnable { getTimeRunable() }
    private val timeLivedata = MutableLiveData<String>("00:00")


    init {
        handler.post(stateRunable)
    }

    fun observeState(): LiveData<PlayerState> = stateLiveData

    fun observeTime(): LiveData<String> = timeLivedata
    fun preparePlayer(url: String?) {
        if (url != null) {
            playInteractor.preparePlayer(url)
            handler.removeCallbacks(timeRunable)
        }
    }

    fun startPlayer() {
        playInteractor.startPlayer()
        handler.post(timeRunable)

    }

    fun pausePlayer() {
        playInteractor.pausePlayer()


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
        stateLiveData.postValue(playInteractor.getPlayerState())
        handler.postDelayed(stateRunable, 300)
    }

    private fun getTimeRunable() {
        timeLivedata.postValue(playInteractor.getCurrentTime())
        handler.postDelayed(timeRunable, 300)
    }

    override fun onCleared() {
        playInteractor.releasePlayer()
        handler.removeCallbacks(timeRunable)
        handler.removeCallbacks(stateRunable)

    }



}