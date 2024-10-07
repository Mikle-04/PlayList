package com.example.playlist.di.domainModule

import com.example.playlist.domain.favorite.db.api.FavouriteInteractor
import com.example.playlist.domain.favorite.db.impl.FavouriteInteractorImpl
import org.koin.dsl.module
import kotlin.math.sin

val favouriteInteractorModule = module {
    single<FavouriteInteractor>{
        FavouriteInteractorImpl(get())
    }
}