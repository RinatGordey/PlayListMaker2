package com.example.playlistmaker2.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "favorite_table")
data class FavoriteEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    val trackTime: String?,
    val artworkUrl100: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val collectionName: String?,
    val country: String?,
    val previewUrl: String?,
    val deadline: Date?,
)