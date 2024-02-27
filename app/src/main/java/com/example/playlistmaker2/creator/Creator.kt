package com.example.playlistmaker2.creator

import com.example.playlistmaker2.player.data.dto.Player
import com.example.playlistmaker2.player.domain.TrackInteractor
import com.example.playlistmaker2.player.presentation.TrackPresenter

object Creator {
    private fun getPlayer(url:String): Player {
        return Player(url)
    }

    fun providePresenter(url:String): TrackPresenter {
        return TrackPresenter(interactor = TrackInteractor(getPlayer(url)))
    }
}