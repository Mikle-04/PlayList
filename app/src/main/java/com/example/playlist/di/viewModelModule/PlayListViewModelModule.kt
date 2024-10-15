package com.example.playlist.di.viewModelModule

import com.example.playlist.ui.mediaActivity.playListFragment.viewModel.PlayListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playListViewModelModule = module {
    viewModel { PlayListViewModel(get()) }
}