package com.example.playlistmaker2.settings.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker2.settings.domain.api.SettingsRepository
import com.example.playlistmaker2.settings.domain.models.ThemeSettings

private const val THEME_PREFERENCES = "Theme_preferences"
private const val KEY_FOR_THEME = "key_for_theme"

class SettingsRepositoryImpl(context: Context): SettingsRepository {
    val sharedPrefs = context.getSharedPreferences(
        THEME_PREFERENCES,
        MODE_PRIVATE,
    )
    private var darkTheme:Boolean = false

    override fun getThemeSettings(): ThemeSettings {

        darkTheme = sharedPrefs.getBoolean(KEY_FOR_THEME, darkTheme)
        return ThemeSettings(darkTheme)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        darkTheme = settings.darkThemeEnabled
        sharedPrefs.edit()
            .putBoolean(KEY_FOR_THEME, darkTheme)
            .apply()
    }
}