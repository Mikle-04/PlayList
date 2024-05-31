package com.example.playlist.data.search.network

import android.content.Context
import android.content.Intent
import com.example.playlist.domain.search.api.TrackPutRepository
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.playActivity.PlayActivity

class TrackPutRepositoryImpl(val context: Context): TrackPutRepository {
    override fun putTrack(track: Track) {
        Intent(context, PlayActivity::class.java).also {
            it.putExtra("EXTRA_NAME", track.trackName)
            it.putExtra("EXTRA_AUTHOR", track.artistName)
            it.putExtra("EXTRA_IMAGE", track.artworkUrl100)
            it.putExtra("EXTRA_DURATION", track.trackTime)
            if (track.collectionName.isNotEmpty()) {
                it.putExtra("EXTRA_COLLECTION", track.collectionName)
            }
            it.putExtra("EXTRA_DATE", track.releaseDate)
            it.putExtra("EXTRA_GENRE", track.primaryGenreName)
            it.putExtra("EXTRA_COUNTRY", track.country)
            it.putExtra("EXTRA_PLAY", track.previewUrl)
            context.startActivity(it)

        }
    }

}