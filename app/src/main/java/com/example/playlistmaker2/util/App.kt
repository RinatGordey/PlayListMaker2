package com.example.playlistmaker2.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker2.di.dataModule
import com.example.playlistmaker2.di.interactorModule
import com.example.playlistmaker2.di.repositoryModule
import com.example.playlistmaker2.di.viewModelModule
import com.example.playlistmaker2.settings.domain.api.SettingsRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private var darkTheme: Boolean = true

    private val settingsRepository: SettingsRepository by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
        darkTheme = settingsRepository.getThemeSettings().darkThemeEnabled
        switchTheme(darkTheme)

    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}