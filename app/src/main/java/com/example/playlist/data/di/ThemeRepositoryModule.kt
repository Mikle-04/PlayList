package com.example.playlist.data.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlist.data.settings.impl.ThemeRepositoryImpl
import com.example.playlist.domain.settings.api.ThemeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val themeRepositoryModule = module {
    factory<ThemeRepository> {
        ThemeRepositoryImpl(get())
    }

    factory<SharedPreferences> {
        androidContext().getSharedPreferences("Switch_Boolean", Context.MODE_PRIVATE)
    }
}