package com.example.playlist


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val trackName = itemView.findViewById<TextView>(R.id.trackName)
    private val artistName = itemView.findViewById<TextView>(R.id.artistName)
    private val trackTime = itemView.findViewById<TextView>(R.id.trackTime)
    private val artworkImg = itemView.findViewById<ImageView>(R.id.track_img)



    fun bind(model : Track){
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.size_2dp)))
            .placeholder(R.drawable.img_track_default)
            .into(artworkImg)
    }

}