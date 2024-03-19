package com.example.playlistmaker2.util

import android.content.Context
import com.example.playlistmaker2.player.data.PlayerRepositoryImpl
import com.example.playlistmaker2.player.domain.api.PlayerRepository
import com.example.playlistmaker2.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker2.search.data.HistoryRepositoryImpl
import com.example.playlistmaker2.search.data.TracksRepositoryImpl
import com.example.playlistmaker2.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker2.search.domain.api.HistoryRepository
import com.example.playlistmaker2.search.domain.api.SearchInteractor
import com.example.playlistmaker2.search.domain.api.TracksSearchRepository
import com.example.playlistmaker2.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker2.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker2.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker2.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker2.sharing.domain.impl.SharingInteractorImpl

object Creator {
    private fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }
    fun providePlayerInteractor(): PlayerInteractorImpl {
        return PlayerInteractorImpl(providePlayerRepository())
    }

    private fun getSearchRepository(context: Context): TracksSearchRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }
    fun provideSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(getSearchRepository(context))
    }

    private fun provideHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(context)
    }
    fun provideHistoryInteractor(context: Context): HistoryInteractorImpl {
        return HistoryInteractorImpl(provideHistoryRepository(context))
    }

    private fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }
    fun provideSharingInteractor(context: Context): SharingInteractorImpl {
        return SharingInteractorImpl(getExternalNavigator(context), context)
    }
}