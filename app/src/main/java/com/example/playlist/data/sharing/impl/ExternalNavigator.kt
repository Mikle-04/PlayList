package com.example.playlist.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlist.R
import com.example.playlist.domain.sharing.model.EmailData

class ExternalNavigator(val context: Context) {

    fun shareLink(shareLink: String) {

        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareLink)
            type = "text/plain"
            Intent.createChooser(this, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }

    fun openLink(termsLink: String) {
        val url = Uri.parse(termsLink)
        val intent = Intent(Intent.ACTION_VIEW, url)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)

    }

    fun openEmail(email: EmailData) {

        Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email.emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, email.title)
            putExtra(Intent.EXTRA_TEXT, email.text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }
}