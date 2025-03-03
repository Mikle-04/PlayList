package com.example.playlist.di.viewModelModule

import com.example.playlist.ui.mediaActivity.playListFragment.viewModel.ListPlayListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listPlayListViewModelModule = module {
    viewModel { ListPlayListViewModel(get()) }
}