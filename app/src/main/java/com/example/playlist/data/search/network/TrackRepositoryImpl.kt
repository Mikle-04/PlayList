package com.example.playlist.data.search.network

import android.content.Context
import android.content.Intent
import com.example.playlist.data.search.NetworkClient
import com.example.playlist.data.search.dto.TrackRequest
import com.example.playlist.data.search.dto.TrackResponse
import com.example.playlist.domain.search.api.TrackRepository
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.playActivity.PlayActivity
import com.example.playlist.util.Resource

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTrack(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Нет подключения к интернету")
            }
            200 -> {
                Resource.Success((response as TrackResponse).results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTime,
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.previewUrl,
                        it.country
                    )
                }
                )
            }

            else -> Resource.Success(emptyList())
        }
    }
}

