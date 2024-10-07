package com.example.playlist.domain.player.api

import com.example.playlist.domain.search.models.Track

interface PlayerRepository {

    fun onFavouriteClick(track: Track)

}