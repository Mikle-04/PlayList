package com.example.playlist.data.network

import com.example.playlist.data.NetworkClient
import com.example.playlist.data.dto.TrackRequest
import com.example.playlist.data.dto.TrackResponse
import com.example.playlist.domain.api.TrackRepository
import com.example.playlist.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTrack(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackRequest(expression))
        if (response.resultCode == 200) {
            return (response as TrackResponse).results.map {
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
        }else{
          return emptyList()
        }
    }
}