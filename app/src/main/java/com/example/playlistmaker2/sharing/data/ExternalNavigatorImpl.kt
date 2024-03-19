package com.example.playlistmaker2.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker2.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker2.sharing.domain.model.EmailData

class ExternalNavigatorImpl(val context: Context): ExternalNavigator {

    override fun shareLink(appLink: String) {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, appLink)
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }

    override fun openEmail(emailData: EmailData) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
            putExtra(Intent.EXTRA_TEXT, emailData.message)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }

    override fun openLink(termsLink: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(termsLink)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}