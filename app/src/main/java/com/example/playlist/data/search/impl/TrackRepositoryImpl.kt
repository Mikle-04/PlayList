package com.example.playlist.data.search.impl

import com.example.playlist.data.favourite.db.AppDatabase
import com.example.playlist.data.search.NetworkClient
import com.example.playlist.data.search.dto.TrackRequest
import com.example.playlist.data.search.dto.TrackResponse
import com.example.playlist.domain.search.api.TrackRepository
import com.example.playlist.domain.search.models.Track
import com.example.playlist.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val networkClient: NetworkClient
) : TrackRepository {
    override fun searchTrack(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Нет подключения к интернету"))
            }

            200 -> {
                val favoritesIdList = appDatabase.trackDao().getTrackId()
                emit(Resource.Success((response as TrackResponse).results.map { Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTime,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.previewUrl,
                    it.country,
                    isFavourite(it.trackId, favoritesIdList)
                ) }))
            }

            else -> emit(Resource.Success(emptyList()))
        }
    }.flowOn(Dispatchers.IO)

    private fun isFavourite (trackId: Int, favoritesIdList: List<Int>): Boolean {
        val favorite = favoritesIdList.find { it == trackId }
        return favorite != null
    }

}

