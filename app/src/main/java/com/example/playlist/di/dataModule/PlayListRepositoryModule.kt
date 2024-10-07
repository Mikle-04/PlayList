package com.example.playlist.di.dataModule

import com.example.playlist.data.playList.PlayListRepositoryImpl
import com.example.playlist.domain.playList.api.PlayListRepository
import org.koin.dsl.module

val playListRepositoryModule = module {
    single<PlayListRepository>{
        PlayListRepositoryImpl(get(), get())
    }
}