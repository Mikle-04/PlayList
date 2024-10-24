package com.example.playlist.di.viewModelModule

import com.example.playlist.ui.mediaActivity.playListFragment.viewModel.PlayListDescViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playListDescViewModelModule = module {
    viewModel { PlayListDescViewModel(get(), get(), androidContext()) }
}