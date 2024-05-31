package com.example.playlist.domain.search.api

import com.example.playlist.domain.search.models.Track

class TrackPutInteractor(private val trackPutRepository: TrackPutRepository) {
    fun putTrackPlay(track: Track){
        trackPutRepository.putTrack(track)
    }
}