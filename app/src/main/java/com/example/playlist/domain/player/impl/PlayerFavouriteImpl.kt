package com.example.playlist.domain.player.impl

import com.example.playlist.data.favourite.db.converters.TrackDbConverter
import com.example.playlist.domain.favorite.db.api.FavouriteRepository
import com.example.playlist.domain.player.api.PlayerRepository
import com.example.playlist.domain.search.models.Track

class PlayerFavouriteImpl(private val trackDbConverter: TrackDbConverter, private val favouriteRepository: FavouriteRepository): PlayerRepository {
    override fun onFavouriteClick(track: Track) {
        if (track.isFavourite){
            favouriteRepository.deleteFavoriteTrack(track)
        }
        else{
            favouriteRepository.insertFavoriteTrack(track)
        }
    }


}