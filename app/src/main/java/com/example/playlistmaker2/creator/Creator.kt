package com.example.playlistmaker2.creator

import com.example.playlistmaker2.player.data.PlayerRepositoryImpl
import com.example.playlistmaker2.player.domain.api.PlayerRepository
import com.example.playlistmaker2.player.domain.impl.PlayerInteractorImpl

object Creator {
    private fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }
    fun providePlayerInteractor(): PlayerInteractorImpl {
        return PlayerInteractorImpl(providePlayerRepository())
    }
}