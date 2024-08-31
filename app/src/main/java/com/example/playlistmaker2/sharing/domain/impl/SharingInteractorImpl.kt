package com.example.playlistmaker2.sharing.domain.impl

import android.app.Application
import com.example.playlistmaker2.R
import com.example.playlistmaker2.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker2.sharing.domain.api.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val application: Application,
) : SharingInteractor {

    override fun shareApp() {
        val message = application.getString(R.string.android_developer)
        externalNavigator.shareLink(message)
    }

    override fun openTerms() {
        externalNavigator.openLink()
    }

    override fun openSupport() {
        externalNavigator.openEmail(externalNavigator.getSupportEmailData())
    }
}