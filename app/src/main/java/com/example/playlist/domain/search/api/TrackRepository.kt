package com.example.playlist.domain.search.api

import com.example.playlist.domain.search.models.Track
import com.example.playlist.util.Resource

interface TrackRepository {
    fun searchTrack(expression: String): Resource<List<Track>>?

}