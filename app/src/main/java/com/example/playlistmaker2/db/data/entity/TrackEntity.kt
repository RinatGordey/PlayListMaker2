package com.example.playlistmaker2.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class FavoriteEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val releaseDate: String?,
    val primaryGenreName: String,
    val collectionName: String?,
    val country: String,
    val previewUrl: String?,
)