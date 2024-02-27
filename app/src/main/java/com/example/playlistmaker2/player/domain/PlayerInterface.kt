package com.example.playlistmaker2.player.domain

interface PlayerInterface {
    fun playbackControl()
    fun getPosition() : Int
    fun delete()
}