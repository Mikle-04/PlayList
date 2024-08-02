package com.example.playlist.ui.searchActivity.viewModel

import android.os.Handler
import android.os.Looper
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
import org.koin.core.component.inject


class TrackSearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val repositoryHistory: SearchHistoryRepository
) : ViewModel(), KoinComponent {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var searchJob: Job? = null


    private var lastSearchText: String? = null

    private val tracks = mutableListOf<Track>()

    //create liveData
    private val stateLiveData = MutableLiveData<TrackState>()

    fun observeState(): LiveData<TrackState> = stateLiveData

    private fun renderState(state: TrackState) {
        stateLiveData.postValue(state)
    }


    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText){
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
            searchJob?.cancel()
            renderState(TrackState.Loading)

            trackInteractor.searchTrack(
                newSearchText,
                object : TrackInteractor.TrackConsumer {
                    override fun consume(foundMovies: List<Track>?, errorMessage: String?) {

                        if (foundMovies != null) {
                            tracks.clear()
                            tracks.addAll(foundMovies)
                        }

                        when {
                            errorMessage != null -> {
                                renderState(
                                    TrackState.Error(errorMessage)
                                )

                            }

                            tracks.isEmpty() -> {
                                renderState(
                                    TrackState.Empty(
                                        message = ""
                                    )
                                )
                            }

                            else -> {
                                renderState(
                                    TrackState.Content(
                                        track = tracks
                                    )
                                )
                            }
                        }


                    }

                })
        }
    }

    fun getSearchHistory(): List<Track> {
        return repositoryHistory.getSearchHistory().toMutableList()
    }

    fun saveSearchHistory(track: List<Track>) {
        repositoryHistory.saveHistory(track)
    }

    fun clearSearchHistory(track: MutableList<Track>) {
        repositoryHistory.clearHistory(track)
    }



}