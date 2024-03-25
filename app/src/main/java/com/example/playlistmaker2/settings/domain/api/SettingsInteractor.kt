package com.example.playlistmaker2.settings.domain.api

import com.example.playlistmaker2.settings.domain.models.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}