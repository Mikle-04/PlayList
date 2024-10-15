package com.example.playlist

import android.app.Application
import com.example.playlist.di.dataModule.dataDbConverterModule
import com.example.playlist.di.dataModule.dataDbConverterPlayList
import com.example.playlist.di.dataModule.dataDbModule
import com.example.playlist.di.dataModule.externalNavigatorModule
import com.example.playlist.di.dataModule.favouriteRepositoryModule
import com.example.playlist.di.dataModule.sharingRepositoryModule
import com.example.playlist.di.dataModule.themeRepositoryModule
import com.example.playlist.di.dataModule.trackRepositoryModule
import com.example.playlist.di.domainModule.sharingInteractorModule
import com.example.playlist.di.domainModule.themeInteractorModule
import com.example.playlist.di.domainModule.trackInteractorModule
import com.example.playlist.domain.settings.ThemeInteractor
import com.example.playlist.di.viewModelModule.searchViewModelModule
import com.example.playlist.di.viewModelModule.settingViewModelModule
import com.example.playlist.di.dataModule.historySearchRepositoryModule
import com.example.playlist.di.dataModule.playListRepositoryModule
import com.example.playlist.di.dataModule.selectTrackDbConverterModule
import com.example.playlist.di.domainModule.createPlayListInteractor
import com.example.playlist.di.domainModule.playerFavouriteImpl
import com.example.playlist.di.domainModule.favouriteInteractorModule
import com.example.playlist.di.viewModelModule.favoriteViewModelModule
import com.example.playlist.di.viewModelModule.createPlayListViewModelModule
import com.example.playlist.di.viewModelModule.playListViewModelModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import com.example.playlist.di.viewModelModule.playViewModelModule

class App : Application(), KoinComponent {
    private var darkTheme = false
    private val themeInteractor: ThemeInteractor by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
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
                settingViewModelModule,
                dataDbConverterModule,
                dataDbModule,
                favouriteRepositoryModule,
                favouriteInteractorModule,
                favoriteViewModelModule,
                playerFavouriteImpl,
                dataDbConverterPlayList,
                playListRepositoryModule,
                createPlayListInteractor,
                createPlayListViewModelModule,
                playListViewModelModule,
                selectTrackDbConverterModule
            )
        }
        darkTheme = themeInteractor.getThemeSettings()
        themeInteractor.updateThemeSettings(darkTheme)


    }
}