package com.example.playlist

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

const val SHARED_PREFERENCES = "SharedPreferences"
const val SEARCH_HISTORY_TRACK = "SearchHistoryTrack"


class SearchHistory(context: Context) {
    val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    private val max = 10

    fun addTrack(adapter: AdapterTrack, track: Track){
        if (adapter.trackList.contains(track)){
            adapter.trackList.removeAt(adapter.trackList.indexOf(track))
            adapter.notifyItemRemoved(adapter.trackList.indexOf(track))
            adapter.notifyItemRangeChanged(adapter.trackList.indexOf(track), adapter.trackList.size - 1)
        }
        adapter.trackList.add(0, track)
        adapter.notifyItemInserted(0)
        if(adapter.trackList.size == max + 1){
            adapter.trackList.remove(adapter.trackList[max])
            adapter.notifyItemRemoved(10)
        }
    }

    fun saveHistory(tracks: List<Track>){
        sharedPref.edit().putString(SEARCH_HISTORY_TRACK, createListToGson(tracks)).apply()
    }

    fun getSearchHistory(): List<Track>{
        val json = sharedPref.getString(SEARCH_HISTORY_TRACK, null)
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
    fun clearHistory(track: MutableList<Track>){
        track.clear()
        sharedPref.edit().remove(SEARCH_HISTORY_TRACK).apply()

    }



}