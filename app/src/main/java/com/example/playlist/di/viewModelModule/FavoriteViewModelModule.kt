package com.example.playlist.di.viewModelModule

import com.example.playlist.ui.mediaActivity.favoriteFragment.viewModel.FavouriteFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteViewModelModule = module {
    viewModel{
        FavouriteFragmentViewModel(get())
    }
}