package com.example.playlistmaker2.db.domain.impl

import com.example.playlistmaker2.db.domain.db.PlaylistInteractor
import com.example.playlistmaker2.db.domain.db.PlaylistRepository
import com.example.playlistmaker2.mediaLibrary.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
): PlaylistInteractor {

    override suspend fun addToPlaylist(playlist: Playlist) {
        playlistRepository.addToPlaylist(playlist)
    }

    override fun getPlaylist(): Flow<List<Playlist>> =
        playlistRepository.getPlaylist()

}