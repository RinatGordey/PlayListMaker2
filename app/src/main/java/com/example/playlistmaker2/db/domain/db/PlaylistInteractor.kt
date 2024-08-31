package com.example.playlistmaker2.db.domain.db

import com.example.playlistmaker2.mediaLibrary.domain.models.Playlist
import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun addToPlaylist(playlist: Playlist)

    fun getPlaylist(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track)

    fun getTracksPlaylist(id:List<Int>): Flow<List<Track>>

    suspend fun getCurrentPlaylist(id:Long):Playlist

    fun deleteTrack(idPL:Long, idTrack:Int): Flow<List<Track>>

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)
}