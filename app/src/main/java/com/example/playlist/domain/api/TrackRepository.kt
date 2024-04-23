package com.example.playlist.domain.api

import com.example.playlist.domain.models.Track
import com.example.playlist.util.Resource

interface TrackRepository {
    fun searchTrack(expression: String): Resource<List<Track>>?
}