package com.example.playlist.ui.playActivity.models

import android.widget.ImageView
import com.example.playlist.R


sealed class PlayerState(val isPlayButtonEnabled: Boolean, val imgPlay: Int, val progress: String) {

        class Default : PlayerState(false, R.drawable.play_button,  "00:00")

        class Prepared : PlayerState(true,R.drawable.play_button,  "00:00")

        class Playing(progress: String) : PlayerState(true,R.drawable.stop_button, progress)

        class Paused(progress: String) : PlayerState(true,R.drawable.play_button, progress)
    }


