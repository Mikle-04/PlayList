package com.example.playlist.ui.playActivity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist.R
import com.example.playlist.ui.playActivity.models.PlayerState
import com.example.playlist.ui.playActivity.viewModel.PlayTrackViewModel
import java.text.SimpleDateFormat
import java.util.Locale


class PlayActivity : ComponentActivity() {

    private lateinit var viewModel: PlayTrackViewModel

    private lateinit var cover_artwork: ImageView
    private lateinit var track_name: TextView
    private lateinit var author_track: TextView
    private lateinit var time_play: TextView
    private lateinit var time_track: TextView
    private lateinit var album_name: TextView
    private lateinit var year_track: TextView
    private lateinit var genre_track: TextView
    private lateinit var country_track: TextView
    private lateinit var play: ImageView

    private lateinit var back: ImageView

    private var url: String? = null
    private var uri_img: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)


        viewModel = ViewModelProvider(this)[PlayTrackViewModel::class.java]
        viewModel.observeState().observe(this) {
            renderState(it)
        }

        cover_artwork = findViewById(R.id.cover_img)
        track_name = findViewById(R.id.name_track_txt)
        author_track = findViewById(R.id.author_txt)
        time_play = findViewById(R.id.play_second_txt)
        time_track = findViewById(R.id.time_txt)
        album_name = findViewById(R.id.album_name_txt)
        year_track = findViewById(R.id.year_txt)
        genre_track = findViewById(R.id.genre_txt)
        country_track = findViewById(R.id.country_txt)
        play = findViewById(R.id.play_img)
        back = findViewById(R.id.back_img)

        back.setOnClickListener {
            finish()
        }

        viewModel.observeTime().observe(this){
            time_play.text = it
        }

        getIntentSearchActivity()
        getCoverArtwork()
        viewModel.preparePlayer(url)
        play.setOnClickListener {
            viewModel.playbackControl()
        }
    }

    private fun getIntentSearchActivity() {
        track_name.text = intent.getStringExtra("EXTRA_NAME").toString()
        author_track.text = intent.getStringExtra("EXTRA_AUTHOR").toString()
        time_track.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(intent.getLongExtra("EXTRA_DURATION", 0))
        album_name.text = intent.getStringExtra("EXTRA_COLLECTION").toString()
        album_name.isSelected = true
        year_track.text = intent.getStringExtra("EXTRA_DATE").toString().take(4)
        genre_track.text = intent.getStringExtra("EXTRA_GENRE").toString()
        country_track.text = intent.getStringExtra("EXTRA_COUNTRY").toString()
        url = intent.getStringExtra("EXTRA_PLAY").toString()
        uri_img = intent.getStringExtra("EXTRA_IMAGE").toString()
    }

    private fun getCoverArtwork() {
        Glide.with(applicationContext)
            .load(uri_img?.replaceAfterLast('/', "512x512bb.jpg"))
            .transform(
                CenterCrop(),
                RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.size_8dp))
            )
            .placeholder(R.drawable.img_track_default)
            .into(cover_artwork)
    }



    private fun renderState(playerState: PlayerState) {
        when (playerState) {
            PlayerState.Play -> {
                play.setImageResource(R.drawable.stop_button)
            }

            PlayerState.Prepare -> {
                play.isEnabled = true
                play.setImageResource(R.drawable.play_button)
                time_play.text = "00:00"
            }

            PlayerState.Pause, PlayerState.Default -> {
                play.setImageResource(R.drawable.play_button)
            }
        }
    }


    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

}