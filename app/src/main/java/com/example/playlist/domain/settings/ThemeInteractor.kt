package com.example.playlist.domain.settings

import com.example.playlist.domain.settings.api.ThemeRepository

class ThemeInteractor(val themeRepository: ThemeRepository) {
    fun getThemeSettings(): Boolean{
        return themeRepository.getThemeSettings()
    }

    fun updateThemeSettings(theme: Boolean){
        themeRepository.updateThemeSetting(theme)
    }
}