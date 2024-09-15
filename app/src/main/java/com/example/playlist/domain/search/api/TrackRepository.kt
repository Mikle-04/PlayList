package com.example.playlist.domain.search.api

import com.example.playlist.domain.search.models.Track
import com.example.playlist.util.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTrack(expression: String): Flow<Resource<List<Track>>>

    fun getHistoryTrack(): List<Track>

}