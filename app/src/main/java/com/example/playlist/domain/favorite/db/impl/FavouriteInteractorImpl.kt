package com.example.playlist.domain.favorite.db.impl

import com.example.playlist.data.favourite.db.TrackEntity
import com.example.playlist.domain.favorite.db.api.FavouriteInteractor
import com.example.playlist.domain.favorite.db.api.FavouriteRepository
import com.example.playlist.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouriteInteractorImpl(private val favouriteRepository: FavouriteRepository):
    FavouriteInteractor {
    override fun getFavouriteTrack(): Flow<List<Track>> {
        return favouriteRepository.getFavouriteTrack()
    }

    override  fun deleteFavoriteTrack(track: Track) {
        favouriteRepository.deleteFavoriteTrack(track)
    }

    override fun insertFavoriteTrack(track: Track) {
        favouriteRepository.insertFavoriteTrack(track)
    }

    override fun getFavouriteTrackId(trackId: Int): Flow<Boolean> {
       return favouriteRepository.getFavouriteTrackId(trackId)
    }


}