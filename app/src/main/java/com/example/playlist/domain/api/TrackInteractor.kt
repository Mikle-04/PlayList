package com.example.playlist.domain.api

import com.example.playlist.domain.models.Track

interface TrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundMovies: List<Track>?, errorMessage: String?)
    }
}