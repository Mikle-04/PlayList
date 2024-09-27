package com.example.playlist.ui.playActivity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist.R
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.playActivity.models.FavouriteState
import com.example.playlist.ui.playActivity.models.PlayerState
import com.example.playlist.ui.playActivity.viewModel.PlayTrackViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlayActivity : AppCompatActivity() {


    private val viewModel: PlayTrackViewModel by viewModel() {
        parametersOf(
            intent.getIntExtra("trackId", 0),
            intent.getStringExtra("preview_url"),
        )
    }

    private lateinit var coverArtwork: ImageView
    private lateinit var trackName: TextView
    private lateinit var authorTrack: TextView
    private lateinit var timePlay: TextView
    private lateinit var timeTrack: TextView
    private lateinit var albumName: TextView
    private lateinit var yearTrack: TextView
    private lateinit var genreTrack: TextView
    private lateinit var countryTrack: TextView
    private lateinit var play: ImageView
    private lateinit var back: ImageView
    private lateinit var favourite: ImageView
    private var track: Track? = null

    private var uri_img: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        coverArtwork = findViewById(R.id.cover_img)
        trackName = findViewById(R.id.name_track_txt)
        authorTrack = findViewById(R.id.author_txt)
        timePlay = findViewById(R.id.play_second_txt)
        timeTrack = findViewById(R.id.time_txt)
        albumName = findViewById(R.id.album_name_txt)
        yearTrack = findViewById(R.id.year_txt)
        genreTrack = findViewById(R.id.genre_txt)
        countryTrack = findViewById(R.id.country_txt)
        play = findViewById(R.id.play_img)
        back = findViewById(R.id.back_img)
        favourite = findViewById(R.id.like_img)

        getIntentSearchActivity()
        getCoverArtwork()




        back.setOnClickListener {
            this.onBackPressed()
        }


        play.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.observeState().observe(this) {
            renderState(it)
            play.isEnabled = it.isPlayButtonEnabled
            play.setImageResource(it.imgPlay)
            timePlay.text = it.progress

        }



        favourite.setOnClickListener {
            viewModel.onFavouriteClicked(track)
        }
        viewModel.observeStateFavourite().observe(this) { favourite ->
            if (favourite != null)
                favouriteState(favourite)
        }


    }

    private fun getIntentSearchActivity() {
        track = Track(
            trackId = intent.getIntExtra("trackId", 0),
            trackName = intent.getStringExtra("track_name") ?: "",
            artistName = intent.getStringExtra("artist_name") ?: "",
            trackTime = intent.getLongExtra("time_track", 0),
            collectionName = intent.getStringExtra("collection_name") ?: "",
            releaseDate = intent.getStringExtra("release_data")?.take(4) ?: "",
            primaryGenreName = intent.getStringExtra("genre_name") ?: "",
            artworkUrl100 = intent.getStringExtra("artwork_url") ?: "",
            country = intent.getStringExtra("country_name") ?: "",
            previewUrl = intent.getStringExtra("preview_url") ?: ""
        )

        trackName.text = track?.trackName
        authorTrack.text = track?.artistName
        timeTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track?.trackTime)
        albumName.text = track?.collectionName
        yearTrack.text = track?.releaseDate
        genreTrack.text = track?.primaryGenreName
        countryTrack.text = track?.country
        uri_img = track?.artworkUrl100.toString()

        albumName.isSelected = true
    }

    private fun getCoverArtwork() {
        Glide.with(applicationContext)
            .load(uri_img?.replaceAfterLast('/', "512x512bb.jpg"))
            .transform(
                CenterCrop(),
                RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.size_8dp))
            )
            .placeholder(R.drawable.img_track_default)
            .into(coverArtwork)
    }


    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun renderState(playerState: PlayerState) {
        when (playerState) {
            is PlayerState.Playing -> {
                play.setImageResource(R.drawable.stop_button)
            }

            is PlayerState.Prepared, is PlayerState.Paused, is PlayerState.Default -> {
                play.setImageResource(R.drawable.play_button)

            }

        }

    }

    private fun favouriteState(isFavourite: Boolean) {
        if (isFavourite) {
            favourite.setImageResource(R.drawable.like_click_button)
        } else {
            favourite.setImageResource(R.drawable.like_button)
        }

    }


}
//