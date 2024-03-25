package com.example.playlistmaker2.player.domain.api

import com.example.playlistmaker2.player.domain.models.PlayerState

interface PlayerRepository {
    fun createPlayer(url: String)
    fun play()
    fun pause()
    fun getState(): PlayerState
    fun getCurrentPosition(): String
    fun release()
    fun stop()
}