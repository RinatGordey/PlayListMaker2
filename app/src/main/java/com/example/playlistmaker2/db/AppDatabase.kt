package com.example.playlistmaker2.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker2.db.data.dao.TrackDao
import com.example.playlistmaker2.db.data.entity.FavoriteEntity

@Database(version = 1, entities = [FavoriteEntity::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TrackDao
}