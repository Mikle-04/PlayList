package com.example.playlist.ui.searchActivity.viewModel

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlist.creator.Creator
import com.example.playlist.domain.search.api.TrackInteractor
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.searchActivity.models.TrackState


class TrackSearchViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val RunnableTag = "SEARCH"

        fun getModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TrackSearchViewModel(
                    application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application,
                )
            }
        }
    }


    private val trackInteractor = Creator.provideTrackInteractor(getApplication<Application>())

    private var handler: Handler = Handler(Looper.getMainLooper())

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


    override fun onCleared() {
        handler.removeCallbacks(searchRunable)
    }



}