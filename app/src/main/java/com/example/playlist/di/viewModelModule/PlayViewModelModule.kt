package com.example.playlist.di.viewModelModule

import android.media.MediaPlayer
import com.example.playlist.ui.playActivity.viewModel.PlayViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playViewModelModule = module {
    viewModel {params ->
        PlayViewModel(id = params.get(), url = params.get(),  get(), get())
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }


}