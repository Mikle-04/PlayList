package com.example.playlist.ui.di

import com.example.playlist.ui.searchActivity.viewModel.TrackSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModel {
        TrackSearchViewModel()
    }
}