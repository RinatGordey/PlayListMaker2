package com.example.playlistmaker2.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker2.player.domain.api.PlayerInteractor
import com.example.playlistmaker2.player.domain.models.PlayerState
import com.example.playlistmaker2.player.ui.model.PlaybackState
import com.example.playlistmaker2.player.ui.model.TrackInfo
import com.example.playlistmaker2.util.Creator

class PlayerDisplayViewModel(
    private val lastTrack: TrackInfo,
    private val playerInteractor: PlayerInteractor) : ViewModel() {

    companion object {
        private const val REFRESH_MILLIS = 500L
        private const val START_TIMER = "00:00"

        fun getViewModelFactory(lastTrack: TrackInfo): ViewModelProvider.Factory = viewModelFactory {
                initializer {
                    PlayerDisplayViewModel(lastTrack, Creator.providePlayerInteractor())
                }
        }
    }

    private val playingLiveData = MutableLiveData(PlaybackState(false, START_TIMER))
    fun getPlayingLiveData(): LiveData<PlaybackState> = playingLiveData

    private val handler = Handler(Looper.getMainLooper())

    fun create() {
        playerInteractor.createPlayer(lastTrack.previewUrl)
    }

    fun play() {
        when (playerInteractor.getState()) {
            PlayerState.PLAYING -> {
                playerInteractor.pause()
                playingLiveData.postValue(
                    PlaybackState(false, playerInteractor.getCurrentPosition()))
            }
            PlayerState.PREPARED, PlayerState.PAUSED, PlayerState.DEFAULT, PlayerState.END -> {
                statePlaying()
            }
        }
    }
    private fun statePlaying() {
        playerInteractor.play()
        playingLiveData.postValue(PlaybackState(true, playerInteractor.getCurrentPosition()))
        handler.post(createUpdateTimerTask())
    }
    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                when (playerInteractor.getState()) {
                    PlayerState.PLAYING -> {
                        playingLiveData.postValue(PlaybackState(true, playerInteractor.getCurrentPosition()))
                        handler.postDelayed(this, REFRESH_MILLIS)
                    }
                    PlayerState.END -> {
                        handler.removeCallbacks(this)
                        playingLiveData.postValue(PlaybackState(false, START_TIMER))
                    }
                    else -> {}
                }
            }
        }
    }
    fun onPause() {
        playerInteractor.pause()
        playingLiveData.postValue(
            PlaybackState(
                false,
                playerInteractor.getCurrentPosition()
            )
        )
    }
    fun onDestroy() {
        playerInteractor.release()
    }
}