package com.example.playlist.data.di

import com.example.playlist.data.player.impl.PlayerRepositoryImpl
import com.example.playlist.domain.player.PlayerReposiroty
import org.koin.dsl.module

val playerRepositoryModule = module {
    factory<PlayerReposiroty> {
        PlayerRepositoryImpl()
    }

}