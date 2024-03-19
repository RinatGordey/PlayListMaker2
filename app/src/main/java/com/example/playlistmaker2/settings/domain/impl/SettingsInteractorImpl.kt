package com.example.playlistmaker2.settings.domain.impl

import com.example.playlistmaker2.settings.domain.api.SettingsInteractor
import com.example.playlistmaker2.settings.domain.api.SettingsRepository
import com.example.playlistmaker2.settings.domain.models.ThemeSettings

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository): SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}