package com.example.playlist.ui.searchActivity.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist.domain.search.api.SearchHistoryRepository
import com.example.playlist.domain.search.api.TrackInteractor
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.searchActivity.models.TrackState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent


class TrackSearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val repositoryHistory: SearchHistoryRepository,
) : ViewModel(), KoinComponent {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


    private var searchJob: Job? = null

    private var lastSearchText: String? = null

    //create liveData
    private val stateLiveData = MutableLiveData<TrackState>()

    fun observeState(): LiveData<TrackState> = stateLiveData

    private fun renderState(trackState: TrackState) {
        stateLiveData.postValue(trackState)
    }


    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText) {
            return
        }
        lastSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(lastSearchText.toString())
        }

    }

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {

            renderState(TrackState.Loading)

            viewModelScope.launch {
                trackInteractor
                    .searchTrack(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }

            }

        }
    }

    private fun processResult(listTrack: List<Track>?, errorMessage: String?) {

        val track = mutableListOf<Track>()

        if (listTrack != null) {
            track.addAll(listTrack)
        }

        when {
            errorMessage != null -> {
                renderState(
                    TrackState.Error(errorMessage)
                )

            }

            track.isEmpty() -> {
                renderState(
                    TrackState.Empty(
                        message = ""
                    )
                )
            }

            else -> {
                renderState(
                    TrackState.Content(
                        track = track
                    )
                )
            }
        }

    }


fun getHistoryTrack() : List<Track>{
    return repositoryHistory.getSearchHistory()
}

fun saveSearchHistory(track: List<Track>) {
    repositoryHistory.saveHistory(track)
}

fun clearSearchHistory(track: MutableList<Track>) {
    repositoryHistory.clearHistory(track)
}


}