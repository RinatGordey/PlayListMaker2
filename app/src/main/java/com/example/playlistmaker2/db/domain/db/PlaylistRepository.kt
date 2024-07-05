package com.example.playlistmaker2.db.domain.db

import com.example.playlistmaker2.mediaLibrary.domain.models.Playlist
import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addToPlaylist(playlist: Playlist)

    fun getPlaylist(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track)
}