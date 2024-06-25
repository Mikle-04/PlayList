package com.example.playlist.di.domainModule

import com.example.playlist.domain.sharing.impl.SharingInteractor
import org.koin.dsl.module

val sharingInteractorModule = module {
    factory {
        SharingInteractor(get())
    }
}