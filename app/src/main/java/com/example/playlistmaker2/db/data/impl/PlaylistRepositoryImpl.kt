package com.example.playlistmaker2.db.data.impl

import com.example.playlistmaker2.db.AppDatabase
import com.example.playlistmaker2.db.data.converters.PlaylistDbConvertor
import com.example.playlistmaker2.db.domain.db.PlaylistRepository
import com.example.playlistmaker2.mediaLibrary.models.Playlist

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
) : PlaylistRepository {

    override suspend fun addToPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConvertor.map(playlist))
    }
}