package com.example.playlist.ui.mediaActivity.favoriteFragment.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist.domain.favorite.db.api.FavouriteInteractor
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.mediaActivity.favoriteFragment.state.HistoryState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteFragmentViewModel(
    private val favouriteInteractor: FavouriteInteractor
) : ViewModel() {
    init {

    }

    private val stateLiveData = MutableLiveData<HistoryState>()

    fun observeState(): LiveData<HistoryState> = stateLiveData

    fun getFavouriteTrack() {
        viewModelScope.launch(Dispatchers.IO) {
            favouriteInteractor
                .getFavouriteTrack()
                .collect { track -> processResult(track) }
        }
    }

    private fun processResult(track: List<Track>) {
        if (track.isEmpty()) {
            renderState(HistoryState.Empty(""))
        } else {
            val reverseFavouriteTrack = track.reversed()
            renderState(HistoryState.Content(reverseFavouriteTrack))
        }
    }

    private fun renderState(state: HistoryState) {
        stateLiveData.postValue(state)
    }


}