package com.example.playlist.domain.sharing.impl

import com.example.playlist.domain.sharing.SharingRepository
import com.example.playlist.domain.sharing.api.SharingInteractor

class SharingInteractorImpl(private val sharingRepository: SharingRepository) : SharingInteractor {

    override fun shareApp() {
        sharingRepository.shareApp()
    }

    override fun openTerms() {
        sharingRepository.openTerms()
    }

    override fun openEmail() {
        sharingRepository.openEmail()
    }

    override fun share(link: String) {
        sharingRepository.shareLink(link)
    }

}