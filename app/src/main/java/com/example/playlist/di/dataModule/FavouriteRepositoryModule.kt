package com.example.playlist.di.dataModule

import com.example.playlist.data.favourite.db.FavouriteRepositoryImpl
import com.example.playlist.domain.favorite.db.api.FavouriteRepository
import org.koin.dsl.module

val favouriteRepositoryModule = module {
    single<FavouriteRepository> {
        FavouriteRepositoryImpl(get(), get())
    }
}