package com.example.playlist.domain.search.api

import com.example.playlist.domain.search.models.Track

interface TrackPutRepository {
    fun putTrack(track: Track)
}