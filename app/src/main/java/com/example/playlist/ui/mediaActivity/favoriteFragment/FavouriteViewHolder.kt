package com.example.playlist.ui.mediaActivity.favoriteFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlist.R
import com.example.playlist.domain.search.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class FavouriteViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.view_track_history, parent, false)
) {
        var trackImg: ImageView = itemView.findViewById(R.id.track_img)
        var trackName : TextView = itemView.findViewById(R.id.trackName)
        var artistName: TextView = itemView.findViewById(R.id.artistName)
        var trackTime: TextView = itemView.findViewById(R.id.trackTime)

    fun bind(track: Track){
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.img_track_default)
            .into(trackImg)

        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
    }
}