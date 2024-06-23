package com.example.playlistmaker2.db.domain.db

import com.example.playlistmaker2.mediaLibrary.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addToPlaylist(playlist: Playlist)

    fun getPlaylist(): Flow<List<Playlist>>
}