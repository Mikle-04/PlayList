package com.example.playlist.di.viewModelModule

import android.media.MediaPlayer
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.playActivity.viewModel.PlayViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playViewModelModule = module {
    viewModel {(track: Track) ->
        PlayViewModel(track, get(), get(), get())
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }


}