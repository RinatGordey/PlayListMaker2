package com.example.playlistmaker2.sharing.domain.impl

import com.example.playlistmaker2.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker2.sharing.domain.api.SharingInteractor
import com.example.playlistmaker2.sharing.domain.model.EmailData

private const val APP_LINK = "https://practicum.yandex.ru/android-developer/"
private const val AGREEMENT_LINK = "https://yandex.ru/legal/practicum_offer/"
private const val SUPPORT_EMAIL = "rinatgordey@yandex.ru"
private const val MESSAGE_EMAIL = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
private const val TEXT_EMAIL = "Спасибо разработчикам и разработчицам за крутое приложение!"

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getAgreementLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String =APP_LINK

    private fun getSupportEmailData(): EmailData {
        return EmailData(SUPPORT_EMAIL, MESSAGE_EMAIL,TEXT_EMAIL)
    }

    private fun getAgreementLink(): String = AGREEMENT_LINK
}