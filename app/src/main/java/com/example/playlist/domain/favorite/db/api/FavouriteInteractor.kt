package com.example.playlist.domain.favorite.db.api

import com.example.playlist.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteInteractor {
    fun getFavouriteTrack(): Flow<List<Track>>

     fun deleteFavoriteTrack(track: Track)

    fun insertFavoriteTrack(track: Track)

    fun getFavouriteTrackId(trackId: Int) : Flow<Boolean>
}