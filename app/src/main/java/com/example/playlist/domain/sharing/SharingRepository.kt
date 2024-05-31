package com.example.playlist.domain.sharing

interface SharingRepository {
    fun shareApp()
    fun openEmail()
    fun openTerms()
}