package com.example.playlist

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

const val SHARED_PREF = "SharedPreferencesListTrack"
const val TRACK_KEY = "KeyTrackObject"

class AdapterTrack() : RecyclerView.Adapter<TrackViewHolder>() {
    lateinit var sharedPref: SharedPreferences
    var trackListHistory = ArrayList<Track>()
    var trackList = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])

    }


}