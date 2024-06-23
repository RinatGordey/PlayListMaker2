package com.example.playlistmaker2.mediaLibrary.models

data class Playlist(
    val playlistId:Long?,
    val playlistName: String,
    val playlistDescription: String?,
    val uri: String?,
    val tracksId: String = "",
    var tracksCount: Int = 0,
)