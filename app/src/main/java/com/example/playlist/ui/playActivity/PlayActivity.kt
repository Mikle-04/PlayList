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
        parametersOf(intent.getStringExtra("preview_url"))
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

    private var trackId: Int = 0
    private var trackNames: String? = null
    private var authorTracks: String? = null
    private var trackTime: Long = 0
    private var artworkUrls: String? = null
    private var collectionName: String? = null
    private var releaseDate = ""
    private var primaryGenreName: String? = null
    private var country: String? = null
    private var isFavourite: Boolean = false
    private var previewUrl:String? = null


    private var uri_img: String? = null


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

        viewModel.observeStateFavourite().observe(this){

        }

        favourite.setOnClickListener {
            track?.let { viewModel.onFavouriteClicked(it) }

        }

        viewModel.observeStateFavourite().observe(this){state ->
            isFavourite = state
            changeIconOfFavorite(isFavourite)
        }


    }

    private fun changeIconOfFavorite(isFavourites : Boolean){
        if (isFavourites){
            favourite.setImageResource(R.drawable.like_click_button)
        }
        else{
            favourite.setImageResource(R.drawable.like_button)
        }
    }


    private fun getIntentSearchActivity() {
        intent?.let {
            trackId = intent.getIntExtra("trackId", 0)
            trackNames = intent.getStringExtra("track_name")
            authorTracks = intent.getStringExtra("artist_name")
            trackTime = intent.getLongExtra("time_track", 0)
            collectionName = intent.getStringExtra("collection_name")
            releaseDate = intent.getStringExtra("release_data")?.take(4) ?: ""
            primaryGenreName = intent.getStringExtra("genre_name")
            artworkUrls = intent.getStringExtra("artwork_url")
            country = intent.getStringExtra("country_name")
            previewUrl = intent.getStringExtra("preview_url")
            isFavourite = intent.getBooleanExtra("isFavourite", false)
        }

        trackName.text = trackNames
        authorTrack.text = authorTracks
        timeTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
        albumName.text = collectionName
        yearTrack.text = releaseDate
        genreTrack.text = primaryGenreName
        countryTrack.text = country
        uri_img = artworkUrls

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


}
//