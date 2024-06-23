package com.example.playlistmaker2.db.data.impl

import com.example.playlistmaker2.db.AppDatabase
import com.example.playlistmaker2.db.data.converters.PlaylistDbConvertor
import com.example.playlistmaker2.db.data.entity.PlaylistEntity
import com.example.playlistmaker2.db.domain.db.PlaylistRepository
import com.example.playlistmaker2.mediaLibrary.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
) : PlaylistRepository {

    override suspend fun addToPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConvertor.map(playlist))
    }

    override fun getPlaylist(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylist()
        emit(convertFromPlaylistEntity(playlists))
    }

    private fun convertFromPlaylistEntity(playlistEntities: List<PlaylistEntity>): List<Playlist> {
        return playlistEntities.map { playlistEntity -> playlistDbConvertor.map(playlistEntity) }
    }
}