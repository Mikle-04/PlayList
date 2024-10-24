package com.example.playlist.ui.mediaActivity.playListFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist.R
import com.example.playlist.domain.search.models.Track

class TrackPlayListAdapter(
    private val onClickItemListener: OnClickTrackListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    var listTracksOfPlaylist: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_track_history, parent, false)
        return TrackViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int {
        return listTracksOfPlaylist.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(listTracksOfPlaylist[position])
        holder.itemView.setOnClickListener { onClickItemListener.onClickTrack(listTracksOfPlaylist[position]) }
        holder.itemView.setOnLongClickListener {
            onClickItemListener.onLongClickTrack(listTracksOfPlaylist[position])
            return@setOnLongClickListener true
        }
    }

    interface OnClickTrackListener {
        fun onClickTrack(track: Track)
        fun onLongClickTrack(track: Track)
    }

}