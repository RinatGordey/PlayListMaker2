package com.example.playlistmaker2.player.data

import android.media.MediaPlayer
import com.example.playlistmaker2.player.domain.api.PlayerRepository
import com.example.playlistmaker2.player.domain.models.State
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl() : PlayerRepository {

    private var mediaPlayer = MediaPlayer()
    private var playerState: State = State.DEFAULT

    override fun createPlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener() {
            playerState = State.PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = State.END
        }
    }

    override fun play() {
        mediaPlayer.start()
        playerState = State.PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = State.PAUSED
    }

    override fun getState(): State {
        return playerState
    }

    override fun getCurrentPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(mediaPlayer.currentPosition)
    }
    override fun release(){
        mediaPlayer.release()
    }
    override fun stop(){
        mediaPlayer.stop()
    }
}