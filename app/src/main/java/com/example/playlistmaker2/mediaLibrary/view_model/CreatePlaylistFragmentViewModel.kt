package com.example.playlistmaker2.mediaLibrary.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.db.domain.db.PlaylistInteractor
import com.example.playlistmaker2.mediaLibrary.models.Playlist
import kotlinx.coroutines.launch

class CreatePlaylistFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.addToPlaylist(playlist)
        }
    }
}