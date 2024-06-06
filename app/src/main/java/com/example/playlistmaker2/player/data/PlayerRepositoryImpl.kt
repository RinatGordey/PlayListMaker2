package com.example.playlistmaker2.player.data

import android.media.MediaPlayer
import com.example.playlistmaker2.player.domain.api.PlayerRepository
import com.example.playlistmaker2.player.domain.models.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl(
    private var mediaPlayer:MediaPlayer = MediaPlayer(),
    private var playerState: PlayerState) : PlayerRepository {
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun createPlayer(url: String) {
        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.END
        }
    }

    override fun play() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSED
    }

    override fun getState(): PlayerState {
        return playerState
    }

    override fun getCurrentPosition(): String {
        return dateFormat.format(mediaPlayer.currentPosition)
    }

    override fun release(){
        mediaPlayer.release()
    }

    override fun stop(){
        mediaPlayer.stop()
    }
}