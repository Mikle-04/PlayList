package com.example.playlist.domain.di


import com.example.playlist.domain.search.api.TrackInteractor
import com.example.playlist.domain.search.impl.TrackInteractorImpl
import org.koin.dsl.module

val trackInteractorModule = module {
    factory<TrackInteractor> {
        TrackInteractorImpl(get())
    }
}