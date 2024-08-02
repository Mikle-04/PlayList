package com.example.playlist.ui.playActivity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
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

    private var uri_img: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

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
            time_play.text = it.progress

        }


    }

    private fun getIntentSearchActivity() {

        intent?.let {
            track_name.text = intent.getStringExtra("track_name")
            author_track.text = intent.getStringExtra("artist_name")
            time_track.text = SimpleDateFormat("mm:ss",
                Locale.getDefault()).format(intent.getLongExtra("time_track", 0))
            album_name.text = intent.getStringExtra("collection_name")
            year_track.text = intent.getStringExtra("release_data")?.take(4) ?: ""
            genre_track.text = intent.getStringExtra("genre_name")
            country_track.text = intent.getStringExtra("country_name")
            uri_img = intent.getStringExtra("artwork_url")
        }

        album_name.isSelected = true
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