package com.example.playlist.domain.sharing.api

interface SharingInteractor {

    fun shareApp()

    fun openTerms()

    fun openEmail()

    fun share(link: String)
}