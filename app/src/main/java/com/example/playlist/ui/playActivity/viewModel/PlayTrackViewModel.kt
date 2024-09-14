package com.example.playlist.ui.playActivity.viewModel

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist.data.favourite.db.TrackEntity
import com.example.playlist.data.favourite.db.converters.TrackDbConverter
import com.example.playlist.domain.favorite.db.api.FavouriteInteractor
import com.example.playlist.domain.favorite.db.api.FavouriteRepository
import com.example.playlist.domain.player.api.PlayerRepository
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.playActivity.models.FavouriteState
import com.example.playlist.ui.playActivity.models.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.text.SimpleDateFormat
import java.util.Locale

class PlayTrackViewModel(
    private val url: String,
    private val isFavourite: Boolean,
    private val medaPlayer: MediaPlayer,
    private val favouriteInteractor: FavouriteInteractor,
    private val trackDbConverter: TrackDbConverter,
) : ViewModel(), KoinComponent {


    private var timerJob: Job? = null

    private val stateLiveData = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val stateLiveDataFavourite = MutableLiveData<Boolean>(isFavourite)
    fun observeStateFavourite(): LiveData<Boolean> = stateLiveDataFavourite

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


    fun onFavouriteClicked(track: Track?){
        if (track != null) {
            if (track.isFavourite){
                track.isFavourite = false
                favouriteInteractor.deleteFavoriteTrack(track)
                stateLiveDataFavourite.postValue(track.isFavourite)
            }
            else{
                track.isFavourite = true
                favouriteInteractor.insertFavoriteTrack(track)
                stateLiveDataFavourite.postValue(track.isFavourite)
            }
        }
    }
}

