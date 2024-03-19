package com.example.playlistmaker2.sharing.domain.impl

import android.content.Context
import com.example.playlistmaker2.R
import com.example.playlistmaker2.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker2.sharing.domain.api.SharingInteractor
import com.example.playlistmaker2.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String = context.getString(R.string.android_developer)

    private fun getSupportEmailData(): EmailData {
        val mailFromMe = context.getString(R.string.mail_from_me)
        val mailTitle = context.getString(R.string.mail_title)
        val mailText = context.getString(R.string.mail_text)
        return EmailData(mailFromMe, mailTitle, mailText)
    }

    private fun getTermsLink(): String = context.getString(R.string.push_agreement)
}