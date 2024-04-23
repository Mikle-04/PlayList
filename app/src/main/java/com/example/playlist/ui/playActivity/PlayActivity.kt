package com.example.playlist.ui.playActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.playlist.util.Creator
import com.example.playlist.R



class PlayActivity : AppCompatActivity() {

    private val providePlayController = Creator.providePlayController(this)
    private lateinit var back: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        back = findViewById(R.id.back_img)

        back.setOnClickListener {
            finish()
        }
        providePlayController.onCreate()
    }




    override fun onPause() {
        super.onPause()
       providePlayController.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        providePlayController.onDestroy()
    }
}