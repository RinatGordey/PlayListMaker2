package com.example.playlistmaker2.mediaLibrary.domain.models

sealed interface PlaylistState {

    data object NoPlaylists : PlaylistState

    data class PlaylistsContent(
        val playlists: List<Playlist>,
    ): PlaylistState
}