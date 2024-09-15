package com.example.playlist.domain.search.api

import com.example.playlist.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTrack(expression: String): Flow<Pair<List<Track>?, String?>>

    fun getHistoryTrack(): List<Track>

}