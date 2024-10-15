package com.example.playlist.ui.mediaActivity.playListFragment

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist.R
import com.example.playlist.domain.playList.models.PlayList

class PlayListImgStorageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val namePlayList: TextView = view.findViewById(R.id.name_playlist_in_player)
    private val quantityTrack: TextView = view.findViewById(R.id.quantity_track)
    private val nameTrack: TextView = view.findViewById(R.id.name_track)
    private val imgCoverPlaylist: ImageView = view.findViewById(R.id.cover_playlist_in_player)

    fun bind(playList: PlayList) {
        namePlayList.text = playList.namePlaylist
        quantityTrack.text = playList.amountTracks.toString()
        nameTrack.text = playList.trackSpelling
        imgCoverPlaylist.setImageURI(playList.imgStorage)
    }
}