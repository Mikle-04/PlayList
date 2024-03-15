package com.example.playlist.data

import com.example.playlist.data.dto.Response
import com.example.playlist.data.dto.TrackRequest

interface NetworkClient {
    fun doRequest(request:TrackRequest): Response
}