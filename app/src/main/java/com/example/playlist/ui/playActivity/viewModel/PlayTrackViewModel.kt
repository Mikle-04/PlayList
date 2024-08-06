package com.example.playlist.ui.playActivity.viewModel

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist.ui.playActivity.models.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.text.SimpleDateFormat
import java.util.Locale

class PlayTrackViewModel(
    private val url: String,
    private val medaPlayer: MediaPlayer
) : ViewModel(), KoinComponent {


    private var timerJob: Job? = null

    private val stateLiveData = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observeState(): LiveData<PlayerState> = stateLiveData

    init {
        preparePlayer(url)
    }


    fun preparePlayer(url: String?) {
        if (url != null) {
            medaPlayer.setDataSource(url)
            medaPlayer.prepareAsync()
            medaPlayer.setOnPreparedListener {
                stateLiveData.postValue(PlayerState.Prepared())
            }
            medaPlayer.setOnCompletionListener {
                medaPlayer.seekTo(0)
                stateLiveData.postValue(PlayerState.Prepared())
                timerJob?.cancel()
            }
        }
    }

    fun startPlayer() {
        medaPlayer.start()
        stateLiveData.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    fun pausePlayer() {
        medaPlayer.pause()
        timerJob?.cancel()
        stateLiveData.postValue(PlayerState.Paused(getCurrentPlayerPosition()))

    }

    fun playbackControl() {
        when (stateLiveData.value) {
            is PlayerState.Playing -> {
                pausePlayer()
            }

            is PlayerState.Prepared, is PlayerState.Paused -> {
                startPlayer()
            }

            else -> {}

        }
    }


    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (medaPlayer.isPlaying) {
                delay(300L)
                stateLiveData.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
        }
    }

    private fun releasePlayer() {
        medaPlayer.stop()
        medaPlayer.release()
        stateLiveData.value = PlayerState.Default()
    }

    override fun onCleared() {
        releasePlayer()
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(medaPlayer.currentPosition)
            ?: "00:00"
    }
}

