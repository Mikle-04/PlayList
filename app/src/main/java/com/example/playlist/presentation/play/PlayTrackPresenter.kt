package com.example.playlist.presentation.play

import android.app.Activity
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist.R
import java.text.SimpleDateFormat
import java.util.Locale

class PlayTrackPresenter(private val activity: Activity) {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIMER_PLAY = 500L
    }

    private var playerState = STATE_DEFAULT

    private lateinit var cover_artwork: ImageView
    private lateinit var track_name: TextView
    private lateinit var author_track: TextView
    private lateinit var time_play: TextView
    private lateinit var time_track: TextView
    private lateinit var album_name: TextView
    private lateinit var year_track: TextView
    private lateinit var genre_track: TextView
    private lateinit var country_track: TextView
    private var url: String? = null
    private var uri_img: String? = null
    private lateinit var play: ImageView

    private val timeFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val handlerPlay = Handler(Looper.getMainLooper())
    private var timeSoundRunnable = Runnable { handlerTimePlay() }
    private var mediaPlayer = MediaPlayer()
    fun onCreate(){
        cover_artwork = activity.findViewById(R.id.cover_img)
        track_name = activity.findViewById(R.id.name_track_txt)
        author_track = activity.findViewById(R.id.author_txt)
        time_play = activity.findViewById(R.id.play_second_txt)
        time_track = activity.findViewById(R.id.time_txt)
        album_name = activity.findViewById(R.id.album_name_txt)
        year_track = activity.findViewById(R.id.year_txt)
        genre_track = activity.findViewById(R.id.genre_txt)
        country_track = activity.findViewById(R.id.country_txt)
        play = activity.findViewById(R.id.play_img)
        time_play.text = timeFormat.format(0)

        getIntentSearchActivity()
        getCoverArtwork()
        preparePlayer()
        play.setOnClickListener {
            playbackControl()
        }

    }

    private fun getIntentSearchActivity() {
        track_name.text = activity.intent.getStringExtra("EXTRA_NAME").toString()
        author_track.text = activity.intent.getStringExtra("EXTRA_AUTHOR").toString()
        time_track.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(activity.intent.getLongExtra("EXTRA_DURATION", 0))
        album_name.text = activity.intent.getStringExtra("EXTRA_COLLECTION").toString()
        album_name.isSelected = true
        year_track.text = activity.intent.getStringExtra("EXTRA_DATE").toString().take(4)
        genre_track.text = activity.intent.getStringExtra("EXTRA_GENRE").toString()
        country_track.text = activity.intent.getStringExtra("EXTRA_COUNTRY").toString()
        url = activity.intent.getStringExtra("EXTRA_PLAY").toString()
        uri_img = activity.intent.getStringExtra("EXTRA_IMAGE").toString()
    }

    private fun getCoverArtwork(){
        Glide.with(activity.applicationContext)
            .load(uri_img?.replaceAfterLast('/', "512x512bb.jpg"))
            .transform(
                CenterCrop(),
                RoundedCorners(activity.applicationContext.resources.getDimensionPixelSize(R.dimen.size_8dp))
            )
            .placeholder(R.drawable.img_track_default)
            .into(cover_artwork)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            play.setImageResource(R.drawable.play_button)
            playerState = STATE_PREPARED
            handlerPlay.removeCallbacks(timeSoundRunnable)
            time_play.text = timeFormat.format(0)


        }
    }

    private fun playbackControl() {
        when (playerState) {
           STATE_PLAYING -> {
                pausePlayer()
                handlerPlay.removeCallbacks(timeSoundRunnable)
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                handlerPlay.post(timeSoundRunnable)
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        play.setImageResource(R.drawable.stop_button)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
    }

    private fun handlerTimePlay() {
        time_play.text = timeFormat.format(mediaPlayer.currentPosition + TIMER_PLAY)
        handlerPlay.postDelayed(timeSoundRunnable, TIMER_PLAY)

    }

    fun onPause(){
        pausePlayer()
    }

    fun onDestroy(){
        mediaPlayer.release()
        handlerPlay.removeCallbacks(timeSoundRunnable)
    }
}