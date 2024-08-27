package com.example.playlist.data.favourite.db

import com.example.playlist.data.favourite.db.converters.TrackDbConverter
import com.example.playlist.domain.favorite.db.api.FavouriteRepository
import com.example.playlist.domain.search.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class FavouriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
) : FavouriteRepository {

    override fun getFavouriteTrack(): Flow<List<Track>> = flow {
        val track = appDatabase.trackDao().getTrack()
        emit(convertFromTrackEntityToTrack(track))
    }

    override fun deleteFavoriteTrack(track: TrackEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.trackDao().deleteTrack(track)
        }
    }

    override fun insertFavoriteTrack(track: TrackEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.trackDao().insertTrack(track)
        }
    }

    private fun convertFromTrackEntityToTrack(track: List<TrackEntity>): List<Track> {
        return track.map { track -> trackDbConverter.map(track) }
    }


}
