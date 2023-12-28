package com.example.playlist

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class PlayActivity : AppCompatActivity() {
    lateinit var cover_artwork:ImageView
    lateinit var uri_img:String
    lateinit var track_name:TextView
    lateinit var author_track:TextView
    lateinit var time_play:TextView
    lateinit var time_track:TextView
    lateinit var album_name:TextView
    lateinit var year_track:TextView
    lateinit var genre_track:TextView
    lateinit var country_track:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        val back = findViewById<ImageView>(R.id.back_img)
        cover_artwork = findViewById(R.id.cover_img)
        track_name = findViewById(R.id.name_track_txt)
        author_track = findViewById(R.id.author_txt)
        time_play = findViewById(R.id.play_second_txt)
        time_track = findViewById(R.id.time_txt)
        album_name = findViewById(R.id.album_name_txt)
        year_track = findViewById(R.id.year_txt)
        genre_track = findViewById(R.id.genre_txt)
        country_track = findViewById(R.id.country_txt)

        back.setOnClickListener{
            finish()
        }
        time_play.text = "1:33" //до реализации

        getIntentSearchActivity()

        uri_img = intent.getStringExtra("EXTRA_IMAGE").toString()
        Glide.with(this)
            .load(getCoverArtwork())
            .transform(CenterCrop(), RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.size_8dp)))
            .placeholder(R.drawable.img_track_default)
            .into(cover_artwork)


    }

    fun getIntentSearchActivity(){
        track_name.text = intent.getStringExtra("EXTRA_NAME").toString()
        author_track.text = intent.getStringExtra("EXTRA_AUTHOR").toString()
        time_track.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(intent.getLongExtra("EXTRA_DURATION", 0))
        intent.getStringExtra("EXTRA_DURATION")
        album_name.text = intent.getStringExtra("EXTRA_COLLECTION").toString()
        album_name.isSelected = true
        year_track.text = intent.getStringExtra("EXTRA_DATE").toString().take(4)
        genre_track.text = intent.getStringExtra("EXTRA_GENRE").toString()
        country_track.text = intent.getStringExtra("EXTRA_COUNTRY").toString()
    }
    fun getCoverArtwork(): String = uri_img.replaceAfterLast('/',"512x512bb.jpg")
}