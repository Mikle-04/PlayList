package com.example.playlist.domain.search.impl

import com.example.playlist.domain.search.api.TrackInteractor
import com.example.playlist.domain.search.api.TrackRepository
import com.example.playlist.domain.search.models.Track
import com.example.playlist.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    override fun searchTrack(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTrack(expression).map { result ->
            when(result){
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun getHistoryTrack(): List<Track> {
      return repository.getHistoryTrack()
    }


}