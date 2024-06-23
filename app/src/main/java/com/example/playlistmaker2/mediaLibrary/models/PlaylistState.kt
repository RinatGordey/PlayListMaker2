package com.example.playlistmaker2.mediaLibrary.models

sealed interface PlaylistState {

    data object NoPlaylists : PlaylistState

    data class PlaylistsContent(
        val playlists: List<PlaylistToRv>,
    ): PlaylistState
}