package com.example.playlist.di.viewModelModule

import android.os.Handler
import android.os.Looper
import com.example.playlist.ui.searchActivity.viewModel.TrackSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModel {
        TrackSearchViewModel(get(), get(), get())
    }
    factory<Handler> {
        Handler(Looper.getMainLooper())
    }
}