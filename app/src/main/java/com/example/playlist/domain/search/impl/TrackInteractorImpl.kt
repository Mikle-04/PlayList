package com.example.playlist.domain.search.impl

import com.example.playlist.domain.search.api.TrackInteractor
import com.example.playlist.domain.search.api.TrackRepository
import com.example.playlist.util.Resource

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {
    private val executor = java.util.concurrent.Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            val resource = repository.searchTrack(expression)
            when (resource) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }

                else -> {}
            }
        }
    }
}