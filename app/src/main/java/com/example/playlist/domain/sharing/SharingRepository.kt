package com.example.playlist.domain.sharing

interface SharingRepository {

    fun shareLink(link: String)
    fun shareApp()
    fun openEmail()
    fun openTerms()

}