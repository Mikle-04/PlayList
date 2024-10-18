package com.example.playlist.ui.playActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist.R
import com.example.playlist.domain.playList.models.PlayList

class PlayerListAdapter (
    private val onPlaylistClickListener: OnPlaylistClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var playlists: MutableList<PlayList> = mutableListOf()

    override fun getItemViewType(position: Int): Int {

        val itemPlaylist = playlists[position]

        return when (itemPlaylist.imgStorage.toString()) {
            "null" -> 1
            else -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_play_list_bottom_shell, parent, false)

        return when (viewType) {
            1 -> PlayerListNoImgViewHolder(view, parent.context)
            else -> PlayerListImgViewHolder(view, parent.context)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.setOnClickListener { onPlaylistClickListener.onPlaylistClick(playlists[position]) }

        when (holder) {
            is PlayerListNoImgViewHolder -> {
                holder.bind(playlists[position])
            }
            is PlayerListImgViewHolder -> {
                holder.bind((playlists[position]))
            }
        }

    }

    fun interface OnPlaylistClickListener {
        fun onPlaylistClick(playlist: PlayList)
    }
}