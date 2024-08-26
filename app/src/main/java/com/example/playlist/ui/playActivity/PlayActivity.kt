package com.example.playlist.ui.playActivity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist.R
import com.example.playlist.ui.playActivity.models.PlayerState
import com.example.playlist.ui.playActivity.viewModel.PlayTrackViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlayActivity : AppCompatActivity(){

    companion object{
        private const val KEY_PREVIEW = "preview_url"
    }

   private val viewModel: PlayTrackViewModel by viewModel(){
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

        getIntentSearchActivity()
        getCoverArtwork()




        back.setOnClickListener {
           this.onBackPressed()
        }


        play.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.observeState().observe(this){
            renderState(it)
            play.isEnabled = it.isPlayButtonEnabled
            play.setImageResource(it.imgPlay)
            timePlay.text = it.progress

        }


    }

    private fun getIntentSearchActivity() {

        intent?.let {
            trackName.text = intent.getStringExtra("track_name")
            authorTrack.text = intent.getStringExtra("artist_name")
            timeTrack.text = SimpleDateFormat("mm:ss",
                Locale.getDefault()).format(intent.getLongExtra("time_track", 0))
            albumName.text = intent.getStringExtra("collection_name")
            yearTrack.text = intent.getStringExtra("release_data")?.take(4) ?: ""
            genreTrack.text = intent.getStringExtra("genre_name")
            countryTrack.text = intent.getStringExtra("country_name")
            uri_img = intent.getStringExtra("artwork_url")
        }

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

    private fun renderState(playerState: PlayerState){
        when (playerState) {
            is PlayerState.Playing-> {
                play.setImageResource(R.drawable.stop_button)
            }

           is PlayerState.Prepared,is PlayerState.Paused, is PlayerState.Default -> {
                play.setImageResource(R.drawable.play_button)

            }

        }

    }

}