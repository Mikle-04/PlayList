package com.example.playlist.data.network


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlist.data.NetworkClient
import com.example.playlist.data.dto.Response
import com.example.playlist.data.dto.TrackRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context): NetworkClient {
    private val baseUrls = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrls)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(TrackApi::class.java)
    override fun doRequest(request: TrackRequest): Response {

        if (isConnected() == false) {
            return Response().apply { resultCode = -1 }
        }
        if (request !is TrackRequest){
            return Response().apply { resultCode = 400 }
        }

        val resp = trackService.search(request.expression).execute()
        val body = resp.body()
        return if (body != null){
            body.apply { resultCode = resp.code() }
        }else{
            Response().apply { resultCode = resp.code() }
        }

    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}