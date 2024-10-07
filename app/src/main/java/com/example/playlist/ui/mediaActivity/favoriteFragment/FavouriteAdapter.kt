package com.example.playlist.ui.mediaActivity.favoriteFragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist.domain.search.models.Track

class FavouriteAdapter : RecyclerView.Adapter<FavouriteViewHolder>() {
    var onItemClick : ((Track) -> Unit)? = null
    var favouriteTrack = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder = FavouriteViewHolder(parent)

    override fun getItemCount(): Int = favouriteTrack.size

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.bind(favouriteTrack.get(position))
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(favouriteTrack[position])
        }
    }
}