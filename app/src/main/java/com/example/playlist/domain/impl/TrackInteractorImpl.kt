package com.example.playlist.domain.impl

import com.bumptech.glide.util.Executors
import com.example.playlist.domain.api.TrackInteractor
import com.example.playlist.domain.api.TrackRepository

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {
    private val executor = java.util.concurrent.Executors.newCachedThreadPool()
    override fun searchTrack(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.searchTrack(expression))
        }
    }
}