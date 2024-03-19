package com.example.playlistmaker2.util

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker2.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker2.settings.domain.api.SettingsRepository
import com.example.playlistmaker2.settings.domain.impl.SettingsInteractorImpl

class App : Application() {
    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
    fun provideSettingsInteractor(context: Context): SettingsInteractorImpl {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private var darkTheme: Boolean = true

    override fun onCreate() {
        super.onCreate()
        darkTheme = provideSettingsInteractor(this).getThemeSettings().darkThemeEnabled
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}