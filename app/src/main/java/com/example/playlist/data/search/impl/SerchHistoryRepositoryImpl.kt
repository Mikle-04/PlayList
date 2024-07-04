package com.example.playlist.data.search.impl

import android.content.SharedPreferences
import com.example.playlist.domain.search.api.SearchHistoryRepository
import com.example.playlist.domain.search.models.Track
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences): SearchHistoryRepository {
    private val KEY_HISTORY = "SearchHistoryTrack"

    override fun saveHistory(tracks: List<Track>){
        sharedPreferences.edit().putString(KEY_HISTORY, createListToGson(tracks)).apply()
    }

    override fun getSearchHistory(): List<Track>{
        val json = sharedPreferences.getString(KEY_HISTORY, null)
        return if (json != null){
            createListFromGson(json)
        }else{
            emptyList()
        }
    }

    private fun createListFromGson(json: String): List<Track>{
        return Gson().fromJson(json, Array<Track>::class.java).asList()
    }
    private fun createListToGson(track: List<Track>): String{
        return Gson().toJson(track)
    }
    override fun clearHistory(track: MutableList<Track>){
        track.clear()
        sharedPreferences.edit().remove(KEY_HISTORY).apply()

    }



}