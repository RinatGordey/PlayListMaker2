package com.example.playlistmaker2.mediaLibrary.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.db.domain.db.PlaylistInteractor
import com.example.playlistmaker2.mediaLibrary.models.Playlist
import com.example.playlistmaker2.mediaLibrary.models.PlaylistState
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val application: Application,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistState>()
    private var _imageUri: String? = null

    init {
        getData()
    }

    fun observeState(): LiveData<PlaylistState> = stateLiveData

    fun getData() {
        viewModelScope.launch {
            playlistInteractor.getPlaylist().collect { playlists -> getState(playlists) }
        }
    }

    private fun getState(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            stateLiveData.postValue(PlaylistState.NoPlaylists)

        } else {
            stateLiveData.postValue(PlaylistState.PlaylistsContent(playlists))
        }
    }
}