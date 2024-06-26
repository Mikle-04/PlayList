package com.example.playlist.di.dataModule

import android.media.MediaPlayer
import com.example.playlist.data.player.impl.PlayerRepositoryImpl
import com.example.playlist.domain.player.PlayerReposiroty
import com.example.playlist.ui.playActivity.models.PlayerState
import org.koin.dsl.module

val playerRepositoryModule = module {
    factory<PlayerReposiroty> {
        PlayerRepositoryImpl(get(), get())
    }

    factory<PlayerState>{
        PlayerState.Default
    }

    factory<MediaPlayer>{
        MediaPlayer()
    }

}