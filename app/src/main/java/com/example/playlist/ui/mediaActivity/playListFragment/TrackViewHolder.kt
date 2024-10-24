package com.example.playlist.ui.mediaActivity.playListFragment

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist.R
import com.example.playlist.domain.search.models.Track
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class TrackViewHolder(
    itemView: View,
    val context: Context,
) : RecyclerView.ViewHolder(itemView) {

    private val trackName = itemView.findViewById<TextView>(R.id.track_Name)
    private val artistName = itemView.findViewById<TextView>(R.id.artistName)
    private val trackTime = itemView.findViewById<TextView>(R.id.track_time)
    private val imageTrack = itemView.findViewById<ImageView>(R.id.track_img)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.requestLayout()
        artistName.text = model.artistName
        trackTime.text =
            "${model.trackTime.milliseconds.inWholeMinutes}:" + SimpleDateFormat(
                context.getString(R.string.ss_time), Locale.getDefault()
            ).format(model.trackTime)
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.img_track_default)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, imageTrack.context)))
            .into(imageTrack)
    }
}

fun dpToPx(
    dp: Float,
    context: Context
): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}