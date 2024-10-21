package com.example.playlist.di.domainModule

import com.example.playlist.domain.playList.api.PlayListInteractor
import com.example.playlist.domain.playList.impl.PlayListInteractorImpl
import org.koin.dsl.module

val createPlayListInteractor = module {
    single<PlayListInteractor> {
        PlayListInteractorImpl(get())
    }
}