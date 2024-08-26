package com.example.playlist.ui.mediaActivity.favorite.models

import com.example.playlist.domain.search.models.Track

sealed interface FavouriteState {
    object Loading : FavouriteState

    data class Content(
        val track: List<Track>
    ) : FavouriteState

    data class Empty(
        val mesage: String
    ) : FavouriteState
}