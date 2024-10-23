package com.example.playlist.ui.mediaActivity.playListFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist.R
import com.example.playlist.domain.playList.models.PlayList

class PlayListAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listPlayList = mutableListOf<PlayList>()
    override fun getItemViewType(position: Int): Int {

        val item = listPlayList[position]

        return when (item.imgStorage.toString()) {
            "null" -> 1
            else -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_play_list, parent, false)
        return when (viewType) {
            1 -> PlayListImgDefaultViewHolder(view, parent.context)
            else -> PlayListImgStorageViewHolder(view, parent.context)
        }
    }

    override fun getItemCount(): Int {
        return listPlayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PlayListImgDefaultViewHolder -> holder.bind(listPlayList.get(position))
            is PlayListImgStorageViewHolder -> holder.bind(listPlayList.get(position))
        }
    }
}