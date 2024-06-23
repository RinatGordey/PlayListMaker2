package com.example.playlistmaker2.db.data.converters

import com.example.playlistmaker2.db.data.entity.PlaylistEntity
import com.example.playlistmaker2.mediaLibrary.models.Playlist

class PlaylistDbConvertor() {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.uri,
            playlist.tracksId,
            playlist.tracksCount,
        )
    }

    fun map(playlist: PlaylistEntity): Playlist{
        return Playlist(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.uri,
            playlist.tracksId,
            playlist.tracksCount,
        )
    }
}