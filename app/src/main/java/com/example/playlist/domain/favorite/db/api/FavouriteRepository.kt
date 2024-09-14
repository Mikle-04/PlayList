package com.example.playlist.domain.favorite.db.api

import com.example.playlist.data.favourite.db.TrackEntity
import com.example.playlist.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {
    fun getFavouriteTrack(): Flow<List<Track>>

     fun deleteFavoriteTrack(track: Track)

     fun insertFavoriteTrack(track: Track)
}