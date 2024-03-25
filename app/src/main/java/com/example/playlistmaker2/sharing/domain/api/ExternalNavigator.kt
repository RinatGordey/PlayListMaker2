package com.example.playlistmaker2.sharing.domain.api

import com.example.playlistmaker2.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink()
    fun openEmail(emailData: EmailData)
    fun openLink()
    fun getSupportEmailData(): EmailData
}