package com.example.playlist.ui.searchActivity.models

import com.example.playlist.domain.models.Track

sealed interface TrackState{

    object Loading : TrackState

    data class Content(val track: List<Track>): TrackState

    data class Error(val errorMessage: String) : TrackState

    data class Empty(val message: String) : TrackState
}