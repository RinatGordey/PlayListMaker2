package com.example.playlistmaker2.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long?,
    val playlistName: String,
    val playlistDescription: String?,
    val uri:String?,
    val tracksId: String,
    val tracksCount: Int,
)