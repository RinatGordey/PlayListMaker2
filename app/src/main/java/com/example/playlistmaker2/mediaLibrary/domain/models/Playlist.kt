package com.example.playlistmaker2.mediaLibrary.domain.models

import java.io.Serializable

data class Playlist (
    val playlistId:Long?,
    val playlistName: String,
    val playlistDescription: String?,
    val uri: String?,
    val tracksId: String = "",
    var tracksCount: Int = 0
): Serializable