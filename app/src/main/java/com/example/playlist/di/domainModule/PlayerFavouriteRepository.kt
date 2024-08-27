package com.example.playlist.di.domainModule

import com.example.playlist.domain.player.impl.PlayerFavouriteImpl
import com.example.playlist.domain.player.api.PlayerRepository
import org.koin.dsl.module

val playerFavouriteImpl = module {
    factory<PlayerRepository> {
        PlayerFavouriteImpl(get(), get())
    }
}