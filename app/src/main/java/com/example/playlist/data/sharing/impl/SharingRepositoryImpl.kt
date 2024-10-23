package com.example.playlist.data.sharing.impl

import android.content.Context
import android.content.Intent
import com.example.playlist.R
import com.example.playlist.domain.sharing.SharingRepository
import com.example.playlist.domain.sharing.model.EmailData

class SharingRepositoryImpl(val context: Context, val externalNavigator: ExternalNavigator) :
    SharingRepository {
    override fun share(link: String) {
        Intent(Intent.ACTION_SEND).apply {
            type = context.getString(R.string.text_plain)
            putExtra(Intent.EXTRA_TEXT, link)
            if (resolveActivity(context.packageManager) != null) {
                this.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(this)
            }
        }
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