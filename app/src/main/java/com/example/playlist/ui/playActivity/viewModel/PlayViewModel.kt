package com.example.playlist.ui.playActivity.viewModel

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist.domain.favorite.db.api.FavouriteInteractor
import com.example.playlist.domain.playList.api.PlayListInteractor
import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.playList.models.SelectTrack
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.mediaActivity.playListFragment.state.PlayListState
import com.example.playlist.ui.playActivity.models.PlayerState
import com.example.playlist.ui.playActivity.state.InsertTrackPlayListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.text.SimpleDateFormat
import java.util.Locale

class PlayViewModel(
    private val track: Track,
    private val medaPlayer: MediaPlayer,
    private val favouriteInteractor: FavouriteInteractor,
    private val playListInteractor: PlayListInteractor

) : ViewModel(), KoinComponent {


    private var timerJob: Job? = null

    private val stateLiveData = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val stateLiveDataFavourite = MutableLiveData<Boolean>()
    fun observeStateFavourite(): LiveData<Boolean> = stateLiveDataFavourite

    private var statePlaylist = MutableLiveData<PlayListState>()
    fun getStatePlaylist(): LiveData<PlayListState> = statePlaylist

    private var stateInsertTrack = MutableLiveData<InsertTrackPlayListState>()
    fun getStateInsertTrack(): LiveData<InsertTrackPlayListState> = stateInsertTrack

    init {
        preparePlayer(track.previewUrl)
        checkIsFavouriteTrack(track.trackId)
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


    fun onFavouriteClicked(track: Track?) {
        if (track != null) {
            if (track.isFavourite) {
                track.isFavourite = false
                favouriteInteractor.deleteFavoriteTrack(track)
                stateLiveDataFavourite.postValue(track.isFavourite)
            } else {
                track.isFavourite = true
                favouriteInteractor.insertFavoriteTrack(track)
                stateLiveDataFavourite.postValue(track.isFavourite)
            }
        }
    }

    private fun checkIsFavouriteTrack(trackId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            favouriteInteractor.getFavouriteTrackId(trackId).collect {
                stateLiveDataFavourite.postValue(it)
            }
        }
    }

    fun getPlayListDb() {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.getListPlaylist().collect() { listPlayList ->
                if (listPlayList.isEmpty()) {
                    statePlaylist.postValue(PlayListState.Empty())
                } else {
                    statePlaylist.postValue(PlayListState.Content(listPlayList))
                }
            }
        }
    }

    fun insertTrackToPlayList(track: Track, playlist: PlayList) {
        viewModelScope.launch {
            val selectTrack = SelectTrack(
                0,
                track.trackId,
                playlist.id,
                track.trackName,
                track.artistName,
                track.trackTime,
                track.artworkUrl100,
                track.collectionName,
                track.releaseDate,
                track.primaryGenreName,
                track.previewUrl,
                track.country,
                track.isFavourite
            )
            playListInteractor.insertTrackToPlaylist(track).collect { numberInsert ->
                if (numberInsert == 1L) {
                    stateInsertTrack.postValue(InsertTrackPlayListState.Success(playlist.namePlaylist))
                } else {
                    stateInsertTrack.postValue(InsertTrackPlayListState.Fail(playlist.namePlaylist))
                }
            }
        }
    }


}

