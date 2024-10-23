package com.example.playlist.ui.mediaActivity.playListFragment

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist.R
import com.example.playlist.domain.playList.models.PlayList

class PlayListImgDefaultViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    private val namePlayList: TextView = view.findViewById(R.id.name_playlist_in_playlist)
    private val quantityTrack: TextView = view.findViewById(R.id.quantity_track)
    private val titleTrack: TextView = view.findViewById(R.id.title_track)

    fun bind(playList: PlayList) {
        namePlayList.text = playList.namePlaylist
        quantityTrack.text = playList.amountTracks.toString()
        titleTrack.text = chooseEnding(playList.amountTracks)
    }

    private fun chooseEnding(amount: Int): String {
        return if (amount % 100 in 11..19) {
            context.getString(R.string.tracks)
        } else {
            when (amount % 10) {
                1 -> context.getString(R.string.track)
                2,3,4 -> context.getString(R.string.track_a)
                else -> context.getString(R.string.tracks)
            }
        }
    }
}