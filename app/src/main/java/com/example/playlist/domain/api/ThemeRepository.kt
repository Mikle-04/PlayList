package com.example.playlist.domain.api

interface ThemeRepository {
    fun checkTheme(defaultTheme: Boolean): Boolean

    fun saveTheme(darkThemeEnabled: Boolean)
}