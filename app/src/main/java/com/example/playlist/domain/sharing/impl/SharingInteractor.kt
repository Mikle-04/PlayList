package com.example.playlist.domain.sharing.impl

import com.example.playlist.domain.sharing.SharingRepository

class SharingInteractor(private val sharingRepository: SharingRepository) {

    fun shareApp() {
        sharingRepository.shareApp()
    }

    fun openTerms() {
        sharingRepository.openTerms()
    }

    fun openEmail() {
        sharingRepository.openEmail()
    }
}