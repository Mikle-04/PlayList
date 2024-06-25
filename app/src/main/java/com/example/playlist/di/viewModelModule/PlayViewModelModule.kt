package com.example.playlist.di.viewModelModule

import android.os.Handler
import android.os.Looper
import com.example.playlist.ui.playActivity.viewModel.PlayTrackViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playViewModelModule = module {
    viewModel {params ->
        PlayTrackViewModel(get(), url = params.get())
    }

    factory<Handler> {
        Handler(Looper.getMainLooper())
    }
}