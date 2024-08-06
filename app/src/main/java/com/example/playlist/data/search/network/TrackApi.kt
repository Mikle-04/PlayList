package com.example.playlist.data.search.network

import com.example.playlist.data.search.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApi {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): TrackResponse
}