package com.example.playlist.data.di

import com.example.playlist.data.search.NetworkClient
import com.example.playlist.data.search.network.RetrofitNetworkClient
import com.example.playlist.data.search.network.TrackRepositoryImpl
import com.example.playlist.domain.search.api.TrackRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val trackRepositoryModule = module {
    factory<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    factory<NetworkClient> {
        RetrofitNetworkClient(androidContext())
    }

}