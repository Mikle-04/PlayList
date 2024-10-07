package com.example.playlist.di.viewModelModule

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.playActivity.viewModel.PlayTrackViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playViewModelModule = module {
    viewModel {params ->
        PlayTrackViewModel(id = params.get(), url = params.get(),  get(), get())
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }


}