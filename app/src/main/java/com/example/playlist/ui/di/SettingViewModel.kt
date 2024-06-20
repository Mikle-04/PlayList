package com.example.playlist.ui.di

import com.example.playlist.ui.settingActivity.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingViewModelModule = module {
    viewModel {
        SettingsViewModel(get(), get())
    }
}