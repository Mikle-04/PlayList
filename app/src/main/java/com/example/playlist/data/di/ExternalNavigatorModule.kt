package com.example.playlist.data.di

import com.example.playlist.data.sharing.impl.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val externalNavigatorModule = module {
    factory {
        ExternalNavigator(androidContext())
    }
}