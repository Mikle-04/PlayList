package com.example.playlist.di.domainModule

import com.example.playlist.domain.sharing.api.SharingInteractor
import com.example.playlist.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val sharingInteractorModule = module {
    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }
}