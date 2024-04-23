package com.example.playlist.presentation.tracks

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.playlist.util.Creator
import com.example.playlist.domain.api.TrackInteractor
import com.example.playlist.domain.models.Track
import com.example.playlist.ui.searchActivity.models.TrackState
import moxy.MvpPresenter


class TrackSearchPresenter(
    context: Context
) : MvpPresenter<TrackView>() {


    private val trackInteractor = Creator.provideTrackInteractor(context)

    private var handler: Handler = Handler(Looper.getMainLooper())

    private var lastSearchText: String? = null

    private val searchRunable = Runnable {
        val newSearchText = lastSearchText ?: ""
        searchRequest(newSearchText) }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val RunnableTag = "SEARCH"
    }


    private val tracks = mutableListOf<Track>()


    fun searchDebounce(changedText: String) {
        this.lastSearchText = changedText
        handler.removeCallbacksAndMessages(RunnableTag)
        handler.postDelayed(searchRunable, RunnableTag, SEARCH_DEBOUNCE_DELAY)
    }

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            handler.removeCallbacksAndMessages(RunnableTag)
            viewState.render(TrackState.Loading)

            trackInteractor.searchTrack(
                newSearchText,
                object : TrackInteractor.TrackConsumer {
                    override fun consume(foundMovies: List<Track>?, errorMessage: String?) {
                        handler.post {
                            if (foundMovies != null) {
                                tracks.clear()
                                tracks.addAll(foundMovies)
                            }

                            when {
                                errorMessage != null -> {
                                    viewState.render(
                                        TrackState.Error(errorMessage)
                                    )

                                }

                                tracks.isEmpty() -> {
                                    viewState.render(
                                        TrackState.Empty(
                                            message = ""
                                        )
                                    )
                                }

                                else -> {
                                    viewState.render(
                                        TrackState.Content(
                                            track = tracks
                                        )
                                    )
                                }
                            }

                        }
                    }

                })
        }
    }

    fun onStop() {
        handler.removeCallbacks(searchRunable)
    }

}