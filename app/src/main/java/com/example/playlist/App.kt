package com.example.playlist

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        Creator.setContext(this)
        checkTheme()
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        Creator.provideThemeRepository().saveTheme(darkThemeEnabled)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun checkTheme() {

        val themeNigthDefault =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        darkTheme = Creator.provideThemeRepository().checkTheme(themeNigthDefault)
    }
}