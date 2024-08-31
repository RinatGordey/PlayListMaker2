package com.example.playlistmaker2.mediaLibrary.view_model

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.db.domain.db.PlaylistInteractor
import com.example.playlistmaker2.mediaLibrary.domain.models.Playlist
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    playlistInteractor: PlaylistInteractor,
) : CreatePlaylistFragmentViewModel(playlistInteractor) {

    fun update(playlist: Playlist) {
        val newPlaylist =
            if (nameImage != null)
                Playlist(
                    playlist.playlistId,
                    playlist.playlistName,
                    playlist.playlistDescription,
                    nameImage,
                    playlist.tracksId,
                    playlist.tracksCount,
                ) else
                playlist

        viewModelScope.launch{
            playlistInteractor.updatePlaylist(newPlaylist)
        }
    }
}