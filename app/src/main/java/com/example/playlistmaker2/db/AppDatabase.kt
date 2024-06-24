package com.example.playlistmaker2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker2.db.data.Migration
import com.example.playlistmaker2.db.data.converters.DateConverter
import com.example.playlistmaker2.db.data.dao.PlaylistDao
import com.example.playlistmaker2.db.data.dao.TrackDao
import com.example.playlistmaker2.db.data.entity.FavoriteEntity
import com.example.playlistmaker2.db.data.entity.PlaylistEntity

@Database(version = 2, entities = [FavoriteEntity::class, PlaylistEntity::class])

@TypeConverters(DateConverter::class)

abstract class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

    companion object {
        fun newDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            .addMigrations(Migration.MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()
    }
}