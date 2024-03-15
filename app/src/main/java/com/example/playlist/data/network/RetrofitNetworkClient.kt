package com.example.playlist.data.network

import android.util.Log
import com.example.playlist.data.NetworkClient
import com.example.playlist.data.dto.Response
import com.example.playlist.data.dto.TrackRequest
import com.example.playlist.data.dto.TrackResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {
    private val baseUrls = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrls)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(TrackApi::class.java)
    override fun doRequest(request: TrackRequest): Response {
        return try {
            val resp = trackService.search(request.expression).execute()
            val body = resp.body() ?: Response()
            body.apply { resultCode = resp.code() }
        }catch (e:Exception){
            Response().apply { resultCode = 400 }
        }



    }
}