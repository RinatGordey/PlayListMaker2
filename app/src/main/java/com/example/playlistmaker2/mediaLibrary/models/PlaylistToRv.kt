package com.example.playlistmaker2.mediaLibrary.models

import java.io.File

data class PlaylistToRv(
    val playlistId:Long?,
    val playlistName: String,
    val playlistDescription: String?,
    val uri: File?,
    val tracksId: String = "",
    var tracksCount: Int = 0,
)
