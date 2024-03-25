package com.example.playlistmaker2.player.domain.impl

import com.example.playlistmaker2.player.domain.api.PlayerInteractor
import com.example.playlistmaker2.player.domain.api.PlayerRepository
import com.example.playlistmaker2.player.domain.models.PlayerState

class PlayerInteractorImpl(
    private val playerRepository: PlayerRepository
): PlayerInteractor {
    override fun createPlayer(url: String) {
        playerRepository.createPlayer(url)
    }
    override fun play() {
        playerRepository.play()
    }
    override fun pause() {
        playerRepository.pause()
    }
    override fun getState(): PlayerState {
        return playerRepository.getState()
    }
    override fun getCurrentPosition(): String {
        return playerRepository.getCurrentPosition()
    }
    override fun release() {
        playerRepository.release()
    }
    override fun stop() {
        playerRepository.stop()
    }
}
