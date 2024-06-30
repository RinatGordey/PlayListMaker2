package com.example.playlistmaker2.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.db.domain.db.FavoriteInteractor
import com.example.playlistmaker2.db.domain.db.PlaylistInteractor
import com.example.playlistmaker2.mediaLibrary.models.Playlist
import com.example.playlistmaker2.mediaLibrary.models.PlaylistState
import com.example.playlistmaker2.player.domain.api.PlayerInteractor
import com.example.playlistmaker2.player.domain.models.PlayerState
import com.example.playlistmaker2.player.ui.mapper.TrackMapper
import com.example.playlistmaker2.player.ui.model.PlaybackState
import com.example.playlistmaker2.player.ui.model.TrackInfo
import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerDisplayViewModel(
    private val lastTrack: TrackInfo,
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistInteractor: PlaylistInteractor,
    get: Any,
    ) : ViewModel() {

    companion object {
        private const val REFRESH_MILLIS = 300L
        private const val START_TIMER = "00:00"
    }

    private val trackMapper = TrackMapper()

    private val playingLiveData = MutableLiveData(PlaybackState(false, START_TIMER))
    fun getPlayingLiveData(): LiveData<PlaybackState> = playingLiveData

    private val favoriteLiveData = MutableLiveData(false)
    fun getFavoriteLiveData(): LiveData<Boolean> = favoriteLiveData

    private val bottomSheetLiveData = MutableLiveData<PlaylistState>()
    fun getBottomSheetLiveData():LiveData<PlaylistState> = bottomSheetLiveData

    private val addToPlaylistLiveData = MutableLiveData<Pair<Boolean, String>>()
    fun getAddToPlaylistLiveData(): LiveData<Pair<Boolean, String>> = addToPlaylistLiveData

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
            PlaybackState(true, playerInteractor.getCurrentPosition())
        )
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

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylist().collect { playlists -> getState(playlists) }
        }
    }

    private fun getState(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            bottomSheetLiveData.postValue(PlaylistState.NoPlaylists)
        } else {
            bottomSheetLiveData.postValue(PlaylistState.PlaylistsContent(playlists))
        }
    }

    fun addToPlaylist(playlist: Playlist, tracks: List<Track>) {
        val trackIds = playlist.tracksId.toMutableList()
        val addedTracks = mutableListOf<Track>()

        tracks.forEach { track ->
            if (!playlist.playlistId?.let { trackIds.contains(it.toInt()) }!!) {
                trackIds.add(playlist.playlistId.toInt())
                addedTracks.add(track)
            }
        }

        val newTracksCount = playlist.tracksCount + addedTracks.size
        val newPlaylist = Playlist(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.uri,
            trackIds,
            newTracksCount
        )

        viewModelScope.launch {
            playlistInteractor.addToPlaylist(newPlaylist)
            addedTracks.forEach { addedTrack -> playlistInteractor.addTrackToPlaylist(addedTrack) }
        }

        addToPlaylistLiveData.postValue(Pair(false, playlist.playlistName))
    }
}