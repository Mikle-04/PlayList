package com.example.playlist

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

const val KEY_SP = "Shared_Preferences_Key"
const val KEY_SWITCH = "Switch_Boolean"

class App : Application() {
    var darkTheme = false
    lateinit var sharedPref: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPref = getSharedPreferences(KEY_SP, MODE_PRIVATE)
        checkTheme()
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        sharedPref.edit().putBoolean(KEY_SWITCH, darkThemeEnabled).apply()
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
        darkTheme = sharedPref.getBoolean(KEY_SWITCH, themeNigthDefault)
    }
}