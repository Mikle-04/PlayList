package com.example.playlist.domain.search.api

import com.example.playlist.domain.search.models.Track

interface TrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundMovies: List<Track>?, errorMessage: String?)
    }
}