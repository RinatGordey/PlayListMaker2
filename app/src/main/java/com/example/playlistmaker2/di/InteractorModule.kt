package com.example.playlistmaker2.di

import com.example.playlistmaker2.db.domain.db.FavoriteInteractor
import com.example.playlistmaker2.db.domain.impl.FavoriteInteractorImpl
import com.example.playlistmaker2.player.domain.api.PlayerInteractor
import com.example.playlistmaker2.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker2.search.domain.api.HistoryInteractor
import com.example.playlistmaker2.search.domain.api.SearchInteractor
import com.example.playlistmaker2.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker2.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker2.settings.domain.api.SettingsInteractor
import com.example.playlistmaker2.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker2.sharing.domain.api.SharingInteractor
import com.example.playlistmaker2.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    single<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
}