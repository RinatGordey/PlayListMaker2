package com.example.playlistmaker2.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.db.domain.db.FavoriteInteractor
import com.example.playlistmaker2.player.domain.api.PlayerInteractor
import com.example.playlistmaker2.player.domain.models.PlayerState
import com.example.playlistmaker2.player.ui.mapper.TrackMapper
import com.example.playlistmaker2.player.ui.model.PlaybackState
import com.example.playlistmaker2.player.ui.model.TrackInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerDisplayViewModel(
    private val lastTrack: TrackInfo,
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val trackMapper: TrackMapper,
    ) : ViewModel() {

    companion object {
        private const val REFRESH_MILLIS = 300L
        private const val START_TIMER = "00:00"
    }

    private val playingLiveData = MutableLiveData(PlaybackState(false, START_TIMER))
    fun getPlayingLiveData(): LiveData<PlaybackState> = playingLiveData

    private val favoriteLiveData = MutableLiveData(false)
    fun getFavoriteLiveData(): LiveData<Boolean> = favoriteLiveData
    init {
        getFavor(lastTrack.trackId)
    }

    private fun getFavor(id: Int) {
        viewModelScope.launch {
            favoriteLiveData.postValue(favoriteInteractor.isFavorite(id))
        }
    }

    private var playerTimerJob: Job? = null

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
        playingLiveData.postValue(
            PlaybackState(true, playerInteractor.getCurrentPosition()))

        playerTimerJob?.cancel()

        playerTimerJob = viewModelScope.launch {
            while (playerInteractor.getState() == PlayerState.PLAYING) {
                playingLiveData.postValue(
                    PlaybackState(true, playerInteractor.getCurrentPosition()))
                delay(REFRESH_MILLIS)
            }
            if (playerInteractor.getState() == PlayerState.END) {
                playingLiveData.postValue(PlaybackState(false, START_TIMER))
            }
        }
    }

    fun onPause() {
        playerInteractor.pause()
        playerTimerJob?.cancel()
        playingLiveData.postValue(
            PlaybackState(false, playerInteractor.getCurrentPosition()))
    }

    fun onDestroy() {
        playerInteractor.release()
    }

    fun likeClick() {
        viewModelScope.launch {
            if (favoriteInteractor.isFavorite(lastTrack.trackId)) {
                val track = trackMapper.map(lastTrack)
                favoriteInteractor.deleteFavorite(track)
                favoriteLiveData.postValue(false)
            } else {
                val track = trackMapper.map(lastTrack)
                favoriteInteractor.addFavorite(track)
                favoriteLiveData.postValue(true)
            }
        }
    }
}