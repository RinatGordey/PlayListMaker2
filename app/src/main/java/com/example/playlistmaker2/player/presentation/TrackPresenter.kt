package com.example.playlistmaker2.player.presentation

import android.app.Activity
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.TextView
import com.example.playlistmaker2.R
import com.example.playlistmaker2.player.constants.Constants.REFRESH_MILLIS
import com.example.playlistmaker2.player.constants.Constants.STATE_PAUSED
import com.example.playlistmaker2.player.constants.Constants.STATE_PLAYING
import com.example.playlistmaker2.player.constants.Constants.STATE_PREPARED
import com.example.playlistmaker2.player.domain.TrackInteractor
import java.text.SimpleDateFormat

class TrackPresenter(private val interactor: TrackInteractor) {

    private val dateFormat = SimpleDateFormat("mm:ss")
    private var playerState = STATE_PAUSED
    private var handler = Handler(Looper.getMainLooper())
    private var mediaPlayer: MediaPlayer? = null

    fun onPlayClicked(play: ImageButton, time: TextView){
        playbackControl(play, time)
    }
    fun delete(){
        interactor.delete()
    }
    fun preparePlayer(play: ImageButton) {
        play.isEnabled = true
        play.setImageResource(R.drawable.ic_play_bt)
    }
    private fun startPlayer(play: ImageButton, time: TextView) {
        startTimer(time)
        play.setImageResource(R.drawable.ic_bt_pause)
        playerState = STATE_PLAYING
    }
    private fun pausePlayer(play: ImageButton) {
        play.setImageResource(R.drawable.ic_play_bt)
        playerState = STATE_PAUSED
    }
    private fun playbackControl(play: ImageButton, time: TextView) {
        interactor.playbackControl()
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer(play)
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer(play, time)
            }
        }
    }
    private fun startTimer(time: TextView) {
        handler.post(createUpdateTimerTask(time))
    }
    private fun createUpdateTimerTask(time: TextView): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING && mediaPlayer != null) {
                    time.text = dateFormat.format(mediaPlayer!!.currentPosition)
                    handler.postDelayed(this, REFRESH_MILLIS)
                }
            }
        }
    }
}
