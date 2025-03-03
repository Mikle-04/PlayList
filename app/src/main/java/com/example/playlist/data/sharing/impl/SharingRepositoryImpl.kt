package com.example.playlist.data.sharing.impl

import android.content.Context
import android.content.Intent
import com.example.playlist.R
import com.example.playlist.domain.sharing.SharingRepository
import com.example.playlist.domain.sharing.model.EmailData

class SharingRepositoryImpl(val context: Context, val externalNavigator: ExternalNavigator) :
    SharingRepository {
    override fun shareLink(link: String) {
        externalNavigator.shareLink(link)
    }

    override fun shareApp() {
        externalNavigator.shareLink(context.getString(R.string.link_message))
    }

    override fun openEmail() {
        externalNavigator.openEmail(
            EmailData(
                title = context.getString(R.string.subject_message),
                text = context.getString(R.string.dev_message),
                emailAddress = context.getString(R.string.email_message)
            )
        )
    }

    override fun openTerms() {
        externalNavigator.openLink(context.getString(R.string.uri_message))
    }


}