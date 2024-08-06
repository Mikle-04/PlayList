package com.example.playlist.data.search.network


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlist.data.search.NetworkClient
import com.example.playlist.data.search.dto.Response
import com.example.playlist.data.search.dto.TrackRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context): NetworkClient {
    private val baseUrls = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrls)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(TrackApi::class.java)
    override suspend fun doRequest(request: TrackRequest): Response {

        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        if (request !is TrackRequest){
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO){
            try {
                val response = trackService.search(request.expression)
                response.apply { resultCode = 200 }
            }catch (e: Exception){
                Response().apply { resultCode = -1 }
            }
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