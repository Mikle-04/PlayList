package com.example.playlist.di.domainModule

import com.example.playlist.domain.settings.ThemeInteractor
import org.koin.dsl.module

val themeInteractorModule = module {
    factory {
        ThemeInteractor(get())
    }
}