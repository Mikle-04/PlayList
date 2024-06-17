package com.example.playlist.data.settings.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlist.domain.settings.api.ThemeRepository

class ThemeRepositoryImpl(private val sharedPref: SharedPreferences) : ThemeRepository {
    private val KEY_SWITCH = "Switch_Boolean"
    override fun getThemeSettings(): Boolean {
        return sharedPref.getBoolean(KEY_SWITCH, false)
    }

    override fun updateThemeSetting(isNightModeOn: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isNightModeOn){
                AppCompatDelegate.MODE_NIGHT_YES
            }else{
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        sharedPref.edit().putBoolean(KEY_SWITCH, isNightModeOn).apply()
    }

}