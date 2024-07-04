package com.example.playlist.di.dataModule

import com.example.playlist.data.sharing.impl.SharingRepositoryImpl
import com.example.playlist.domain.sharing.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharingRepositoryModule = module {
    factory<SharingRepository> {
        SharingRepositoryImpl(androidContext(), get())
    }
}