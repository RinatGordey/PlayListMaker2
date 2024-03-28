package com.example.playlistmaker2.settings.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker2.settings.domain.api.SettingsInteractor
import com.example.playlistmaker2.settings.domain.models.ThemeSettings
import com.example.playlistmaker2.sharing.domain.api.SharingInteractor
import com.example.playlistmaker2.util.App

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val application: Application
) : ViewModel() {

    private val themeSwitcherLiveData =
        MutableLiveData(settingsInteractor.getThemeSettings().darkThemeEnabled)

    fun getThemeSwitcherLiveData(): LiveData<Boolean> = themeSwitcherLiveData

    fun shareApp() = sharingInteractor.shareApp()
    fun openSupport() = sharingInteractor.openSupport()
    fun openTerms() = sharingInteractor.openTerms()
    fun switchTheme(darkThemeEnabled: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(darkThemeEnabled))
        themeSwitcherLiveData.postValue(darkThemeEnabled)
        (application as App).switchTheme(darkThemeEnabled)
    }
}