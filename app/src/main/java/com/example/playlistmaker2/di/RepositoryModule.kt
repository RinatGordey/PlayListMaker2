package com.example.playlistmaker2.di

import com.example.playlistmaker2.player.data.PlayerRepositoryImpl
import com.example.playlistmaker2.player.domain.api.PlayerRepository
import com.example.playlistmaker2.search.data.HistoryRepositoryImpl
import com.example.playlistmaker2.search.data.TracksRepositoryImpl
import com.example.playlistmaker2.search.domain.api.HistoryRepository
import com.example.playlistmaker2.search.domain.api.TracksSearchRepository
import com.example.playlistmaker2.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker2.settings.domain.api.SettingsRepository
import com.example.playlistmaker2.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker2.sharing.domain.api.ExternalNavigator
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get(),get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get(named(THEME_APP_PREFERENCES)))
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(named(HISTORY_PREFERENCES)),get(),)
    }

    single<TracksSearchRepository> {
        TracksRepositoryImpl(get())
    }
}