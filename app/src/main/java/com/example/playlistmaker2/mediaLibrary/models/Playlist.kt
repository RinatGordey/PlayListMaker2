package com.example.playlistmaker2.mediaLibrary.models

import java.io.Serializable

data class Playlist(
    val playlistId:Long?,
    val playlistName: String,
    val playlistDescription: String?,
    val uri: String?,
    val tracksId: List<Int>,
    var tracksCount: Int,
) : Serializable