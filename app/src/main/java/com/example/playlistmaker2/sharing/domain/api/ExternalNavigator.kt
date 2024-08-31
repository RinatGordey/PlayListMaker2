package com.example.playlistmaker2.sharing.domain.api

import com.example.playlistmaker2.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(message: String)
    fun openEmail(emailData: EmailData)
    fun openLink()
    fun getSupportEmailData(): EmailData
}