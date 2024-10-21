package com.example.playlist.ui.mediaActivity.playListFragment.viewModel

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist.domain.playList.api.PlayListInteractor
import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.ui.mediaActivity.playListFragment.state.PlayListState
import com.example.playlist.ui.mediaActivity.playListFragment.state.TrackPlayListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayListViewModel(val playListInteractor: PlayListInteractor) : ViewModel() {


    private var statePlayList = MutableLiveData<PlayListState>()
    fun getStatePlayList(): LiveData<PlayListState> = statePlayList


    fun getPlayListDb() {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.getPlaylist().collect() { listPlayList ->
                if (listPlayList.isEmpty()) {
                    statePlayList.postValue(PlayListState.Empty())
                } else {
                    statePlayList.postValue(PlayListState.Content(listPlayList))
                }
            }
        }
    }




}