package com.example.playlist.data.search

import com.example.playlist.data.search.dto.Response
import com.example.playlist.data.search.dto.TrackRequest

interface NetworkClient {
    suspend fun doRequest(request: TrackRequest): Response
}