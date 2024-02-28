package com.example.playlist.data.impl

import android.content.SharedPreferences
import com.example.playlist.domain.api.ThemeRepository

class ThemeRepositoryImpl(private val sharedPref: SharedPreferences) : ThemeRepository {
    private val KEY_SWITCH = "Switch_Boolean"
    override fun checkTheme(defaultTheme: Boolean): Boolean {
        return sharedPref.getBoolean(KEY_SWITCH, defaultTheme)
    }

    override fun saveTheme(darkThemeEnabled:Boolean) {
        sharedPref.edit().putBoolean(KEY_SWITCH, darkThemeEnabled).apply()
    }
}