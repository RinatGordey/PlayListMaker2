package com.example.playlistmaker2.sharing.domain.api

import com.example.playlistmaker2.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(string: String)
    fun openEmail(emailData: EmailData)
    fun openLink(string: String)

}