package com.example.playlist.ui.searchActivity.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist.domain.search.api.SearchHistoryRepository
import com.example.playlist.domain.search.api.TrackInteractor
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.searchActivity.models.TrackState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class TrackSearchViewModel(private var handler: Handler) : ViewModel(), KoinComponent {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val RunnableTag = "SEARCH"
    }

    private val trackInteractor : TrackInteractor by inject()
    private val repositoryHistory: SearchHistoryRepository by inject()


    private var lastSearchText: String? = null

    private val searchRunable = Runnable {
        val newSearchText = lastSearchText ?: ""
        searchRequest(newSearchText) }

    private val tracks = mutableListOf<Track>()

    //create liveData
    private val stateLiveData = MutableLiveData<TrackState>()

    fun observeState():LiveData<TrackState> = stateLiveData

    private fun renderState(state: TrackState){
        stateLiveData.postValue(state)
    }


    fun searchDebounce(changedText: String) {
        this.lastSearchText = changedText
        handler.removeCallbacksAndMessages(RunnableTag)
        handler.postDelayed(searchRunable, RunnableTag, SEARCH_DEBOUNCE_DELAY)
    }

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            handler.removeCallbacksAndMessages(RunnableTag)
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

    fun getSearchHistory() : List<Track>{
       return repositoryHistory.getSearchHistory().toMutableList()
    }

    fun saveSearchHistory(track: List<Track>){
        repositoryHistory.saveHistory(track)
    }

    fun clearSearchHistory(track: MutableList<Track>){
        repositoryHistory.clearHistory(track)
    }

    fun stopSearch(){
        handler.removeCallbacks(searchRunable)
    }

    override fun onCleared() {
        stopSearch()
    }



}