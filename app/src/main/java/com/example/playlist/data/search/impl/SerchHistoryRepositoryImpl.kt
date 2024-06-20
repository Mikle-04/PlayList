package com.example.playlist.data.search.impl

import android.content.SharedPreferences
import com.example.playlist.domain.search.api.SearchHistoryRepository
import com.example.playlist.domain.search.models.Track
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences): SearchHistoryRepository {
    private val SEARCH_HISTORY_TRACK = "SearchHistoryTrack"

    override fun saveHistory(tracks: List<Track>){
        sharedPreferences.edit().putString(SEARCH_HISTORY_TRACK, createListToGson(tracks)).apply()
    }

    override fun getSearchHistory(): List<Track>{
        val json = sharedPreferences.getString(SEARCH_HISTORY_TRACK, null)
        return if (json != null){
            createListFromGson(json)
        }else{
            emptyList<Track>()
        }
    }

    fun createListFromGson(json: String): List<Track>{
        return Gson().fromJson(json, Array<Track>::class.java).asList()
    }
    fun createListToGson(track: List<Track>): String{
        return Gson().toJson(track)
    }
    override fun clearHistory(track: MutableList<Track>){
        track.clear()
        sharedPreferences.edit().remove(SEARCH_HISTORY_TRACK).apply()

    }



}