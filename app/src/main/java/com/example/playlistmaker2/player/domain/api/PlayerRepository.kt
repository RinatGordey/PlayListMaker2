package com.example.playlistmaker2.player.domain.api

import com.example.playlistmaker2.player.domain.models.State

interface PlayerRepository {
    fun createPlayer(url: String)
    fun play()
    fun pause()
    fun getState(): State
    fun getCurrentPosition(): String
    fun release()
    fun stop()
}