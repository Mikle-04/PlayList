package com.example.playlist.ui.mediaActivity.playListFragment.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist.domain.playList.api.PlayListInteractor
import com.example.playlist.ui.mediaActivity.playListFragment.state.PlayListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlayListViewModel(val playListInteractor: PlayListInteractor) : ViewModel() {

    private var statePlayList = MutableLiveData<PlayListState>()

    fun getStatePlayList(): LiveData<PlayListState> = statePlayList

    fun getPlayListDb() {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.getListPlaylist().collect() { listPlayList ->
                if (listPlayList.isEmpty()) {
                    statePlayList.postValue(PlayListState.Empty())
                } else {
                    statePlayList.postValue(PlayListState.Content(listPlayList))
                }
            }
        }
    }


}