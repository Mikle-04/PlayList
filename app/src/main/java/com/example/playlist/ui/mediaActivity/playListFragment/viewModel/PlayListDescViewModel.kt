package com.example.playlist.ui.mediaActivity.playListFragment.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist.R
import com.example.playlist.domain.playList.api.PlayListInteractor
import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.search.models.Track
import com.example.playlist.domain.sharing.api.SharingInteractor
import com.example.playlist.ui.mediaActivity.playListFragment.state.DeleteState
import com.example.playlist.ui.mediaActivity.playListFragment.state.TrackPlayListState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class PlayListDescViewModel(
    private val playListInteractor: PlayListInteractor,
    private val sharingInteractor: SharingInteractor,
    val context: Context
) : ViewModel(){

    private var playlistState = MutableLiveData<PlayList>()
    fun getPlaylistState(): LiveData<PlayList> = playlistState

    private var listTracksOfPlaylistState = MutableLiveData<TrackPlayListState>()
    fun getListTracksOfPlaylistState(): LiveData<TrackPlayListState> = listTracksOfPlaylistState

    private var stateDeleteLiveData = MutableLiveData<DeleteState>()
    fun getStateDelete(): LiveData<DeleteState> = stateDeleteLiveData

    fun getPlaylist(playlistId: Int) {
        viewModelScope.launch {
            playListInteractor.getPlaylist(playlistId).collect { playlist ->
                playlistState.postValue(playlist)
            }
        }
    }

    fun getTracksByPlaylistId(playlistId: Int) {
        viewModelScope.launch {
            playListInteractor.getTracksByPlaylistId(playlistId).collect { listTrackOfPlaylist ->
                if (listTrackOfPlaylist.isEmpty()) {
                    listTracksOfPlaylistState.postValue(TrackPlayListState.EmptyPlaylist)
                } else {
                    listTracksOfPlaylistState.postValue(
                        TrackPlayListState.ContentPlaylist(
                            listTrackOfPlaylist
                        )
                    )
                }
            }
        }
    }

    fun deleteSelectedTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            playListInteractor.deleteSelectedTrackFromPlaylist(track)
            getTracksByPlaylistId(track.playlistId)
            getPlaylist(track.playlistId)
        }
    }

    fun shareLinkPlaylist(playlistId: Int) {
        viewModelScope.launch {
            val message = buildMessage(playlistId)
            sharingInteractor.share(message)
        }
    }

    private suspend fun buildMessage(playlistId: Int): String {
        var textMessage = ""
        viewModelScope.async {
            playListInteractor.getPlaylist(playlistId).collect { playlist ->
                textMessage =
                    "${playlist.namePlaylist}\n${playlist.descriptionPlaylist}\n${playlist.amountTracks} ${playlist.trackSpelling}"
            }
            playListInteractor.getTracksByPlaylistId(playlistId).collect { listTracksOfPlaylist ->
                var indexTrack = 0
                listTracksOfPlaylist.forEach { track ->
                    indexTrack += 1
                    textMessage += "\n${indexTrack}. ${track.artistName} - ${track.trackName} (${track.trackTime.milliseconds.inWholeMinutes}:" + SimpleDateFormat(
                        context.getString(R.string.ss_time) + ")",
                        Locale.getDefault()
                    ).format(track.trackTime)
                }
            }
        }.await()
        return textMessage
    }

    fun deletePlaylistById(playlistId: Int) {
        viewModelScope.launch {
            playListInteractor.deletePlaylistById(playlistId).collect { isCompleteDelete ->
                if (isCompleteDelete == 1) stateDeleteLiveData.postValue(DeleteState(true))
            }
        }
    }

}