package com.example.playlist.domain.sharing

interface SharingRepository {

    fun share(link: String)
    fun shareApp()
    fun openEmail()
    fun openTerms()
}