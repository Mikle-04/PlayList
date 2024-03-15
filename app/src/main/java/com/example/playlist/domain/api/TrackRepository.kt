package com.example.playlist.domain.api

import com.example.playlist.domain.models.Track

interface TrackRepository {
    fun searchTrack(expression: String): List<Track>?
}