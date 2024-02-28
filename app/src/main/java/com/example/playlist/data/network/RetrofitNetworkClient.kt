package com.example.playlist.data.network

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
    override fun doRequest(dto: Any): Response {
        if (dto is TrackRequest){
            val resp = trackService.search(dto.expression).execute()
            val body = resp.body() ?: Response()
            return body.apply { resultCode = resp.code() }
        }else{
            return  Response().apply { resultCode = 400 }
        }
    }
}