package com.example.playlistmaker2.db.domain.db

import com.example.playlistmaker2.mediaLibrary.models.Playlist

interface PlaylistRepository {

    suspend fun addToPlaylist(playlist: Playlist)
}