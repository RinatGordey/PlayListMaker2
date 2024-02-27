package com.example.playlistmaker2.player.data.dto

import android.media.MediaPlayer
import com.example.playlistmaker2.player.constants.Constants.STATE_PAUSED
import com.example.playlistmaker2.player.constants.Constants.STATE_PLAYING
import com.example.playlistmaker2.player.constants.Constants.STATE_PREPARED
import com.example.playlistmaker2.player.domain.PlayerInterface

class Player(url:String) : PlayerInterface {

    private var playerState = STATE_PAUSED

    private var mediaPlayer = MediaPlayer()

    init {
        mediaPlayer.setDataSource(url)
        this.preparePlayer()
    }

    override fun getPosition(): Int = mediaPlayer.currentPosition

    override fun delete() {
        mediaPlayer.release()
    }

    private fun preparePlayer() {
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
}