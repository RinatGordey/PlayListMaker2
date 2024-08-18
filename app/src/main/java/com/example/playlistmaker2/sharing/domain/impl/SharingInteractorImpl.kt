package com.example.playlistmaker2.sharing.domain.impl

import com.example.playlistmaker2.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker2.sharing.domain.api.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {

    override fun shareApp() {
        val message = "Check out this amazing app!"
        externalNavigator.shareLink(message)
    }

    override fun openTerms() {
        externalNavigator.openLink()
    }

    override fun openSupport() {
        externalNavigator.openEmail(externalNavigator.getSupportEmailData())
    }
}