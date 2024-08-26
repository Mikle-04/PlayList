package com.example.playlist.domain.favorite.db.impl

import com.example.playlist.data.favourite.db.TrackEntity
import com.example.playlist.domain.favorite.db.api.FavouriteInteractor
import com.example.playlist.domain.favorite.db.api.FavouriteRepository
import com.example.playlist.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class FavouriteInteractorImpl(private val favouriteRepository: FavouriteRepository):
    FavouriteInteractor {
    override fun getFavouriteTrack(): Flow<List<Track>> {
        return favouriteRepository.getFavouriteTrack()
    }

    override fun deleteFavoriteTrack(track: Track) {
        return favouriteRepository.deleteFavoriteTrack(track)
    }

    override fun insertFavoriteTrack(track: Track) {
        return favouriteRepository.insertFavoriteTrack(track)
    }

}