package com.example.playlistmaker2.player.domain

class TrackInteractor(
    private val player: PlayerInterface) {
    fun playbackControl(){
        player.playbackControl()
    }
    fun getPosition():Int{
        return player.getPosition()
    }
    fun delete(){
        player.delete()
    }
}