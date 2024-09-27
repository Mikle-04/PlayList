package com.example.playlist.ui.mediaActivity.favoriteFragment.state

import com.example.playlist.domain.search.models.Track

sealed interface HistoryState {
    data class Content(val track: List<Track>) : HistoryState

    data class Empty(val message: String): HistoryState
}