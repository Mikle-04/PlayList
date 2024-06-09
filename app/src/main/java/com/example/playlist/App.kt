package com.example.playlist

import android.app.Application
import com.example.playlist.creator.Creator

class App : Application() {
    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        Creator.setContext(this)
        val themeInteractor = Creator.provideThemeInteractor()
        darkTheme = themeInteractor.getThemeSettings()
        themeInteractor.updateThemeSettings(darkTheme)
    }
}