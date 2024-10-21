package com.example.playlist.ui.playActivity

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist.R
import com.example.playlist.domain.playList.models.PlayList

class PlayerListImgViewHolder(view: View, val context: Context): RecyclerView.ViewHolder(view) {
    private val imgPlaylist: ImageView = view.findViewById(R.id.cover_playlist_in_player)
    private val namePlaylist: TextView = view.findViewById(R.id.name_playlist_in_player)
    private val amountTracks: TextView = view.findViewById(R.id.amount_track_in_player)
    private val titleTracks: TextView = view.findViewById(R.id.title_track_in_player)

    fun bind(model: PlayList) {
        imgPlaylist.setImageURI(model.imgStorage)
        namePlaylist.text = model.namePlaylist
        amountTracks.text = model.amountTracks.toString()
        titleTracks.text = chooseEnding(model.amountTracks)
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