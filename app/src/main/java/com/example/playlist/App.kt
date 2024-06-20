package com.example.playlist

import android.app.Application
import com.example.playlist.data.di.externalNavigatorModule
import com.example.playlist.data.di.playerRepositoryModule
import com.example.playlist.data.di.sharingRepositoryModule
import com.example.playlist.data.di.themeRepositoryModule
import com.example.playlist.data.di.trackRepositoryModule
import com.example.playlist.domain.di.sharingInteractorModule
import com.example.playlist.domain.di.themeInteractorModule
import com.example.playlist.domain.di.trackInteractorModule
import com.example.playlist.domain.settings.ThemeInteractor
import com.example.playlist.ui.di.searchViewModelModule
import com.example.playlist.ui.di.settingViewModelModule
import historySearchRepositoryModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import playViewModelModule

class App : Application(), KoinComponent {
    private var darkTheme = false
    private val themeInteractor: ThemeInteractor by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                playerRepositoryModule,
                playViewModelModule,
                historySearchRepositoryModule,
                trackRepositoryModule,
                trackInteractorModule,
                searchViewModelModule,
                themeRepositoryModule,
                themeInteractorModule,
                externalNavigatorModule,
                sharingRepositoryModule,
                sharingInteractorModule,
                settingViewModelModule
            )
        }
        darkTheme = themeInteractor.getThemeSettings()
        themeInteractor.updateThemeSettings(darkTheme)


    }
}