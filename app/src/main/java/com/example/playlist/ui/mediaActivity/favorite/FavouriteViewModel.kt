package com.example.playlist.ui.mediaActivity.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist.domain.favorite.db.api.FavouriteInteractor
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.mediaActivity.favorite.models.FavouriteState
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val favouriteInteractor: FavouriteInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavouriteState>()

    fun observeState(): LiveData<FavouriteState> = stateLiveData

    fun fillData(){
        renderState(FavouriteState.Loading)
        viewModelScope.launch {
            favouriteInteractor
                .getFavouriteTrack()
                .collect(){track ->
                    processResult(track)
                }
        }
    }

    fun processResult(track: List<Track>){
        if (track.isEmpty()){
            renderState(FavouriteState.Empty("ЛЯЛЯ"))
        }
        else{
            renderState(FavouriteState.Content(track))
        }
    }

    private fun renderState(state: FavouriteState) {
        stateLiveData.postValue(state)
    }

}