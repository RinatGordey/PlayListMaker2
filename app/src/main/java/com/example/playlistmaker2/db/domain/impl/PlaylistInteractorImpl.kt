package com.example.playlistmaker2.db.domain.impl

import com.example.playlistmaker2.db.domain.db.PlaylistInteractor
import com.example.playlistmaker2.db.domain.db.PlaylistRepository
import com.example.playlistmaker2.mediaLibrary.domain.models.Playlist
import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
): PlaylistInteractor {

    override suspend fun addToPlaylist(playlist: Playlist) {
        playlistRepository.addToPlaylist(playlist)
    }

    override fun getPlaylist(): Flow<List<Playlist>> =
        playlistRepository.getPlaylist()

    override suspend fun addTrackToPlaylist(track: Track) {
        playlistRepository.addTrackToPlaylist(track)
    }

    override fun getTracksPlaylist(id: List<Int>): Flow<List<Track>> {
        return playlistRepository.getTracksPlaylist(id)
    }

    override suspend fun getCurrentPlaylist(id: Long): Playlist {
        return playlistRepository.getCurrentPlaylist(id)
    }

    override fun deleteTrack(idPL:Long, idTrack:Int): Flow<List<Track>> {
        return playlistRepository.deleteTrack(idPL, idTrack)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }
}