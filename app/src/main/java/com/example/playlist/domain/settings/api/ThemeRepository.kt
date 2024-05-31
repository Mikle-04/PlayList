package com.example.playlist.domain.settings.api

interface ThemeRepository {
    fun getThemeSettings(): Boolean
    fun updateThemeSetting(isNightModeOn: Boolean)

}