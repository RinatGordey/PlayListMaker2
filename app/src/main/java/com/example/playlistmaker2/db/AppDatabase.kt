package com.example.playlistmaker2.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker2.db.data.dao.PlaylistDao
import com.example.playlistmaker2.db.data.dao.PlaylistTrackDao
import com.example.playlistmaker2.db.data.dao.TrackDao
import com.example.playlistmaker2.db.data.entity.TrackEntity
import com.example.playlistmaker2.db.data.entity.PlaylistEntity
import com.example.playlistmaker2.db.data.entity.PlaylistTrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class])

abstract class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun playlistTrackDao(): PlaylistTrackDao

}