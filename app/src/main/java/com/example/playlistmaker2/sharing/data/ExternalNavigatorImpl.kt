package com.example.playlistmaker2.sharing.data

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker2.R
import com.example.playlistmaker2.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker2.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val application: Application): ExternalNavigator {

    override fun shareLink() {
        val link = application.getString(R.string.android_developer)
        val intent = Intent(Intent.ACTION_SEND).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_TEXT, link)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    override fun openEmail(emailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf((emailData.email)))
            putExtra(Intent.EXTRA_SUBJECT, (emailData.message))
            putExtra(Intent.EXTRA_TEXT, (emailData.text))
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    override fun getSupportEmailData(): EmailData {
        val resources = application.resources
        return EmailData(
            resources.getString(R.string.mail_from_me),
            resources.getString(R.string.mail_title),
            resources.getString(R.string.mail_text),
        )
    }

    override fun openLink() {
        val termsLink = application.getString(R.string.push_agreement)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(termsLink))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }
}