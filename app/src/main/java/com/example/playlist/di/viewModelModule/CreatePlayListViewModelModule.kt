package com.example.playlist.di.viewModelModule

import com.example.playlist.ui.mediaActivity.playListFragment.createPlayList.viewModel.CreateAlbumFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val createPlayListViewModelModule = module {
    viewModel {
        CreateAlbumFragmentViewModel(get())
    }

}